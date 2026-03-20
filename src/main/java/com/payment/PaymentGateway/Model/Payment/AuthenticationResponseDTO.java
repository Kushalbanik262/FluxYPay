package com.payment.PaymentGateway.Model.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authentication Response DTO
 * Contains JWT token and refresh token after successful authentication
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String jwtToken;
    private String refreshToken;
    private String clientId;
    private long expiresIn;
    private String tokenType;

    public AuthenticationResponseDTO(String jwtToken, String refreshToken) {
        this.jwtToken = jwtToken;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.expiresIn = 86400; // 24 hours
    }

    // Convenience getters for backward compatibility
    public String getToken() { return this.jwtToken; }
    public void setToken(String token) { this.jwtToken = token; }
}
