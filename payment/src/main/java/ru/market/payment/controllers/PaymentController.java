package ru.market.payment.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import ru.market.payment.server.api.DefaultApi;
import ru.market.payment.server.domain.AccountBalance;
import ru.market.payment.server.domain.PaymentRequest;
import ru.market.payment.server.domain.PaymentResult;
import ru.market.payment.services.PaymentService;

@Controller
@RequestMapping("v1")
public class PaymentController implements DefaultApi {

  @Autowired
  private PaymentService paymentService;

  @Override
  @PreAuthorize("hasAuthority('SERVICE')")
  public Mono<ResponseEntity<PaymentResult>> executePayment(UUID accountId, UUID operationId,
                                                            Mono<PaymentRequest> paymentRequest,
                                                            final ServerWebExchange exchange) {
    return paymentService.executePayment(accountId, operationId, paymentRequest);
  }

  @Override
  @PreAuthorize("hasAuthority('SERVICE')")
  public Mono<ResponseEntity<AccountBalance>> getAccountBalance(UUID accountId, final ServerWebExchange exchange) {
    return paymentService.getAccountBalance(accountId);
  }

}