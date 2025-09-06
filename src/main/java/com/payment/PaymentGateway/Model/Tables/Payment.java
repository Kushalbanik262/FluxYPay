package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.PAYMENT_STATUS;
import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import jakarta.persistence.*;

@Entity
public class Payment {
    @Id
    private String id;
    @Column
    private String dateTime;
    @Enumerated(EnumType.STRING)
    private PAYMENT_TYPE paymentType;
    @OneToOne
    private PaymentDetails paymentDetails;
    @ManyToOne
    private User user;
    @Enumerated(EnumType.STRING)
    private PAYMENT_STATUS paymentStatus;
    @OneToOne
    private Transaction transaction;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return dateTime;
    }

    public void setTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public PAYMENT_TYPE getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PAYMENT_TYPE paymentType) {
        this.paymentType = paymentType;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PAYMENT_STATUS getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PAYMENT_STATUS paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;
        return id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
