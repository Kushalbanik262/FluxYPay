package com.payment.PaymentGateway.Model.Payment;

import com.payment.PaymentGateway.Model.Tables.Token;

/**
 * Payment verification Result
 */
public class PaymentVerificationResult {
    private int statusCode;
    private String errorMessage;
    private Token paymentToken;
}
