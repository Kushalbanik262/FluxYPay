package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Tables.Client;
import com.payment.PaymentGateway.Model.Tables.IdempotencyToken;
import com.payment.PaymentGateway.Model.Auth.TokenVerificationStatus;
import com.payment.PaymentGateway.Model.Auth.Token_Type;
import com.payment.PaymentGateway.Model.Tables.TransactionToken;

/**
 *Token service to all token related operations
 */
public interface TokenService{
    /**
     * Build the token based on token type
     * @param tokenType Type of the token
     */
    public IdempotencyToken buildToken(Token_Type tokenType, Client client);

    /**
     * Verify the token
     * @param token TransactionToken we want to verify
     */
    public TokenVerificationStatus verifyIdempotencyToken(IdempotencyToken token);

    /**
     * Verify Transaction Token
     */
    public TokenVerificationStatus verifyTransactionToken(TransactionToken token);
}
