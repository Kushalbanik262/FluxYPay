package com.payment.PaymentGateway.PaymentIntegration;

import com.payment.PaymentGateway.Model.Payment.CURRENCY;
import com.payment.PaymentGateway.Model.Tables.PaymentRequest;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class PaymentNetwork {
    @Id
    private String id;

    @OneToMany(mappedBy = "paymentNetwork")
    private List<PaymentRequest> paymentRequest;

    @Column
    private PAYMENT_TYPE paymentType;

    @Column(name = "networkName")
    private String name;

    @Column(name = "currencyType")
    private CURRENCY currency;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PaymentRequest> getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(List<PaymentRequest> paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public PAYMENT_TYPE getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PAYMENT_TYPE paymentType) {
        this.paymentType = paymentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CURRENCY getCurrency() {
        return currency;
    }

    public void setCurrency(CURRENCY currency) {
        this.currency = currency;
    }

    @Override
    public String toString() {
        return "PaymentNetwork{" +
                "id='" + id + '\'' +
                ", paymentRequest=" + paymentRequest +
                ", paymentType=" + paymentType +
                ", name='" + name + '\'' +
                ", currency=" + currency +
                '}';
    }
}
