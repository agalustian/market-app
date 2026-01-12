package ru.market.shopfront.integration.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import ru.market.shopfront.controllers.OrdersController;
import ru.market.shopfront.integration.FixturesGenerator;
import ru.market.shopfront.services.OrdersService;

@WebFluxTest(OrdersController.class)
class OrdersControllerTests {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private OrdersService ordersService;

  @Test
  void shouldRedirectUnauthorizedUserToLoginPage() {
    webTestClient.get()
        .uri("/orders/1")
        .header("Accept", "text/html")
        .exchange()
        .expectStatus().is3xxRedirection()
        .expectHeader().value("Location", location ->
            assertThat(location).contains("/login"));
  }

  @Test
  @WithMockUser(username = "test-user")
  void shouldGetOrderById() {
    when(ordersService.getOrder("test-user", 1)).thenReturn(FixturesGenerator.generateOrderDTO());

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
  void shouldDenyGetOrderByIdForUnauthorizedUser() {
    webTestClient.get()
        .uri("/orders/1")
        .exchange()
        .expectStatus().is3xxRedirection();
  }

  @Test
  @WithMockUser(username = "test-user")
  void shouldGetOrders() {
    when(ordersService.getOrders("test-user")).thenReturn(Flux.just(FixturesGenerator.generateOrderDTO().block(), FixturesGenerator.generateOrderDTO().block()));

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

  @Test
  void shouldDenyGetOrdersForUnauthorizedUser() {
    webTestClient.get()
        .uri("/orders")
        .exchange()
        .expectStatus().is3xxRedirection();
  }

}
