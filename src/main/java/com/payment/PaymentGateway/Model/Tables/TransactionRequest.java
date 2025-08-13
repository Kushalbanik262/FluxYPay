package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Auth.Token;
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

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private PaymentDetails paymentDetails;

    @ManyToOne
    @JoinColumn(name = "paymentId")
    private Payment payment;


    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Token paymentToken;

    public TransactionRequest() {
    }

    public TransactionRequest(String paymentRequestId, Token paymentToken, Payment payment, PAYMENT_TYPE paymentMethod, PaymentDetails paymentDetails, int paymentAmount, String dateTime) {
        this.paymentRequestId = paymentRequestId;
        this.paymentToken = paymentToken;
        this.payment = payment;
        this.paymentMethod = paymentMethod;
        this.paymentDetails = paymentDetails;
        this.paymentAmount = paymentAmount;
        this.dateTime = dateTime;
    }

    public String getPaymentRequestId() {
        return paymentRequestId;
    }

    public void setPaymentRequestId(String paymentRequestId) {
        this.paymentRequestId = paymentRequestId;
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

    public PaymentDetails getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(PaymentDetails paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Token getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(Token paymentToken) {
        this.paymentToken = paymentToken;
    }
}
