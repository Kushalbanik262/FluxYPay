package com.payment.PaymentGateway.PaymentIntegration;

import com.payment.PaymentGateway.Model.Payment.PaymentMethod;
import com.payment.PaymentGateway.Model.Tables.PaymentNetwork;

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
