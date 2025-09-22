package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Tables.Client;

/**
 * Service to Verify Clients
 */
public interface ClientVerificationService {
    /**
     * Verify any Specific Client
     */
    public void verifyClient(Client client);
}
