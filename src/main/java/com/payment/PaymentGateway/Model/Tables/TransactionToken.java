package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;

import java.util.List;


@Table(name = "TransactionToken")
@Entity
public class TransactionToken {
    @Id
    private String id;
    @Column
    private String issued;
    @Column
    private String expires;
    @Column
    private String notBefore;
    @Column
    private String tokenHash;
    @OneToOne
    private PaymentRequest paymentRequest;
    @OneToMany(mappedBy = "transactionToken")
    private List<TransactionRequest> transactionRequests;
    @Column
    private boolean isUsed;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getNotBefore() {
        return notBefore;
    }

    public void setNotBefore(String notBefore) {
        this.notBefore = notBefore;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    public PaymentRequest getPaymentRequest() {
        return paymentRequest;
    }

    public void setPaymentRequest(PaymentRequest paymentRequest) {
        this.paymentRequest = paymentRequest;
    }

    public List<TransactionRequest> getTransactionRequests() {
        return transactionRequests;
    }

    public void setTransactionRequests(List<TransactionRequest> transactionRequests) {
        this.transactionRequests = transactionRequests;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionToken that = (TransactionToken) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
