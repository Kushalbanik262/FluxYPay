package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.TRANSACTION_STATUS;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table
public class Transaction {
    @Id
    private String id;
    @OneToMany(mappedBy = "transaction")
    private List<TransactionRequest> transactionRequests;
    @Column
    private String transactionTime;
    @Column
    private TRANSACTION_STATUS transactionStatus;
    @OneToOne
    private Payment payment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TransactionRequest> getTransactionRequests() {
        return transactionRequests;
    }

    public void setTransactionRequests(List<TransactionRequest> transactionRequests) {
        this.transactionRequests = transactionRequests;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public TRANSACTION_STATUS getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TRANSACTION_STATUS transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
