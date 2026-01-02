package ru.market.shopfront.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import ru.market.shopfront.payment.client.api.DefaultApi;
import ru.market.shopfront.payment.domain.Amount;
import ru.market.shopfront.payment.domain.PaymentRequest;
import ru.market.shopfront.payment.domain.PaymentResult;

public class PaymentService {

  // TODO how to rename DefaultApi -> Payment[Api/Client]?
  @Autowired
  private DefaultApi paymentApiClient;

  public Mono<PaymentResult> chargePayment(UUID accountId, UUID operationId, PaymentRequest paymentRequest) {
    return paymentApiClient.executePayment(accountId, operationId, paymentRequest)
  }

  public Mono<Integer> getBalance(UUID accountId) {
    return paymentApiClient.getAccountBalance(accountId).map(Amount::getAmount).onErrorReturn(BigDecimal.valueOf(0))
        .map(BigDecimal::intValue);
  }

}
