package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;

import java.util.List;


@Table(name = "Token")
@Entity
public class Token{
    @Id
    public String tokenId;
    @Column
    public String tokenHash;
    @Column
    public String dateTime;
    @Column
    public boolean isActive;

    @OneToOne(fetch = FetchType.EAGER)
    public TransactionRequest transactionRequest;

    @OneToMany(mappedBy = "paymentToken")
    public List<PaymentRequest> paymentRequest;

    public Token() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    public List<PaymentRequest> getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(List<PaymentRequest> paymentRequest) {
        this.paymentRequest = paymentRequest;
    }
}
