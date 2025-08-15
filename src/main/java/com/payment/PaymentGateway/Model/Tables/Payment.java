package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.PAYMENT_STATUS;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table
public class Payment {
    @Id
    @Column(name="paymentId")
    private String paymentId;

    @Column(name = "paymentTime")
    private String dateTime;

    @Column(name = "paymentStatus")
    private PAYMENT_STATUS paymentStatus;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "client")
    private Client client;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "payment")
    private Set<PaymentRequest> transactionRequests;

    public Payment() {
    }

}
