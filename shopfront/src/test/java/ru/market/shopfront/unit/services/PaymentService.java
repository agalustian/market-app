package ru.market.shopfront.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import ru.market.shopfront.exceptions.PaymentException;
import ru.market.shopfront.payment.client.api.DefaultApi;
import ru.market.shopfront.payment.domain.AccountBalance;
import ru.market.shopfront.payment.domain.PaymentRequest;
import ru.market.shopfront.payment.domain.PaymentResult;
import ru.market.shopfront.services.PaymentService;

class PaymentServiceTests {

  @Mock
  private DefaultApi paymentClient = Mockito.mock(DefaultApi.class);

  @InjectMocks
  private PaymentService paymentService = new PaymentService(this.paymentClient);

  @Test
  void shouldToGetBalance() {
    var accountBalance = new AccountBalance();
    accountBalance.setAmount(BigDecimal.valueOf(1500));
    var uuid = UUID.randomUUID();

    when(paymentClient.getAccountBalance(uuid)).thenReturn(Mono.just(accountBalance));

    var balance = paymentService.getBalance(uuid).block();

    assertEquals(1500, balance);
    verify(paymentClient, times(1)).getAccountBalance(uuid);
  }

  @Test
  void shouldToGetEmptyBalanceOnPaymentServiceError() {
    var uuid = UUID.randomUUID();

    when(paymentClient.getAccountBalance(uuid)).thenReturn(Mono.error(new PaymentException("test exception!")));

    var balance = paymentService.getBalance(uuid).block();

    assertEquals(0, balance);
    verify(paymentClient, times(1)).getAccountBalance(uuid);
  }

  @Test
  void shouldToChargePayment() {
    var accountUUID = UUID.randomUUID();
    var operationUUID = UUID.randomUUID();
    var expectedAmount = 10000;
    var expectedResult = new PaymentResult();
    expectedResult.setStatus(PaymentResult.StatusEnum.SUCCESS);

    when(paymentClient.executePayment(eq(accountUUID), eq(operationUUID), any(PaymentRequest.class)))
        .thenReturn(Mono.just(expectedResult));

    var result = paymentService.chargePayment(accountUUID, operationUUID, expectedAmount).block();

    assertEquals(PaymentResult.StatusEnum.SUCCESS, result.getStatus());
    verify(paymentClient, times(1))
        .executePayment(eq(accountUUID), eq(operationUUID), any(PaymentRequest.class));
  }

  @Test
  void shouldReturnErrorOnChargePaymentError() {
    var errorMessage = "exception!";
    when(paymentClient.executePayment(any(UUID.class), any(UUID.class), any(PaymentRequest.class)))
        .thenReturn(Mono.error(new PaymentException(errorMessage)));

    paymentService
        .chargePayment(UUID.randomUUID(), UUID.randomUUID(), 1000)
        .onErrorResume(err -> {
          assertEquals(errorMessage, err.getMessage());

          verify(paymentClient, times(1))
              .executePayment(any(UUID.class), any(UUID.class), any(PaymentRequest.class));

          return Mono.empty();
        }).block();
  }

}
