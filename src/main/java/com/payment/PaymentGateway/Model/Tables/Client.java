package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;

import java.util.HashSet;
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
    private Set<Payment> payments;

    public Client(){}

    public Client(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        payments = new HashSet<>();
    }

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

    public Set<Payment> getPayments() {
        return payments;
    }

    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
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
