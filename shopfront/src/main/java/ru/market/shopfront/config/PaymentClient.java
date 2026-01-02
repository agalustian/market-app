package ru.market.shopfront.config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import ru.market.shopfront.payment.client.api.DefaultApi;

@Configurable
public class PaymentClient {

  @Bean
  DefaultApi paymentClient() {
    return new DefaultApi();
  }

}
