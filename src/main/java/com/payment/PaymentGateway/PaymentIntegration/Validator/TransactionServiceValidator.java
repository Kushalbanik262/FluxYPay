package com.payment.PaymentGateway.PaymentIntegration.Validator;

import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;

import java.util.concurrent.Future;

public interface TransactionServiceValidator extends ServiceIntegrationValidator{
    /**
     * Verify before issuing a payment token
     * @param transactionRequest Request for the transaction
     */
    public Future<PaymentVerificationResult> verifyPaymentDuringTransaction(TransactionRequest transactionRequest);
}
