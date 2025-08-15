package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;

public interface PaymentRequestVerificationService extends verificationService{

    /**
     * verifies any payment request
     */
    public PaymentVerificationResult verifyPayment(TransactionRequest paymentRequest);
}
