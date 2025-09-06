package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Payment.REQUEST_TYPE;
import com.payment.PaymentGateway.Model.Request;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Table
@Entity
public class IdempotencyToken {
    @Id
    private String id;
    @Column
    private LocalDateTime generationTime;
    @Column
    private LocalDateTime expiredAt;
    @Column
    private boolean processed;
    @Column
    private LocalDateTime lastProcessingTime;
    @Column
    private boolean markedAsInvalid;
    @Column
    private boolean markedAsMalicious;
    @Column
    private REQUEST_TYPE requestType;

    @ManyToOne(cascade = CascadeType.ALL)
    private Client client;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IdempotencyToken that = (IdempotencyToken) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getGenerationTime() {
        return generationTime;
    }

    public void setGenerationTime(LocalDateTime generationTime) {
        this.generationTime = generationTime;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public LocalDateTime getLastProcessingTime() {
        return lastProcessingTime;
    }

    public void setLastProcessingTime(LocalDateTime lastProcessingTime) {
        this.lastProcessingTime = lastProcessingTime;
    }

    public boolean isMarkedAsInvalid() {
        return markedAsInvalid;
    }

    public void setMarkedAsInvalid(boolean markedAsInvalid) {
        this.markedAsInvalid = markedAsInvalid;
    }

    public boolean isMarkedAsMalicious() {
        return markedAsMalicious;
    }

    public void setMarkedAsMalicious(boolean markedAsMalicious) {
        this.markedAsMalicious = markedAsMalicious;
    }

    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    public void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
