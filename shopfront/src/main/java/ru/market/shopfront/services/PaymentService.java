package ru.market.shopfront.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.market.shopfront.payment.client.api.DefaultApi;
import ru.market.shopfront.payment.domain.Amount;
import ru.market.shopfront.payment.domain.PaymentRequest;
import ru.market.shopfront.payment.domain.PaymentResult;

@Component
public class PaymentService {

  // TODO how to rename DefaultApi -> Payment[Api/Client]?
  private final DefaultApi paymentApiClient = new DefaultApi();

  public Mono<PaymentResult> chargePayment(UUID accountId, UUID operationId, Integer amount) {
    var paymentRequest = new PaymentRequest();
    paymentRequest.setAmount(BigDecimal.valueOf(amount));

    return paymentApiClient.executePayment(accountId, operationId, paymentRequest);
  }

  public Mono<Integer> getBalance(UUID accountId) {
    return paymentApiClient.getAccountBalance(accountId)
        .map(val -> {
          return val.getAmount();
        })
            //Amount::getAmount)
//        .onErrorReturn(BigDecimal.valueOf(0))
        .map(BigDecimal::intValue);
  }

}
