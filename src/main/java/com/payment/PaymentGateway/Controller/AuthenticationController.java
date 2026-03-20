package com.payment.PaymentGateway.Controller;

import com.payment.PaymentGateway.Model.Payment.AuthenticationResponseDTO;
import com.payment.PaymentGateway.Services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Authentication Operations
 * Handles JWT token generation and refresh
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication Controller", description = "Authentication and authorization endpoints")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Authenticate client and generate JWT token
     * 
     * @param credentials Client ID and Secret
     * @return JWT token and expiration time
     */
    @PostMapping("/login")
    @Operation(summary = "Authenticate Client", description = "Generate JWT token using client credentials")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody Map<String, String> credentials) {
        
        String clientId = credentials.get("clientId");
        String clientSecret = credentials.get("clientSecret");
        
        log.info("Authenticating client: {}", clientId);
        
        AuthenticationResponseDTO response = authenticationService.authenticate(clientId, clientSecret);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Refresh JWT token
     * 
     * @param refreshToken The refresh token
     * @return New JWT token
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh Token", description = "Generate a new JWT token using refresh token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            @RequestBody Map<String, String> refreshTokenRequest) {
        
        String refreshToken = refreshTokenRequest.get("refreshToken");
        
        log.info("Refreshing JWT token");
        
        AuthenticationResponseDTO response = authenticationService.refreshToken(refreshToken);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Validate JWT token
     * 
     * @param token JWT token to validate
     * @return Validation result
     */
    @PostMapping("/validate")
    @Operation(summary = "Validate Token", description = "Validate if a JWT token is still valid")
    public ResponseEntity<Map<String, Object>> validateToken(
            @RequestBody Map<String, String> tokenRequest) {
        
        String token = tokenRequest.get("token");
        
        boolean isValid = authenticationService.validateToken(token);
        
        return ResponseEntity.ok(Map.of(
                "valid", isValid,
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }

    /**
     * Logout and invalidate token
     * 
     * @param token JWT token to invalidate
     * @return Logout confirmation
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Invalidate the current JWT token")
    public ResponseEntity<Map<String, String>> logout(
            @RequestHeader(value = "Authorization") String token) {
        
        log.info("Processing logout request");
        
        authenticationService.invalidateToken(token);
        
        return ResponseEntity.ok(Map.of(
                "status", "LOGGED_OUT",
                "message", "Token has been invalidated"
        ));
    }
}
