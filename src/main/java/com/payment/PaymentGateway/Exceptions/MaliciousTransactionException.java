package com.payment.PaymentGateway.Exceptions;

public class MaliciousTransactionException extends RuntimeException {
    public MaliciousTransactionException(String message) {
        super(message);
    }
}
