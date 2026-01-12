package ru.market.shopfront.integration.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.util.Objects;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.market.shopfront.controllers.ItemsController;
import ru.market.shopfront.dto.CartAction;
import ru.market.shopfront.dto.ItemDTO;
import ru.market.shopfront.dto.ItemsSort;
import ru.market.shopfront.integration.FixturesGenerator;
import ru.market.shopfront.services.CartsService;
import ru.market.shopfront.services.ItemsService;

@WebFluxTest(ItemsController.class)
class ItemsControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private ItemsService itemsService;

  @MockitoBean
  private CartsService cartsService;

  @Test
  @WithMockUser(username = "test-user")
  void shouldSearchItems() {
    when(itemsService.searchForUser(eq("test"), eq(ItemsSort.NO), any(PageRequest.class), eq("test-user"))).thenReturn(
        FixturesGenerator.generateItems().map(ItemDTO::from));

    checkSearchRequest();
  }

  private void checkSearchRequest() {
    when(itemsService.searchCount(eq("test"))).thenReturn(Mono.just(5));

    webTestClient.get()
        .uri("/items?search=test&sort=NO&pageNumber=1&pageSize=1")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentType("text/html")
        .expectBody(String.class)
        .value(html -> {
          assert html.contains("input type=\"hidden\" name=\"itemId\" value=\"2\">");
          assert html.contains("<input type=\"hidden\" name=\"search\" value=\"test\">");
          assert html.contains("<input type=\"hidden\" name=\"sort\" value=\"NO\">");
          assert html.contains("<input type=\"hidden\" name=\"pageSize\" value=\"1\">");
          assert html.contains("<input type=\"hidden\" name=\"pageNumber\" value=\"1\">");
        });
  }

  @Test
  @WithMockUser(username = "test-user")
  void shouldGetItemById() {
    when(itemsService.getItemByIdForUser(1, "test-user"))
        .thenReturn(Mono.just(Objects.requireNonNull(FixturesGenerator.generateItemDTO().blockFirst())));

    checkGetByIdRequest();
  }

  @Test
  void shouldGetItemByIdForUnauthorizedUser() {
    when(itemsService.getItemById(1))
        .thenReturn(Mono.just(Objects.requireNonNull(FixturesGenerator.generateItemDTO().blockFirst())));

    checkGetByIdRequest();
  }

  private void checkGetByIdRequest() {
    webTestClient.get()
        .uri("/items/1")
        .header("Accept", "text/html")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentType("text/html")
        .expectBody(String.class)
        .value(html -> {
          assert html.contains("<h5 class=\"card-title\">Test</h5>");
        });
  }

  @Nested
  @WebFluxTest(ItemsController.class)
  class AddRemoveToCartFromItemsView {

    private static final String ACTUAL_URL_PARAMS = "search=test&sort=ALPHA&pageNumber=1&pageSize=5";

    @ParameterizedTest
    @EnumSource(CartAction.class)
    @WithMockUser(username = "test-user")
    void shouldAddRemoveItemToCart(CartAction action) {
      when(cartsService.addRemoveToCart("test-user", 1, action)).thenReturn(Mono.empty());
      when(cartsService.getCart("test-user")).thenReturn(FixturesGenerator.generateItemDTO());

      generateAddRemoveSpec(action)
          .expectStatus().is3xxRedirection();
    }

    @ParameterizedTest
    @EnumSource(CartAction.class)
    void shouldDenyAddRemoveItemToCartForUnauthorizedUser(CartAction action) {
      generateAddRemoveSpec(action)
          .expectStatus().is3xxRedirection();
    }

    private WebTestClient.ResponseSpec generateAddRemoveSpec(CartAction action) {
      return webTestClient.mutateWith(csrf())
          .post()
          .uri("/items?" + ACTUAL_URL_PARAMS)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(FixturesGenerator.generateAddRemoveToCartBody(action)))
          .exchange();
    }

  }

  @Nested
  @WebFluxTest(ItemsController.class)
  class AddRemoveToCartFromItemDetailView {

    @ParameterizedTest
    @EnumSource(CartAction.class)
    @WithMockUser(username = "test-user", roles = "USER")
    void shouldAddRemoveItemToCart(CartAction action) {
      when(cartsService.addRemoveToCart("test-user", 1, action)).thenReturn(Mono.empty());
      when(itemsService.getItemByIdForUser(1, "test-user")).thenReturn(
          Mono.just(Objects.requireNonNull(FixturesGenerator.generateItemDTO().blockFirst())));

      var responseSpec = generateAddRemoveSpec(action);

      switch (action) {
        case PLUS -> responseSpec.expectStatus().is2xxSuccessful();
        case MINUS -> responseSpec.expectStatus().is2xxSuccessful();
        case DELETE -> responseSpec.expectStatus().is3xxRedirection();
      }
    }

    @ParameterizedTest
    @EnumSource(CartAction.class)
    void shouldDenyAddRemoveItemToCartForUnauthorizedUser(CartAction action) {
      generateAddRemoveSpec(action)
          .expectStatus().is3xxRedirection();
    }

    private WebTestClient.ResponseSpec generateAddRemoveSpec(CartAction action) {
      return webTestClient.mutateWith(csrf())
          .post()
          .uri("/items/1")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(FixturesGenerator.generateAddRemoveToCartBody(action)))
          .exchange();
    }

  }

  @Test
  @WithMockUser(username = "test-user", roles = "USER")
  void shouldGetImage() {
    checkGetImage();
  }

  private void checkGetImage() {
    byte[] image = {108};

    when(itemsService.getItemImage(1)).thenReturn(Mono.just(image));

    webTestClient.get()
        .uri("/items/image/1")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(byte[].class)
        .isEqualTo(image);
  }

}
