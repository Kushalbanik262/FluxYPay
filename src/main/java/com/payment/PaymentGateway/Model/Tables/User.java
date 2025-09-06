package com.payment.PaymentGateway.Model.Tables;

import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String id;
    @Column
    private String name;
    @Column
    private String mail;
    @Column
    private String dob;
    @OneToMany(mappedBy = "user")
    private List<Payment> payments;

    @OneToMany(mappedBy = "sender")
    private List<TransactionRequest> transactionRequestsAsSender;

    @OneToMany(mappedBy = "receiver")
    private List<TransactionRequest> transactionRequestsAsReceiver;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<TransactionRequest> getTransactionRequestsAsSender() {
        return transactionRequestsAsSender;
    }

    public void setTransactionRequestsAsSender(List<TransactionRequest> transactionRequestsAsSender) {
        this.transactionRequestsAsSender = transactionRequestsAsSender;
    }

    public List<TransactionRequest> getTransactionRequestsAsReceiver() {
        return transactionRequestsAsReceiver;
    }

    public void setTransactionRequestsAsReceiver(List<TransactionRequest> transactionRequestsAsReceiver) {
        this.transactionRequestsAsReceiver = transactionRequestsAsReceiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
