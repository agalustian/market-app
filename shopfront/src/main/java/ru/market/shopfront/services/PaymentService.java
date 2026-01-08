package ru.market.shopfront.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.market.shopfront.payment.client.api.DefaultApi;
import ru.market.shopfront.payment.domain.PaymentRequest;
import ru.market.shopfront.payment.domain.PaymentResult;

@Service
public class PaymentService {

  // TODO how to rename DefaultApi -> Payment[Api/Client]?
  private final DefaultApi paymentApiClient;

  public PaymentService(DefaultApi paymentApiClient) {
    this.paymentApiClient = paymentApiClient;
  }

  public Mono<PaymentResult> chargePayment(UUID accountId, UUID operationId, Integer amount) {
    var paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.valueOf(amount));

    return paymentApiClient.executePayment(accountId, operationId, paymentRequest);
  }

  // TODO use logger
  public Mono<Integer> getBalance(UUID accountId) {
    return paymentApiClient.getAccountBalance(accountId)
        .map(amount -> {
          System.out.println("Received amount balance: " + amount.getAmount());
          return amount.getAmount();
        })
        .onErrorResume(err -> {
          System.out.println("Received error: " + err.getMessage());
          return Mono.just(BigDecimal.valueOf(0));
        })
        .map(BigDecimal::intValue);
  }

}
