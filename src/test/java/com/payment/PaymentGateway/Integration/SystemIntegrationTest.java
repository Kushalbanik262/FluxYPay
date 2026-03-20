package com.payment.PaymentGateway.Integration;

import com.payment.PaymentGateway.Model.Payment.PaymentResponseDTO;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Tests for FluxyPay System
 * Tests end-to-end functionality with real Spring context
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("System Integration Tests")
class SystemIntegrationTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired(required = false)
    private CacheManager cacheManager;

    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        transactionRequest = new TransactionRequest();
        transactionRequest.setId("test_transaction_123");
    }

    @Test
    @DisplayName("Should process payment end-to-end")
    void testEndToEndPaymentProcessing() {
        // Arrange
        String idempotencyKey = "integration_test_key_001";

        // Act
        PaymentResponseDTO response = paymentService.processPayment(transactionRequest, idempotencyKey);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getTransactionId());
        assertEquals("USD", response.getCurrency());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getMessage());
        assertTrue(response.getMessage().length() > 0);
    }

    @Test
    @DisplayName("Should retrieve transaction status")
    void testGetTransactionStatus() {
        // Act
        var status = paymentService.getTransactionStatus("test_txn_123");

        // Assert
        assertNotNull(status);
        assertNotNull(status.getStatus());
        assertNotNull(status.getCreatedAt());
        assertTrue(status.getRetryCount() >= 0);
    }

    @Test
    @DisplayName("Should handle multiple sequential payments")
    void testMultipleSequentialPayments() {
        // Act
        PaymentResponseDTO response1 = paymentService.processPayment(transactionRequest, "key_1");
        PaymentResponseDTO response2 = paymentService.processPayment(transactionRequest, "key_2");
        PaymentResponseDTO response3 = paymentService.processPayment(transactionRequest, "key_3");

        // Assert
        assertNotNull(response1.getTransactionId());
        assertNotNull(response2.getTransactionId());
        assertNotNull(response3.getTransactionId());

        // Transaction IDs should be unique
        assertNotEquals(response1.getTransactionId(), response2.getTransactionId());
        assertNotEquals(response2.getTransactionId(), response3.getTransactionId());
    }

    @Test
    @DisplayName("Should verify cache is configured")
    void testCacheConfiguration() {
        // Assert
        if (cacheManager != null) {
            assertNotNull(cacheManager.getCacheNames());
            assertTrue(cacheManager.getCacheNames().size() > 0);
        }
    }

    @Test
    @DisplayName("Should verify transaction status caching")
    void testTransactionStatusCaching() {
        // Arrange
        String transactionId = "cache_test_123";

        // Act - Call twice to verify caching
        var status1 = paymentService.getTransactionStatus(transactionId);
        var status2 = paymentService.getTransactionStatus(transactionId);

        // Assert
        assertNotNull(status1);
        assertNotNull(status2);
        assertEquals(status1.getTransactionId(), status2.getTransactionId());
    }
}
