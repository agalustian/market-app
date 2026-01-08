package ru.market.shopfront.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import ru.market.shopfront.payment.client.ApiClient;
import ru.market.shopfront.payment.client.api.DefaultApi;

@Configuration
public class PaymentServiceClientConfig {

  @Bean
  ApiClient paymentPrivateApiClient(WebClient webClient) {
    return new ApiClient(webClient);
  }

  @Bean
  DefaultApi paymentClient(ApiClient apiClient) {
    return new DefaultApi(apiClient);
  }

}
