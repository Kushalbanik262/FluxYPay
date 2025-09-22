package com.payment.PaymentGateway.PaymentIntegration.Validator;

import com.payment.PaymentGateway.Model.Tables.PaymentRequest;
import com.payment.PaymentGateway.Model.Payment.PaymentVerificationResult;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Validate Payment Request against a third party Network
 */
@Service
public class PaymentNetworkValidator implements PaymentServiceValidator{

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
