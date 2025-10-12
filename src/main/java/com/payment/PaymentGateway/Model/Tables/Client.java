package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


/**
 * Client Who is mainly responsible for any payment
 */
@Table(name = "client")
@Entity
@Data
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
