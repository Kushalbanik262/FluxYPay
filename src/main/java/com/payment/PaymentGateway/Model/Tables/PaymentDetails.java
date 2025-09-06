package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.CURRENCY;
import com.payment.PaymentGateway.Model.Payment.PAYMENT_STATUS;
import jakarta.persistence.*;

// Details for any payment System
@Entity
@Table
public abstract class PaymentDetails {
    @Id
    private String id;
    @OneToOne
    private Payment payment;
    @Column
    private int amount;
    @Column
    private CURRENCY currency;
    @ManyToOne
    private PaymentNetwork paymentNetwork;
    @Column
    private PAYMENT_STATUS paymentStatus;
    @Column
    private String paymentTime;
    @OneToOne
    private User paymentUser;
    @OneToOne
    private User paymentTouser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public CURRENCY getCurrency() {
        return currency;
    }

    public void setCurrency(CURRENCY currency) {
        this.currency = currency;
    }

    public PaymentNetwork getPaymentNetwork() {
        return paymentNetwork;
    }

    public void setPaymentNetwork(PaymentNetwork paymentNetwork) {
        this.paymentNetwork = paymentNetwork;
    }

    public PAYMENT_STATUS getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PAYMENT_STATUS paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public User getPaymentUser() {
        return paymentUser;
    }

    public void setPaymentUser(User paymentUser) {
        this.paymentUser = paymentUser;
    }

    public User getPaymentTouser() {
        return paymentTouser;
    }

    public void setPaymentTouser(User paymentTouser) {
        this.paymentTouser = paymentTouser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentDetails that = (PaymentDetails) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
