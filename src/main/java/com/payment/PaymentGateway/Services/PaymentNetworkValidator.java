package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Tables.PaymentNetwork;

/**
 * Payment Network validator
 */
public interface PaymentNetworkValidator {
    /**
     * Validate the current PaymentNetwork
     */
    public void validatePaymentNetwork(PaymentNetwork paymentNetwork);
}
