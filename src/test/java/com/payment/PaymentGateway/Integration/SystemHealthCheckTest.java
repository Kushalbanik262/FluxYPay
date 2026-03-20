package com.payment.PaymentGateway.Integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * System Health and Configuration Tests
 * Verifies that all services and configurations are properly initialized
 */
@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("System Health Check Tests")
class SystemHealthCheckTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // System is ready for tests
    }

    @Test
    @DisplayName("Should return UP status from health endpoint")
    void testApplicationHealth() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("Should expose Prometheus metrics endpoint")
    void testPrometheusMetricsEndpoint() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").isArray());
    }

    @Test
    @DisplayName("Should provide application info")
    void testApplicationInfo() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should verify Payment Service is available")
    void testPaymentServiceAvailability() throws Exception {
        mockMvc.perform(get("/api/v1/payments/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("Payment Service"));
    }

    @Test
    @DisplayName("Should handle OPTIONS requests for CORS")
    void testCorsSupport() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                .options("/api/v1/payments/initiate"))
                .andExpect(status().isOk());
    }
}
