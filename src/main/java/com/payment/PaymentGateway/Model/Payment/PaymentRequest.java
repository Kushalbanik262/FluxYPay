package com.payment.PaymentGateway.Model.Payment;


import com.payment.PaymentGateway.Model.Auth.Token;
import com.payment.PaymentGateway.Model.Request;
import com.payment.PaymentGateway.Model.Tables.Payment;
import com.payment.PaymentGateway.PaymentIntegration.PaymentNetwork;
import jakarta.persistence.*;

import java.util.List;

public class PaymentRequest extends Request {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentToken")
    private Token requestToken;

    @ManyToOne
    @JoinColumn(name = "payment")
    private Payment payment;


    private PaymentNetwork paymentNetwork;
}
