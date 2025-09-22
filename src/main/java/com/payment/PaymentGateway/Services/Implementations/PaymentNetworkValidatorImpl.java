package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Exceptions.PaymentNetworkValidationException;
import com.payment.PaymentGateway.Model.Tables.PaymentNetwork;
import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import com.payment.PaymentGateway.Repository.PaymentNetworkRepository;
import com.payment.PaymentGateway.Services.PaymentNetworkValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentNetworkValidatorImpl implements PaymentNetworkValidator {

    @Autowired
    private PaymentNetworkRepository paymentNetworkRepository;
    /**
     * Validate the current PaymentNetwork
     *
     * @param paymentNetwork
     */
    @Override
    public void validatePaymentNetwork(PaymentNetwork paymentNetwork) {
        PaymentNetwork existingPaymentNetwork = paymentNetworkRepository.getReferenceById(paymentNetwork.getId());
        if(existingPaymentNetwork == null){
            throw new PaymentNetworkValidationException("Payment Network not registered");
        }
        if(!paymentNetwork.isActive()){
            throw new PaymentNetworkValidationException("Payment network not active please try with another one");
        }
    }
}
