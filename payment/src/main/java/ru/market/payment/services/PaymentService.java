package ru.market.payment.services;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.market.payment.server.domain.AccountBalance;
import ru.market.payment.server.domain.PaymentRequest;
import ru.market.payment.server.domain.PaymentResult;

@Service
public class PaymentService {

  public static final BigDecimal AMOUNT = BigDecimal.valueOf(15000);

  public Mono<ResponseEntity<PaymentResult>> executePayment(UUID accountId, UUID operationId,
                                                            Mono<PaymentRequest> paymentRequest) {
    return paymentRequest.map(request -> {
          PaymentResult paymentResult = new PaymentResult();

          PaymentResult.StatusEnum status = AMOUNT.subtract(request.getAmount()).intValue() > 0
              ? PaymentResult.StatusEnum.SUCCESS
              : PaymentResult.StatusEnum.FAILED;

          paymentResult.setStatus(status);

          return ResponseEntity.ok(paymentResult);
        }
    );
  }

  public Mono<ResponseEntity<AccountBalance>> getAccountBalance(UUID accountId) {
    return Mono.just(ResponseEntity.ok(new AccountBalance(AMOUNT)));
  }

}
