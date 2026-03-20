package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Exceptions.ClientVerificationException;
import com.payment.PaymentGateway.Model.Payment.AuthenticationResponseDTO;
import com.payment.PaymentGateway.Model.Tables.Client;
import com.payment.PaymentGateway.Repository.ClientRepository;
import com.payment.PaymentGateway.Services.AuthenticationService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

/**
 * Implementation of AuthenticationService
 * Handles JWT token generation and validation
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpiration;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshTokenExpiration;

    @Value("${app.jwt.issuer}")
    private String jwtIssuer;

    @Value("${app.jwt.audience}")
    private String jwtAudience;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Authenticate client and generate JWT token
     */
    @Override
    public AuthenticationResponseDTO authenticate(String clientId, String clientSecret) {
        log.info("Authenticating client: {}", clientId);

        // Fetch client from database
        Optional<Client> clientOptional = clientRepository.findById(clientId);
        if (clientOptional.isEmpty()) {
            log.warn("Client not found: {}", clientId);
            throw new ClientVerificationException("Invalid client ID or secret");
        }

        Client client = clientOptional.get();

        // Verify client secret
        if (!passwordEncoder.matches(clientSecret, client.getClientSecret())) {
            log.warn("Invalid credentials for client: {}", clientId);
            throw new ClientVerificationException("Invalid client ID or secret");
        }

        // Generate tokens
        String accessToken = generateAccessToken(clientId);
        String refreshToken = generateRefreshToken(clientId);

        log.info("Successfully authenticated client: {}", clientId);

        return AuthenticationResponseDTO.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(jwtExpiration / 1000)  // Convert to seconds
                .tokenType("Bearer")
                .clientId(clientId)
                .build();
    }

    /**
     * Refresh JWT token
     */
    @Override
    public AuthenticationResponseDTO refreshToken(String refreshToken) {
        log.info("Refreshing JWT token");

        try {
            String clientId = extractClientIdFromToken(refreshToken);
            String newAccessToken = generateAccessToken(clientId);

            return AuthenticationResponseDTO.builder()
                    .token(newAccessToken)
                    .refreshToken(refreshToken)
                    .expiresIn(jwtExpiration / 1000)
                    .tokenType("Bearer")
                    .clientId(clientId)
                    .build();

        } catch (Exception e) {
            log.error("Failed to refresh token: {}", e.getMessage());
            throw new ClientVerificationException("Invalid refresh token");
        }
    }

    /**
     * Validate JWT token
     */
    @Override
    @Cacheable(value = "tokens", key = "#token")
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Invalidate/blacklist a token
     */
    @Override
    @CacheEvict(value = "tokens", key = "#token")
    public void invalidateToken(String token) {
        log.info("Invalidating token");
        // Token is evicted from cache, effectively blacklisting it
    }

    /**
     * Extract client ID from JWT token
     */
    @Override
    public String extractClientId(String token) {
        return extractClientIdFromToken(token);
    }

    // Private helper methods

    private String generateAccessToken(String clientId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + jwtExpiration);

        return Jwts.builder()
                .setSubject(clientId)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("type", "access")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateRefreshToken(String clientId) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expiryDate = new Date(nowMillis + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(clientId)
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("type", "refresh")
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String extractClientIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
