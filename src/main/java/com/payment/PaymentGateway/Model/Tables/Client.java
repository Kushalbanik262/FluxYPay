package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Client Who is mainly responsible for any payment
 */
@Table(name = "client")
@Entity
public class Client {
    @Id
    @Column(name = "clientId")
    private String clientId;
    @Column(name = "clientSecret")
    private String clientSecret;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "client")
    private List<Payment> payments;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "client")
    private List<TransactionRequest> transactionRequests;

    public Client(){}

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

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<TransactionRequest> getTransactionRequests() {
        return transactionRequests;
    }

    public void setTransactionRequests(List<TransactionRequest> transactionRequests) {
        this.transactionRequests = transactionRequests;
    }

    @Override
    public String toString() {
        return "Client{" +
                "clientId='" + clientId + '\'' +
                ", clientSecret='" + clientSecret + '\'' +
                ", payments=" + payments +
                ", transactionRequests=" + transactionRequests +
                '}';
    }
}
