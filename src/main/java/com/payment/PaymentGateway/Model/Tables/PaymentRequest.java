package com.payment.PaymentGateway.Model.Tables;


import com.payment.PaymentGateway.Model.Request;
import com.payment.PaymentGateway.PaymentIntegration.PaymentNetwork;
import jakarta.persistence.*;

@Entity
public class PaymentRequest extends Request {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paymentToken")
    private Token paymentToken;

    @ManyToOne
    @JoinColumn(name = "payment")
    private Payment payment;

    @ManyToOne
    @JoinColumn(name = "paymentNetwork")
    private PaymentNetwork paymentNetwork;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Token getToken() {
        return paymentToken;
    }

    public void setToken(Token token) {
        this.paymentToken = token;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentNetwork getPaymentNetwork() {
        return paymentNetwork;
    }

    public void setPaymentNetwork(PaymentNetwork paymentNetwork) {
        this.paymentNetwork = paymentNetwork;
    }

    @Override
    public String toString() {
        return "PaymentRequest{" +
                "id='" + id + '\'' +
                ", token=" + paymentToken +
                ", payment=" + payment +
                ", paymentNetwork=" + paymentNetwork +
                '}';
    }
}
