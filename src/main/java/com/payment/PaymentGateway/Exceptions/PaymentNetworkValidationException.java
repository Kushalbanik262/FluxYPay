package com.payment.PaymentGateway.Exceptions;

public class PaymentNetworkValidationException extends RuntimeException {
    public PaymentNetworkValidationException(String message) {
        super(message);
    }
}
