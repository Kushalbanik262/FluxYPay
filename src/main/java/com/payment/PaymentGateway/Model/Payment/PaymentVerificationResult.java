package com.payment.PaymentGateway.Model.Payment;

import com.payment.PaymentGateway.Model.Tables.TransactionToken;

/**
 * Payment verification Result
 */
public class PaymentVerificationResult {
    private int statusCode;
    private String errorMessage;
    private TransactionToken paymentToken;
}
