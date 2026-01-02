package ru.market.shopfront.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.market.shopfront.payment.client.api.DefaultApi;

@Configuration
public class PaymentServiceClientConfig {

  @Bean
  DefaultApi paymentClient() {
    return new DefaultApi();
  }

}
