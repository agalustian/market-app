package ru.market.payment.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import ru.market.payment.server.domain.PaymentRequest;
import ru.market.payment.server.domain.PaymentResult;
import ru.market.payment.services.PaymentService;

class PaymentServiceTests {

  private final PaymentService paymentService = new PaymentService();

  @Test
  void shouldExecutePaymentWithMoneyEnough() {

    paymentService.executePayment(UUID.randomUUID(), UUID.randomUUID(),
            Mono.just(new PaymentRequest(BigDecimal.valueOf(10000))))
        .doOnNext(paymentResult -> {
          assertEquals(PaymentResult.StatusEnum.SUCCESS, paymentResult.getBody().getStatus());
        });
  }

  @Test
  void shouldExecutePaymentWithMoneyNotEnough() {

    paymentService.executePayment(UUID.randomUUID(), UUID.randomUUID(),
            Mono.just(new PaymentRequest(BigDecimal.valueOf(22220000))))
        .doOnNext(paymentResult -> {
          assertEquals(PaymentResult.StatusEnum.FAILED, paymentResult.getBody().getStatus());
        });
  }

  @Test
  void shouldGetAccountBalance() {

    paymentService.getAccountBalance(UUID.randomUUID())
        .doOnNext(accountBalance -> {
          assertEquals(PaymentService.AMOUNT, accountBalance.getBody().getAmount());
        });
  }

}
