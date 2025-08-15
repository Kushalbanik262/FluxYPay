package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Auth.AuthenticationResponse;
import com.payment.PaymentGateway.Model.Payment.PaymentDeclineRequest;
import com.payment.PaymentGateway.Model.Payment.PaymentResponse;
import com.payment.PaymentGateway.Model.Payment.PaymentRequest;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;

import java.util.concurrent.Future;

/**
 * Service Responsible for all Payment Related Operations
 */
public interface PaymentService {
    public Future<AuthenticationResponse> initiatePayment(TransactionRequest paymentRequest);
    public Future<PaymentResponse>  startPayment(PaymentRequest paymentRequest);
    public Future<PaymentResponse> declinePayment(PaymentDeclineRequest paymentDeclineRequest);
}
