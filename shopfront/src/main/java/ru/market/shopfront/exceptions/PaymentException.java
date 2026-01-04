package ru.market.shopfront.exceptions;

public class PaymentException extends RuntimeException {

  public PaymentException(String message) {
    super(message);
  }

}
