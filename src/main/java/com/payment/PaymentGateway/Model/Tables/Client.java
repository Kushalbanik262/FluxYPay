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
}
