package com.payment.PaymentGateway.Model.Payment;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard API Response wrapper for all payment operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private String transactionId;
    private String status;
    private BigDecimal amount;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String paymentMethod;
    private String lastCardDigits;
    private String referenceId;
    private Map<String, Object> metadata;
    private String message;
    private Integer retryCount;
}
