package com.payment.PaymentGateway.Model.Payment;

import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import com.payment.PaymentGateway.PaymentIntegration.PaymentNetwork;

public abstract class PaymentMethod {
    protected String id;
    protected PAYMENT_TYPE paymentType;
    protected boolean active;

    protected abstract PaymentNetwork getPaymentNetwork();
}
