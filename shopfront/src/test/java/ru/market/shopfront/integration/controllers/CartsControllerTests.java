package ru.market.shopfront.integration.controllers;

import static org.mockito.Mockito.when;

import java.util.Objects;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.market.shopfront.controllers.CartsController;
import ru.market.shopfront.dto.CartAction;
import ru.market.shopfront.dto.OrderDTO;
import ru.market.shopfront.models.Order;
import ru.market.shopfront.services.CartsService;

@WebFluxTest(CartsController.class)
class CartsControllerTests extends BaseControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private CartsService cartsService;

  @Test
  void shouldGetCartItems() {
    when(cartsService.getCart(999)).thenReturn(generateItemDTO());

    webTestClient.get()
        .uri("/cart/items")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
        .expectBody(String.class)
        .value(html -> {
          assert html.contains("<h2>Итого: 2000 руб.</h2>");
          assert html.contains("<button class=\"btn btn-warning ms-auto\">Купить</button>");
        });
  }

  @Test
  void shouldBuyCartItems() {
    when(cartsService.buy(999))
        .thenReturn(Mono.just(
                OrderDTO.from(new Order(1111), Objects.requireNonNull(generateOrderItems().collectList().block()))
            )
        );

    webTestClient.post()
        .uri("/cart/buy")
        .exchange()
        .expectStatus().is3xxRedirection();
  }

  @Nested
  @WebFluxTest(CartsController.class)
  class AddRemoveToCart {

    @ParameterizedTest
    @EnumSource(CartAction.class)
    void shouldAddRemoveItemToCart(CartAction action) {
      when(cartsService.addRemoveToCart(999, 1, action)).thenReturn(Mono.empty());
      when(cartsService.getCart(999)).thenReturn(generateItemDTO());

      webTestClient.post()
          .uri("/cart/items")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(generateAddRemoveToCartBody(action)))
          .exchange()
          .expectStatus().is2xxSuccessful()
          .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
          .expectBody(String.class)
          .value(html -> {
            assert html.contains("<button class=\"btn btn-warning ms-auto\">Купить</button>");
          });
    }

  }

}
