package ru.market.shopfront.integration.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import java.util.Objects;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;
import ru.market.shopfront.controllers.CartsController;
import ru.market.shopfront.dto.CartAction;
import ru.market.shopfront.dto.OrderDTO;
import ru.market.shopfront.integration.FixturesGenerator;
import ru.market.shopfront.models.Order;
import ru.market.shopfront.payment.domain.PaymentResult;
import ru.market.shopfront.services.CartsService;
import ru.market.shopfront.services.PaymentService;

@WebFluxTest(CartsController.class)
class CartsControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private CartsService cartsService;

  @MockitoBean
  private PaymentService paymentService;

  @Test
  @WithMockUser(username = "test-user")
  void shouldGetCartItems() {
    when(cartsService.getCart("test-user")).thenReturn(FixturesGenerator.generateItemDTO());
    when(paymentService.getBalance(any(UUID.class))).thenReturn(Mono.just(15000));

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
  void shouldDenyGetCartItemsForUnauthorizedUser() {
    webTestClient.get()
        .uri("/cart/items")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @WithMockUser(username = "test-user")
  void shouldNotContainBuyButtonOnEmptyBalance() {
    when(cartsService.getCart("test-user")).thenReturn(FixturesGenerator.generateItemDTO());
    when(paymentService.getBalance(any(UUID.class))).thenReturn(Mono.just(0));

    webTestClient.get()
        .uri("/cart/items")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
        .expectBody(String.class)
        .value(html -> {
          assert !html.contains("<h2>Итого: 2000 руб.</h2>");
          assert !html.contains("<button class=\"btn btn-warning ms-auto\">Купить</button>");
        });
  }

  @Test
  @WithMockUser(username = "test-user")
  void shouldBuyCartItems() {
    var userId = "test-user";
    when(cartsService.buy(userId))
        .thenReturn(Mono.just(
                OrderDTO.from(new Order(userId, 1111), Objects.requireNonNull(FixturesGenerator.generateOrderItems().collectList().block()))
            )
        );
    var paymentResult = new PaymentResult();
    paymentResult.setStatus(PaymentResult.StatusEnum.SUCCESS);
    when(paymentService.chargePayment(any(UUID.class), any(UUID.class), any(Integer.class))).thenReturn(Mono.just(paymentResult));

    webTestClient.mutateWith(csrf())
        .post()
        .uri("/cart/buy")
        .exchange()
        .expectStatus().is3xxRedirection();
  }

  @Test
  void shouldDenyBuyCartItemsForUnauthorizedUser() {
    webTestClient.mutateWith(csrf())
        .post()
        .uri("/cart/buy")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @WithMockUser(username = "test-user")
  void shouldReturnPaymentErrorOnBuying() {
    when(cartsService.buy("test-user"))
        .thenReturn(Mono.just(
                OrderDTO.from(new Order("test-user", 1111), Objects.requireNonNull(FixturesGenerator.generateOrderItems().collectList().block()))
            )
        );
    var paymentResult = new PaymentResult();
    paymentResult.setStatus(PaymentResult.StatusEnum.FAILED);
    when(paymentService.chargePayment(any(UUID.class), any(UUID.class), any(Integer.class))).thenReturn(Mono.just(paymentResult));
    when(cartsService.getCart("test-user")).thenReturn(FixturesGenerator.generateItemDTO());
    when(paymentService.getBalance(any(UUID.class))).thenReturn(Mono.just(15000));

    webTestClient.mutateWith(csrf())
        .post()
        .uri("/cart/buy")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody(String.class)
        .value(html -> {
          assert html.contains("Ошибка оплаты, статус: " + PaymentResult.StatusEnum.FAILED);
        });
  }

  @Nested
  @WebFluxTest(CartsController.class)
  class AddRemoveToCart {

    @ParameterizedTest
    @EnumSource(CartAction.class)
    @WithMockUser(username = "test-user")
    void shouldAddRemoveItemToCart(CartAction action) {
      when(cartsService.addRemoveToCart("test-user", 1, action)).thenReturn(Mono.empty());
      when(cartsService.getCart("test-user")).thenReturn(FixturesGenerator.generateItemDTO());
      when(paymentService.getBalance(any(UUID.class))).thenReturn(Mono.just(15000));

      generateAddRemoveSpec(action)
          .expectStatus().is2xxSuccessful()
          .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
          .expectBody(String.class)
          .value(html -> {
            assert html.contains("<button class=\"btn btn-warning ms-auto\">Купить</button>");
          });
    }

    @ParameterizedTest
    @EnumSource(CartAction.class)
    void shouldDenyAddRemoveItemToCart(CartAction action) {
      generateAddRemoveSpec(action)
          .expectStatus().isUnauthorized();
    }

    private WebTestClient.ResponseSpec generateAddRemoveSpec(CartAction action) {
      return webTestClient.mutateWith(csrf())
          .post()
          .uri("/cart/items")
          .contentType(MediaType.APPLICATION_FORM_URLENCODED)
          .body(BodyInserters.fromFormData(FixturesGenerator.generateAddRemoveToCartBody(action)))
          .exchange();
    }

  }

}
