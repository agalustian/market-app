package ru.market.payment.integration.controllers;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.market.payment.server.domain.AccountBalance;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class PaymentServiceTests {

  static final UUID ID = UUID.fromString("046b6c7f-0b8a-43b9-b35d-6489e6daee91");

  @Autowired
  private WebTestClient webClient;

  @Test
  void shouldToGetUnauthorizedError() {
    webClient.get().uri("/v1/accounts/"+ID+"/balance")
        .exchange()
        .expectStatus().isUnauthorized();
  }

  @Test
  @WithMockUser(authorities = "SERVICE")
  void shouldToGetBalance() {
    webClient.get().uri("/v1/accounts/"+ID+"/balance")
        .exchange()
        .expectStatus().isOk()
        .expectBody(AccountBalance.class)
        .isEqualTo(new AccountBalance(BigDecimal.valueOf(15000)));
  }

}
