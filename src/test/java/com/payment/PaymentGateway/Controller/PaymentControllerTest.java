package com.payment.PaymentGateway.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.PaymentGateway.Model.Payment.PaymentResponseDTO;
import com.payment.PaymentGateway.Model.Payment.RefundRequestDTO;
import com.payment.PaymentGateway.Model.Payment.TransactionStatusDTO;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;
import com.payment.PaymentGateway.Services.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for PaymentController
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("PaymentController Tests")
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PaymentService paymentService;

    private PaymentResponseDTO mockPaymentResponse;
    private TransactionStatusDTO mockTransactionStatus;
    private TransactionRequest transactionRequest;

    @BeforeEach
    void setUp() {
        mockPaymentResponse = PaymentResponseDTO.builder()
                .transactionId("txn_12345")
                .status("COMPLETED")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .completedAt(LocalDateTime.now())
                .paymentMethod("CARD")
                .lastCardDigits("1111")
                .referenceId("ref_123")
                .message("Payment processed successfully")
                .build();

        mockTransactionStatus = TransactionStatusDTO.builder()
                .transactionId("txn_12345")
                .status("COMPLETED")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .retryCount(0)
                .build();

        transactionRequest = new TransactionRequest();
        transactionRequest.setId("123456");
    }

    @Test
    @DisplayName("Should initiate payment successfully")
    void testInitiatePaymentSuccess() throws Exception {
        // Arrange
        when(paymentService.processPayment(any(TransactionRequest.class), anyString()))
                .thenReturn(mockPaymentResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/initiate")
                .header("Authorization", "Bearer mock_token")
                .header("Idempotency-Key", "payment_key_123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transactionId").value("txn_12345"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.amount").value(99.99))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    @DisplayName("Should get transaction status successfully")
    void testGetTransactionStatusSuccess() throws Exception {
        // Arrange
        when(paymentService.getTransactionStatus(anyString()))
                .thenReturn(mockTransactionStatus);

        // Act & Assert
        mockMvc.perform(get("/api/v1/payments/txn_12345")
                .header("Authorization", "Bearer mock_token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").value("txn_12345"))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.retryCount").value(0));
    }

    @Test
    @DisplayName("Should refund payment successfully")
    void testRefundPaymentSuccess() throws Exception {
        // Arrange
        RefundRequestDTO refundRequest = new RefundRequestDTO();
        refundRequest.setAmount(new BigDecimal("99.99"));
        refundRequest.setReason("Customer request");

        PaymentResponseDTO refundResponse = PaymentResponseDTO.builder()
                .transactionId("txn_12345")
                .status("REFUNDED")
                .amount(new BigDecimal("99.99"))
                .message("Refund processed successfully")
                .build();

        when(paymentService.refundPayment(anyString(), any(RefundRequestDTO.class), anyString()))
                .thenReturn(refundResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/txn_12345/refund")
                .header("Authorization", "Bearer mock_token")
                .header("Idempotency-Key", "refund_key_123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refundRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.transactionId").value("txn_12345"))
                .andExpect(jsonPath("$.status").value("REFUNDED"));
    }

    @Test
    @DisplayName("Should get payment health status")
    void testPaymentHealthCheck() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/v1/payments/health")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Payment Service"));
    }

    @Test
    @DisplayName("Should fail payment initiation without Idempotency-Key")
    void testInitiatePaymentMissingIdempotencyKey() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/initiate")
                .header("Authorization", "Bearer mock_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should fail payment initiation without Authorization header")
    void testInitiatePaymentMissingAuthHeader() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/initiate")
                .header("Idempotency-Key", "payment_key_123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isUnauthorized());
    }
}
