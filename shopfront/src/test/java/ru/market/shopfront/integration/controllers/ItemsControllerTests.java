package ru.market.shopfront.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Objects;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
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
  void shouldSearchItems() {
    when(itemsService.search(eq("test"), eq(ItemsSort.NO), any(PageRequest.class))).thenReturn(
        FixturesGenerator.generateItems().map(ItemDTO::from));
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
  void shouldGetItemById() {
    when(itemsService.getItemById(1)).thenReturn(Mono.just(Objects.requireNonNull(FixturesGenerator.generateItemDTO().blockFirst())));

    webTestClient.get()
        .uri("/items/1")
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
    void shouldAddRemoveItemToCart(CartAction action) {
      when(cartsService.addRemoveToCart(999, 1, action)).thenReturn(Mono.empty());
      when(cartsService.getCart(999)).thenReturn(FixturesGenerator.generateItemDTO());

      webTestClient.post()
          .uri("/items?" + ACTUAL_URL_PARAMS)
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(FixturesGenerator.generateAddRemoveToCartBody(action)))
          .exchange()
          .expectStatus().is3xxRedirection();
    }

  }

  @Nested
  @WebFluxTest(ItemsController.class)
  class AddRemoveToCartFromItemDetailView {

    @ParameterizedTest
    @EnumSource(CartAction.class)
    void shouldAddRemoveItemToCart(CartAction action) {
      when(cartsService.addRemoveToCart(999, 1, action)).thenReturn(Mono.empty());
      when(itemsService.getItemById(1)).thenReturn(Mono.just(Objects.requireNonNull(FixturesGenerator.generateItemDTO().blockFirst())));

      var responseSpec = webTestClient.post()
          .uri("/items/1")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(FixturesGenerator.generateAddRemoveToCartBody(action)))
          .exchange();

      switch (action) {
        case PLUS -> responseSpec.expectStatus().is2xxSuccessful();
        case MINUS -> responseSpec.expectStatus().is2xxSuccessful();
        case DELETE -> responseSpec.expectStatus().is3xxRedirection();
      }
    }

  }

  @Test
  void shouldGetImage() {
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
