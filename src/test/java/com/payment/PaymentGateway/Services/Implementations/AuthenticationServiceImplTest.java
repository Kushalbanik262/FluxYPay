package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Exceptions.ClientVerificationException;
import com.payment.PaymentGateway.Model.Payment.AuthenticationResponseDTO;
import com.payment.PaymentGateway.Model.Tables.Client;
import com.payment.PaymentGateway.Repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for AuthenticationService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthenticationService Tests")
class AuthenticationServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setClientId("test_client_001");
        testClient.setClientSecret("hashed_secret_key");
    }

    @Test
    @DisplayName("Should authenticate client successfully with valid credentials")
    void testAuthenticateSuccess() {
        // Arrange
        String clientId = "test_client_001";
        String clientSecret = "valid_secret";

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(passwordEncoder.matches(clientSecret, testClient.getClientSecret())).thenReturn(true);

        // Act
        AuthenticationResponseDTO response = authenticationService.authenticate(clientId, clientSecret);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getToken());
        assertNotNull(response.getRefreshToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals(clientId, response.getClientId());
        assertTrue(response.getExpiresIn() > 0);

        verify(clientRepository, times(1)).findById(clientId);
        verify(passwordEncoder, times(1)).matches(clientSecret, testClient.getClientSecret());
    }

    @Test
    @DisplayName("Should fail authentication with invalid client ID")
    void testAuthenticateFailsWithInvalidClientId() {
        // Arrange
        String clientId = "invalid_client";
        String clientSecret = "any_secret";

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ClientVerificationException.class, () ->
                authenticationService.authenticate(clientId, clientSecret)
        );

        verify(clientRepository, times(1)).findById(clientId);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("Should fail authentication with invalid password")
    void testAuthenticateFailsWithInvalidPassword() {
        // Arrange
        String clientId = "test_client_001";
        String clientSecret = "wrong_secret";

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(passwordEncoder.matches(clientSecret, testClient.getClientSecret())).thenReturn(false);

        // Act & Assert
        assertThrows(ClientVerificationException.class, () ->
                authenticationService.authenticate(clientId, clientSecret)
        );

        verify(clientRepository, times(1)).findById(clientId);
        verify(passwordEncoder, times(1)).matches(clientSecret, testClient.getClientSecret());
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testValidateTokenSuccess() {
        // Arrange
        String clientId = "test_client_001";
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        AuthenticationResponseDTO authResponse = authenticationService.authenticate(clientId, "secret");
        String token = authResponse.getToken();

        // Act
        boolean isValid = authenticationService.validateToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Should fail validation with invalid token")
    void testValidateTokenWithInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = authenticationService.validateToken(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Should extract client ID from valid token")
    void testExtractClientIdFromToken() {
        // Arrange
        String clientId = "test_client_001";
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(testClient));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        AuthenticationResponseDTO authResponse = authenticationService.authenticate(clientId, "secret");
        String token = authResponse.getToken();

        // Act
        String extractedClientId = authenticationService.extractClientId(token);

        // Assert
        assertEquals(clientId, extractedClientId);
    }
}
