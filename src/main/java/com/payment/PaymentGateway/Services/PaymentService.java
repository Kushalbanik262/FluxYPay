package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Payment.PaymentResponseDTO;
import com.payment.PaymentGateway.Model.Payment.RefundRequestDTO;
import com.payment.PaymentGateway.Model.Payment.TransactionStatusDTO;
import com.payment.PaymentGateway.Model.Tables.TransactionRequest;

/**
 * Service Responsible for all Payment Related Operations
 */
public interface PaymentService {
    
    /**
     * Process a new payment transaction
     * 
     * @param transactionRequest Payment request details
     * @param idempotencyKey Unique key for idempotency
     * @return Payment response
     */
    PaymentResponseDTO processPayment(TransactionRequest transactionRequest, String idempotencyKey);
    
    /**
     * Get transaction status
     * 
     * @param transactionId Transaction identifier
     * @return Transaction status details
     */
    TransactionStatusDTO getTransactionStatus(String transactionId);
    
    /**
     * Refund an existing transaction
     * 
     * @param transactionId Transaction to refund
     * @param refundRequest Refund details
     * @param idempotencyKey Unique key for idempotency
     * @return Refund response
     */
    PaymentResponseDTO refundPayment(String transactionId, RefundRequestDTO refundRequest, String idempotencyKey);
}
