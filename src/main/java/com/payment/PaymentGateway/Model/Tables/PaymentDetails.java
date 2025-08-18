package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Details for any payment System
@Entity
@Table
public abstract class PaymentDetails {
    @Id
    protected String id;
    @Column(name = "paymentType")
    protected PAYMENT_TYPE paymentType;

    public PaymentDetails(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PAYMENT_TYPE getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PAYMENT_TYPE paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return "PaymentDetails{" +
                "id='" + id + '\'' +
                ", paymentType=" + paymentType +
                '}';
    }
}
