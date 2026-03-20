package com.payment.PaymentGateway.Model.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * Refund Request DTO
 * Used for initiating refund operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDTO {
    private String transactionId;
    private double refundAmount;
    private String reason;
    private String remarks;
    private String bankAccountNumber;

    // Convenience methods for backward compatibility
    public void setAmount(BigDecimal amount) { this.refundAmount = amount != null ? amount.doubleValue() : 0; }
    public BigDecimal getAmount() { return BigDecimal.valueOf(this.refundAmount); }
}
    private String bankAccountNumber;

    public RefundRequestDTO(String transactionId, double refundAmount) {
        this.transactionId = transactionId;
        this.refundAmount = refundAmount;
    }
}
