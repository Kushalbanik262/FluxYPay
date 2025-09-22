package com.payment.PaymentGateway.Exceptions;

/**
 * Exception Related to Client Verification
 */
public class ClientVerificationException extends RuntimeException {
    public ClientVerificationException(String message) {
        super(message);
    }
}
