package ru.market.shopfront.integration.controllers;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.market.shopfront.controllers.OrdersController;
import ru.market.shopfront.services.OrdersService;

@WebFluxTest(OrdersController.class)
class OrdersControllerTests extends BaseControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private OrdersService ordersService;

  @Test
  void shouldGetOrderById() {
    when(ordersService.getOrder(1)).thenReturn(generateOrderDTO());

    webTestClient.get()
        .uri("/orders/1")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
        .expectBody(String.class)
        .value(html -> {
          assert html.contains("<b>Сумма: 111100 руб.</b>");
        });
  }

  @Test
  void shouldGetOrders() {
    when(ordersService.getOrders()).thenReturn(Flux.just(generateOrderDTO().block(), generateOrderDTO().block()));

    webTestClient.get()
        .uri("/orders")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_HTML)
        .expectBody(String.class)
        .value(html -> {
          assert html.contains("<a href=\"/orders/null\">Заказ №null</a>");
        });
  }

}
