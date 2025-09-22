package com.payment.PaymentGateway.Model.Payment;

import com.payment.PaymentGateway.Model.Tables.TransactionToken;

/**
 * Payment verification Result
 */
public class PaymentVerificationResult {
    private int statusCode;
    private String errorMessage;
    private TransactionToken paymentToken;

    public PaymentVerificationResult(int statusCode, String errorMessage, TransactionToken paymentToken) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.paymentToken = paymentToken;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public TransactionToken getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(TransactionToken paymentToken) {
        this.paymentToken = paymentToken;
    }
}
