package com.payment.PaymentGateway.PaymentIntegration;

import com.payment.PaymentGateway.Model.PaymentMethod;

public class VisaCard extends PaymentMethod {

    @Override
    protected PaymentNetwork getPaymentNetwork() {
        return buildVisaPaymentNetwork();
    }

    private VisaNetwork buildVisaPaymentNetwork(){
        // return Visa Network
        return new VisaNetwork();
    }


}
