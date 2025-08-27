package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.TRANSACTION_STATUS;
import com.payment.PaymentGateway.Model.Request;
import com.payment.PaymentGateway.PaymentIntegration.PAYMENT_TYPE;
import jakarta.persistence.*;

/** TODO : Change all the TransactionRequest to paymentRequest and vise versa logically
 * Request for all sort of transactions
 */
@Entity
@Table(name = "TransactionRequest")
public abstract class TransactionRequest extends Request {
    @Id
    @Column(name = "id")
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(int paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PAYMENT_TYPE getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PAYMENT_TYPE paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public TRANSACTION_STATUS getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TRANSACTION_STATUS transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Token getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(Token paymentToken) {
        this.paymentToken = paymentToken;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransactionRequest that = (TransactionRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
