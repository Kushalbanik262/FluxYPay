package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;

import java.util.List;


/**
 * Client Who is mainly responsible for any payment
 */
@Table(name = "client")
@Entity
public class Client {
    @Id
    private String clientId;
    @Column
    private String clientSecret;
    @Column
    private CLIENT_TYPE clientType;
    @Column
    private String  lastActiveTime;
    @Column
    private boolean isActive;

    @OneToMany(mappedBy = "client")
    private List<IdempotencyToken> idempotencyTokens;

    @OneToMany(mappedBy = "client")
    private List<PaymentRequest> paymentRequests;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public CLIENT_TYPE getClientType() {
        return clientType;
    }

    public void setClientType(CLIENT_TYPE clientType) {
        this.clientType = clientType;
    }

    public String getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(String lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<IdempotencyToken> getIdempotencyTokens() {
        return idempotencyTokens;
    }

    public void setIdempotencyTokens(List<IdempotencyToken> idempotencyTokens) {
        this.idempotencyTokens = idempotencyTokens;
    }

    public List<PaymentRequest> getPaymentRequests() {
        return paymentRequests;
    }

    public void setPaymentRequests(List<PaymentRequest> paymentRequests) {
        this.paymentRequests = paymentRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;
        return clientId.equals(client.clientId);
    }

    @Override
    public int hashCode() {
        return clientId.hashCode();
    }
}
