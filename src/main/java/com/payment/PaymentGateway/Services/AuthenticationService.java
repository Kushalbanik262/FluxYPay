package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Payment.AuthenticationResponseDTO;

/**
 * Service for handling authentication and JWT token operations
 */
public interface AuthenticationService {
    
    /**
     * Authenticate client and generate JWT token
     * 
     * @param clientId The client identifier
     * @param clientSecret The client secret
     * @return Authentication response with JWT token
     */
    AuthenticationResponseDTO authenticate(String clientId, String clientSecret);
    
    /**
     * Refresh JWT token using refresh token
     * 
     * @param refreshToken The refresh token
     * @return New authentication response
     */
    AuthenticationResponseDTO refreshToken(String refreshToken);
    
    /**
     * Validate if JWT token is still valid
     * 
     * @param token The JWT token to validate
     * @return true if valid, false otherwise
     */
    boolean validateToken(String token);
    
    /**
     * Invalidate/blacklist a JWT token
     * 
     * @param token The JWT token to invalidate
     */
    void invalidateToken(String token);
    
    /**
     * Extract client ID from JWT token
     * 
     * @param token The JWT token
     * @return Client ID
     */
    String extractClientId(String token);
}
