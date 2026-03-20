package com.payment.PaymentGateway.Model.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * Transaction Status DTO
 * Contains detailed status information of a transaction
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionStatusDTO {
    private String transactionId;
    private String status;
    private String paymentMethod;
    private double amount;
    private String currency;
    private String clientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String statusMessage;
    private String narrativeDescription;
    private int retryCount;

    // Convenience getter for backward compatibility
    public int getRetryCount() { return this.retryCount; }
}
