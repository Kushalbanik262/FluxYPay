package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Request;
import jakarta.persistence.*;

/**
 * Request for all sort of transactions
 */
@Entity
@Table(name = "PaymentRequest")
public abstract class PaymentRequest implements Request {
    @Id
    private String paymentRequestId;
    @OneToOne
    private PaymentDetails paymentDetails;
    @OneToOne
    private IdempotencyToken idempotencyToken;
    @OneToOne
    private TransactionToken transactionToken;
    @Column
    private String requestTimeStamp;
    @Column
    private String processingTimestamp;
    @ManyToOne
    private Client client;

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
    }

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public IdempotencyToken getIdempotencyToken() {
        return idempotencyToken;
    }

    public void setIdempotencyToken(IdempotencyToken idempotencyToken) {
        this.idempotencyToken = idempotencyToken;
    }

    public TransactionToken getTransactionToken() {
        return transactionToken;
    }

    public void setTransactionToken(TransactionToken transactionToken) {
        this.transactionToken = transactionToken;
    }

    public String getRequestTimeStamp() {
        return requestTimeStamp;
    }

    public void setRequestTimeStamp(String requestTimeStamp) {
        this.requestTimeStamp = requestTimeStamp;
    }

    public String getProcessingTimestamp() {
        return processingTimestamp;
    }

    public void setProcessingTimestamp(String processingTimestamp) {
        this.processingTimestamp = processingTimestamp;
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

        PaymentRequest that = (PaymentRequest) o;
        return paymentRequestId.equals(that.paymentRequestId);
    }

    @Override
    public int hashCode() {
        return paymentRequestId.hashCode();
    }
}
