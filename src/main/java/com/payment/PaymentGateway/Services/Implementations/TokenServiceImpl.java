package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Model.Auth.TokenVerificationStatus;
import com.payment.PaymentGateway.Model.Auth.Token_Type;
import com.payment.PaymentGateway.Model.Tables.Client;
import com.payment.PaymentGateway.Model.Tables.IdempotencyToken;
import com.payment.PaymentGateway.Model.Tables.TransactionToken;
import com.payment.PaymentGateway.Repository.ClientRepository;
import com.payment.PaymentGateway.Repository.IdempotencyTokenRepository;
import com.payment.PaymentGateway.Services.ClientVerificationService;
import com.payment.PaymentGateway.Services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.UUID;

public class TokenServiceImpl implements TokenService {

    @Autowired
    private ClientVerificationService clientVerificationService;

    @Autowired
    private IdempotencyTokenRepository idempotencyTokenRepository;

    @Autowired
    private ClientRepository clientRepository;

    /**
     * Build the token based on token type
     *
     * @param tokenType Type of the token
     * @param client
     */
    @Override
    public IdempotencyToken buildToken(Token_Type tokenType, Client client) {
        clientVerificationService.verifyClient(client);

        IdempotencyToken idempotencyToken = IdempotencyToken.builder()
                .id(UUID.randomUUID().toString())
                .markedAsInvalid(false)
                .expiredAt(LocalDateTime.now().plusMinutes(10)) // make it configurable
                .processed(false)
                .generationTime(LocalDateTime.now())
                .markedAsMalicious(false)
                .client(client)
                .build();

        idempotencyTokenRepository.save(idempotencyToken);
        return idempotencyToken;
    }

    /**
     * Verify the token
     *
     * @param token TransactionToken we want to verify
     */
    @Override
    public TokenVerificationStatus verifyIdempotencyToken(IdempotencyToken token) {
        return null;
    }

    /**
     * Verify Transaction Token
     *
     * @param token
     */
    @Override
    public TokenVerificationStatus verifyTransactionToken(TransactionToken token) {
        return null;
    }
}
