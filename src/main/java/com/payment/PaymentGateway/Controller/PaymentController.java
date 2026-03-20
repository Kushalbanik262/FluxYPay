package com.payment.PaymentGateway.Controller;

import com.payment.PaymentGateway.Model.Payment.PaymentResponseDTO;
import com.payment.PaymentGateway.Model.Payment.RefundRequestDTO;
import com.payment.PaymentGateway.Model.Payment.TransactionStatusDTO;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Payment Operations
 * Handles payment initiation, status checks, and refunds
 */
@Slf4j
@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Controller", description = "Payment processing and management endpoints")
@SecurityRequirement(name = "JWT")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    /**
     * Initiate a new payment transaction
     * 
     * @param paymentRequest Payment request details
     * @param idempotencyKey Unique idempotency key for retry-safety
     * @return Payment response with transaction ID and status
     */
    @PostMapping("/initiate")
    @Operation(summary = "Initiate Payment", description = "Start a new payment transaction with idempotency support")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(
            @RequestBody TransactionRequest paymentRequest,
            @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey) {
        
        log.info("Initiating payment transaction with Idempotency-Key: {}", idempotencyKey);
        
        PaymentResponseDTO response = paymentService.processPayment(paymentRequest, idempotencyKey);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get transaction status
     * 
     * @param transactionId The transaction ID to check
     * @return Current transaction status and details
     */
    @GetMapping("/{transactionId}")
    @Operation(summary = "Get Transaction Status", description = "Retrieve the current status of a transaction")
    public ResponseEntity<TransactionStatusDTO> getTransactionStatus(
            @PathVariable String transactionId) {
        
        log.info("Fetching transaction status for ID: {}", transactionId);
        
        TransactionStatusDTO status = paymentService.getTransactionStatus(transactionId);
        
        return ResponseEntity.ok(status);
    }

    /**
     * Refund a completed payment
     * 
     * @param transactionId The transaction ID to refund
     * @param refundRequest Refund details and amount
     * @param idempotencyKey Unique idempotency key
     * @return Refund response
     */
    @PostMapping("/{transactionId}/refund")
    @Operation(summary = "Refund Payment", description = "Initiate a refund for a completed payment")
    public ResponseEntity<PaymentResponseDTO> refundPayment(
            @PathVariable String transactionId,
            @RequestBody RefundRequestDTO refundRequest,
            @RequestHeader(value = "Idempotency-Key", required = true) String idempotencyKey) {
        
        log.info("Processing refund for transaction: {} with Idempotency-Key: {}", 
                 transactionId, idempotencyKey);
        
        PaymentResponseDTO response = paymentService.refundPayment(
                transactionId, refundRequest, idempotencyKey);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    /**
     * Health check endpoint
     * 
     * @return Service status
     */
    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check if payment service is running")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Payment Service",
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}
