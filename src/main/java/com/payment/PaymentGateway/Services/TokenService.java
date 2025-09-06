package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Tables.TransactionToken;
import com.payment.PaymentGateway.Model.Auth.TokenVerificationStatus;
import com.payment.PaymentGateway.Model.Auth.Token_Type;

/**
 * TransactionToken Related all Services
 */
public interface TokenService{
    /**
     * Build the token based on token type
     * @param tokenType Type of the token
     */
    public TransactionToken buildToken(Token_Type tokenType);

    /**
     * Verify the token
     * @param token TransactionToken we want to verify
     */
    public TokenVerificationStatus verifyToken(TransactionToken token);
}
