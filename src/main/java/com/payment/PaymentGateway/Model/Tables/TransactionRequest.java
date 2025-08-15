package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Auth.Token;
import com.payment.PaymentGateway.Model.Payment.TRANSACTION_STATUS;
import com.payment.PaymentGateway.Model.Request;
import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import jakarta.persistence.*;

/**
 * Request for all sort of transactions
 */
@Entity
@Table(name = "TransactionRequest")
public abstract class TransactionRequest extends Request {
    @Id
    @Column(name = "id")
    private String paymentRequestId;
    @Column
    private String dateTime;
    @Column(name = "amount")
    private int paymentAmount;
    @Column(name = "method")
    private PAYMENT_TYPE paymentMethod;

    @Column(name = "transactionStatus")
    private TRANSACTION_STATUS transactionStatus;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Token paymentToken;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "client")
    private Client client;

    public TransactionRequest() {
    }


}
