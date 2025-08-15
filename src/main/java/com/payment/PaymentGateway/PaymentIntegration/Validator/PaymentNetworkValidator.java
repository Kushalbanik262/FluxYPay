package com.payment.PaymentGateway.PaymentIntegration.Validator;

import com.payment.PaymentGateway.Model.Tables.PaymentRequest;
import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;

import java.util.concurrent.Future;

/**
 * Validate Payment Request against a third party Network
 */
public class PaymentNetworkValidator implements Validator{



    /**
     * Verify before issuing a payment token
     *
     * @param transactionRequest Request for the transaction
     */
    @Override
    public Future<PaymentVerificationResult> verifyPaymentDuringTransaction(TransactionRequest transactionRequest) {
        return null;
    }

    /**
     * verify During payment
     *
     * @param paymentRequest verify During Actual payment
     */
    @Override
    public Future<PaymentVerificationResult> verifyPaymentDuringPayment(PaymentRequest paymentRequest) {
        return null;
    }
}
