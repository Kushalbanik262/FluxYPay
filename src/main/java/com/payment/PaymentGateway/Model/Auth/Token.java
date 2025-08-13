package com.payment.PaymentGateway.Model.Auth;

import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import jakarta.persistence.*;


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

    public Token() {
    }

    public Token(String tokenId, String tokenHash, String dateTime, boolean isActive,TransactionRequest transactionRequest) {
        this.tokenId = tokenId;
        tokenHash = tokenHash;
        this.dateTime = dateTime;
        this.isActive = isActive;
        this.transactionRequest = transactionRequest;
    }

    public TransactionRequest getTransactionRequest() {
        return transactionRequest;
    }

    public void setTransactionRequest(TransactionRequest transactionRequest) {
        this.transactionRequest = transactionRequest;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String gettokenHash() {
        return tokenHash;
    }

    public void settokenHash(String tokenHash) {
        tokenHash = tokenHash;
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


}
