package ru.market.payment.controllers;

import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.market.payment.server.api.DefaultApi;
import ru.market.payment.server.domain.AccountBalance;
import ru.market.payment.server.domain.PaymentRequest;
import ru.market.payment.server.domain.PaymentResult;

@Controller
@RequestMapping("v1")
public class PaymentController implements DefaultApi {
  static final BigDecimal AMOUNT = BigDecimal.valueOf(15000);

  //  @PreAuthorize("hasRole('SERVICE')")
  @Override
  public Mono<ResponseEntity<PaymentResult>> executePayment(UUID accountId, UUID operationId,
                                                     Mono<PaymentRequest> paymentRequest,
                                                     final ServerWebExchange exchange) {
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

  @Override
  public Mono<ResponseEntity<AccountBalance>> getAccountBalance(UUID accountId, final ServerWebExchange exchange) {
    return Mono.just(ResponseEntity.ok(new AccountBalance(AMOUNT)));
  }

}