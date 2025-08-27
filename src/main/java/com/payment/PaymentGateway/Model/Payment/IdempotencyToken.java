package com.payment.PaymentGateway.Model.Payment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

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

    public IdempotencyToken() {
    }

    public IdempotencyToken(String id, LocalDateTime generationTime, LocalDateTime expiredAt) {
        this.id = id;
        this.generationTime = generationTime;
        this.expiredAt = expiredAt;
    }

    public REQUEST_TYPE getRequestType() {
        return requestType;
    }

    public void setRequestType(REQUEST_TYPE requestType) {
        this.requestType = requestType;
    }

    public LocalDateTime getLastProcessingTime() {
        return lastProcessingTime;
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

    public LocalDateTime isLastProcessingTime() {
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
}
