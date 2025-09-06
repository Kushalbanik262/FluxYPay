package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.TRANSACTION_STATUS;
import com.payment.PaymentGateway.Model.Request;
import jakarta.persistence.*;

@Table
@Entity
public class TransactionRequest implements Request {
    @Id
    private String id;
    @Column
    private String requestTime;
    @Column
    private TRANSACTION_STATUS transactionStatus;
    @ManyToOne
    private TransactionToken transactionToken;
    @ManyToOne
    private Transaction transaction;
    @Column
    private String webHook;
    @OneToOne
    private IdempotencyToken idempotencyToken;

    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public TRANSACTION_STATUS getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TRANSACTION_STATUS transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public TransactionToken getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(TransactionToken transactionToken) {
        this.transactionToken = transactionToken;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getWebHook() {
        return webHook;
    }

    public void setWebHook(String webHook) {
        this.webHook = webHook;
    }

    public IdempotencyToken getIdempotencyToken() {
        return idempotencyToken;
    }

    public void setIdempotencyToken(IdempotencyToken idempotencyToken) {
        this.idempotencyToken = idempotencyToken;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionRequest that = (TransactionRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
