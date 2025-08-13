package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.PAYMENT_STATUS;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
public class Payment {
    @Id
    @Column(name="paymentId")
    private String paymentId;

    @Column(name = "paymentTime")
    private String dateTime;

    @Column(name = "paymentStatus")
    private PAYMENT_STATUS paymentStatus;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "payment")
    private Set<TransactionRequest> transactionRequests;

    public Payment() {
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public PAYMENT_STATUS getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PAYMENT_STATUS paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<TransactionRequest> getTransactionRequests() {
        return transactionRequests;
    }

    public void setTransactionRequests(Set<TransactionRequest> TransactionRequests) {
        this.transactionRequests = TransactionRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Payment payment = (Payment) o;
        return paymentId.equals(payment.paymentId);
    }

    @Override
    public int hashCode() {
        return paymentId.hashCode();
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", paymentStatus=" + paymentStatus +
                ", client=" + client +
                ", TransactionRequests=" + transactionRequests +
                '}';
    }
}
