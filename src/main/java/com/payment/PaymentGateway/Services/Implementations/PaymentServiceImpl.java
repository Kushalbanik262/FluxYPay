package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Model.Auth.AuthenticationResponse;
import com.payment.PaymentGateway.Model.Payment.PaymentDeclineRequest;
import com.payment.PaymentGateway.Model.Tables.PaymentRequest;
import com.payment.PaymentGateway.Model.Payment.PaymentResponse;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Services.PaymentService;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Handles Payments requested by client
 */
@Service
public class PaymentServiceImpl implements PaymentService {



    @Override
    public Future<AuthenticationResponse> initiatePayment(TransactionRequest paymentRequest) {
        return null;
    }

    @Override
    public Future<PaymentResponse> startPayment(PaymentRequest paymentRequest) {
        return null;
    }

    @Override
    public Future<PaymentResponse> declinePayment(PaymentDeclineRequest paymentDeclineRequest) {
        return null;
    }
}
