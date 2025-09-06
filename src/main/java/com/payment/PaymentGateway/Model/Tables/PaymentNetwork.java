package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.CURRENCY;
import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class PaymentNetwork {
    @Id
    protected String id;
    @Column
    protected PAYMENT_TYPE paymentType;
    @Column
    protected boolean isActive;
    @OneToMany(mappedBy = "paymentNetwork")
    protected List<PaymentDetails> paymentDetails;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<PaymentDetails> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PaymentDetails> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentNetwork that = (PaymentNetwork) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
