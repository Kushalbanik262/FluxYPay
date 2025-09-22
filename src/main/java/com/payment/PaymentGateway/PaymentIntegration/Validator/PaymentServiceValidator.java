package com.payment.PaymentGateway.PaymentIntegration.Validator;

import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.PaymentRequest;

import java.util.concurrent.Future;

public interface PaymentServiceValidator extends ServiceIntegrationValidator{

    /**
     * verify During payment
     * @param paymentRequest verify During Actual payment
     */
    public Future<PaymentVerificationResult> verifyPaymentDuringPayment(PaymentRequest paymentRequest);
}
