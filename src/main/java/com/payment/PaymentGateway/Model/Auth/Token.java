package com.payment.PaymentGateway.Model.Auth;

import com.payment.PaymentGateway.Model.Payment.PaymentRequest;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
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

}
