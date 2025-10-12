package com.payment.PaymentGateway.Model.Tables;

import com.payment.PaymentGateway.Model.Auth.Token;
import com.payment.PaymentGateway.Model.Payment.REQUEST_TYPE;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Builder
@Data
public class IdempotencyToken extends Token {
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
}
