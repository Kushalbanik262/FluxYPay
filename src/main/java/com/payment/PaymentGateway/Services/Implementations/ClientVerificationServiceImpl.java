package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Exceptions.ClientVerificationException;
import com.payment.PaymentGateway.Model.Tables.Client;
import com.payment.PaymentGateway.Repository.ClientRepository;
import com.payment.PaymentGateway.Services.ClientVerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientVerificationServiceImpl implements ClientVerificationService {

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Verify any Specific Client
     *
     * @param client
     */
    @Override
    public void verifyClient(Client client) {
        Client storedClient = clientRepository.getReferenceById(client.getClientId());
        if (!storedClient.getClientSecret().equals(client.getClientSecret())) {
            throw new ClientVerificationException("Client Id mismatch!");
        }
    }
}

    /*
     * TODO: Add verification for rate limiting on client Request
     */
