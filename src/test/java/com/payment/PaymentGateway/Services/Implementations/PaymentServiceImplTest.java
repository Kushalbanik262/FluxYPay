package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Model.Payment.PaymentResponseDTO;
import com.payment.PaymentGateway.Model.Payment.RefundRequestDTO;
import com.payment.PaymentGateway.Model.Payment.TransactionStatusDTO;
import com.payment.PaymentGateway.Model.Tables.IdempotencyToken;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Repository.IdempotencyTokenRepository;
import com.payment.PaymentGateway.Services.Kafka.PaymentEventProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for PaymentService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PaymentService Tests")
class PaymentServiceImplTest {

    @Mock
    private IdempotencyTokenRepository idempotencyTokenRepository;

    @Mock
    private PaymentRequestVerificationServiceImpl paymentVerificationService;

    @Mock
    private PaymentEventProducer paymentEventProducer;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private TransactionRequest transactionRequest;
    private IdempotencyToken idempotencyToken;

    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionRequest();
        transactionRequest.setId("123456");

        idempotencyToken = new IdempotencyToken();
        idempotencyToken.setId("token_123");
        idempotencyToken.setProcessed(false);
    }

    @Test
    @DisplayName("Should process payment successfully")
    void testProcessPaymentSuccess() {
        // Arrange
        String idempotencyKey = "idempotency_key_001";
        when(idempotencyTokenRepository.save(any(IdempotencyToken.class)))
                .thenReturn(idempotencyToken);

        // Act
        PaymentResponseDTO response = paymentService.processPayment(transactionRequest, idempotencyKey);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        assertNotNull(response.getReferenceId());
        assertNotNull(response.getCreatedAt());
        assertEquals("USD", response.getCurrency());
        assertTrue(response.getAmount().compareTo(BigDecimal.ZERO) >= 0);
        assertNotNull(response.getMessage());
    }

    @Test
    @DisplayName("Should get transaction status successfully")
    void testGetTransactionStatus() {
        // Arrange
        String transactionId = "txn_12345";

        // Act
        TransactionStatusDTO status = paymentService.getTransactionStatus(transactionId);

        // Assert
        assertNotNull(status);
        assertEquals(transactionId, status.getTransactionId());
        assertNotNull(status.getStatus());
        assertNotNull(status.getCreatedAt());
        assertNotNull(status.getUpdatedAt());
        assertTrue(status.getRetryCount() >= 0);
    }

    @Test
    @DisplayName("Should process refund successfully")
    void testRefundPaymentSuccess() {
        // Arrange
        String transactionId = "txn_12345";
        String idempotencyKey = "refund_idempotency_001";
        RefundRequestDTO refundRequest = new RefundRequestDTO();
        refundRequest.setAmount(new BigDecimal("99.99"));
        refundRequest.setReason("Customer request");

        when(idempotencyTokenRepository.save(any(IdempotencyToken.class)))
                .thenReturn(idempotencyToken);

        // Act
        PaymentResponseDTO response = paymentService.refundPayment(
                transactionId, refundRequest, idempotencyKey);

        // Assert
        assertNotNull(response);
        assertEquals(transactionId, response.getTransactionId());
        assertEquals("REFUNDED", response.getStatus());
        assertEquals(new BigDecimal("99.99"), response.getAmount());
        assertNotNull(response.getMessage());
    }

    @Test
    @DisplayName("Should handle duplicate payment request")
    void testProcessPaymentDuplicate() {
        // Arrange
        String idempotencyKey = "idempotency_key_duplicate";
        idempotencyToken.setProcessed(true);

        // Act & Assert - should not throw but handle gracefully
        assertDoesNotThrow(() -> {
            paymentService.processPayment(transactionRequest, idempotencyKey);
        });
    }

    @Test
    @DisplayName("Should return valid transaction ID format")
    void testTransactionIdFormat() {
        // Arrange
        String idempotencyKey = "idempotency_key_format";

        // Act
        PaymentResponseDTO response = paymentService.processPayment(transactionRequest, idempotencyKey);

        // Assert
        assertNotNull(response.getTransactionId());
        assertTrue(response.getTransactionId().length() > 0);
        // UUID format check
        assertTrue(response.getTransactionId().matches(
                "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"
        ));
    }
}
