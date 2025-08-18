package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Tables.Token;
import com.payment.PaymentGateway.Model.Auth.TokenVerificationStatus;
import com.payment.PaymentGateway.Model.Auth.Token_Type;

/**
 * Token Related all Services
 */
public interface TokenService{
    /**
     * Build the token based on token type
     * @param tokenType Type of the token
     */
    public Token buildToken(Token_Type tokenType);

    /**
     * Verify the token
     * @param token Token we want to verify
     */
    public TokenVerificationStatus verifyToken(Token token);
}
