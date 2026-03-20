package com.payment.PaymentGateway.Model.Payment;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard Error Response DTO for API errors
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> validationErrors;
}
