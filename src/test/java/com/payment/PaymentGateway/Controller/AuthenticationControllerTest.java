package com.payment.PaymentGateway.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.PaymentGateway.Model.Payment.AuthenticationResponseDTO;
import com.payment.PaymentGateway.Model.Tables.Client;
import com.payment.PaymentGateway.Services.Implementations.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for AuthenticationController
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("AuthenticationController Tests")
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    private AuthenticationResponseDTO mockAuthResponse;

    @BeforeEach
    void setUp() {
        mockAuthResponse = AuthenticationResponseDTO.builder()
                .jwtToken("mock_jwt_token_12345")
                .refreshToken("mock_refresh_token_12345")
                .expiresIn(86400L)
                .tokenType("Bearer")
                .clientId("test_client_001")
                .build();
    }

    @Test
    @DisplayName("Should authenticate client successfully")
    void testAuthenticateSuccess() throws Exception {
        // Arrange
        Map<String, String> credentials = new HashMap<>();
        credentials.put("clientId", "test_client_001");
        credentials.put("clientSecret", "test_secret");

        when(authenticationService.authenticate(anyString(), anyString()))
                .thenReturn(mockAuthResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock_jwt_token_12345"))
                .andExpect(jsonPath("$.refreshToken").value("mock_refresh_token_12345"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(86400))
                .andExpect(jsonPath("$.clientId").value("test_client_001"));
    }

    @Test
    @DisplayName("Should validate token successfully")
    void testValidateTokenSuccess() throws Exception {
        // Arrange
        Map<String, String> tokenRequest = new HashMap<>();
        tokenRequest.put("token", "mock_jwt_token_12345");

        when(authenticationService.validateToken(anyString())).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valid").value(true));
    }

    @Test
    @DisplayName("Should refresh token successfully")
    void testRefreshTokenSuccess() throws Exception {
        // Arrange
        Map<String, String> refreshRequest = new HashMap<>();
        refreshRequest.put("refreshToken", "mock_refresh_token_12345");

        when(authenticationService.refreshToken(anyString()))
                .thenReturn(mockAuthResponse);

        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("Should logout successfully")
    void testLogoutSuccess() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/auth/logout")
                .header("Authorization", "Bearer mock_jwt_token_12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("LOGGED_OUT"));
    }
}
