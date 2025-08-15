package com.payment.PaymentGateway.PaymentIntegration.Validator;

import com.payment.PaymentGateway.Model.Tables.PaymentRequest;
import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;

import java.util.concurrent.Future;

/**
 * Payment verification for any payment Integration platform
 */
public interface Validator {
    /**
     * Verify before issuing a payment token
     * @param transactionRequest Request for the transaction
     */
    public Future<PaymentVerificationResult> verifyPaymentDuringTransaction(TransactionRequest transactionRequest);

    /**
     * verify During payment
     * @param paymentRequest verify During Actual payment
     */
    public Future<PaymentVerificationResult> verifyPaymentDuringPayment(PaymentRequest paymentRequest);
}
