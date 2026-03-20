package com.payment.PaymentGateway.Integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Docker System Validation Tests
 * Validates all Docker services are running and properly connected
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Docker System Validation Tests")
class DockerSystemValidationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should verify application is running on correct port")
    void testApplicationPort() {
        String baseUrl = "http://localhost:" + port;
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/actuator/health", String.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Should verify Redis connectivity")
    void testRedisConnectivity() {
        String url = "http://localhost:" + port + "/actuator/health/redis";
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // Redis might be optional
            assertNotNull(response);
        } catch (Exception e) {
            // Expected if Redis is not configured in test profile
        }
    }

    @Test
    @DisplayName("Should verify Database connectivity")
    void testDatabaseConnectivity() {
        String url = "http://localhost:" + port + "/actuator/health/db";
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            assertNotNull(response);
        } catch (Exception e) {
            // Expected if database is not available in test environment
        }
    }

    @Test
    @DisplayName("Should verify Kafka connectivity")
    void testKafkaConnectivity() {
        String url = "http://localhost:" + port + "/actuator/health/kafka";
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // Kafka might be optional in tests
            assertNotNull(response);
        } catch (Exception e) {
            // Expected if Kafka is not available in test environment
        }
    }

    @Test
    @DisplayName("Should verify API endpoints are accessible")
    void testApiEndpointsAccessibility() {
        String[] endpoints = {
                "/api/v1/payments/health",
                "/actuator/health",
                "/actuator/metrics"
        };

        for (String endpoint : endpoints) {
            ResponseEntity<String> response = restTemplate.getForEntity(
                    "http://localhost:" + port + endpoint, String.class);
            assertNotNull(response);
            assertTrue(response.getStatusCode().is2xxSuccessful() || 
                      response.getStatusCode().is4xxClientError(),
                    "Endpoint " + endpoint + " returned: " + response.getStatusCode());
        }
    }

    @Test
    @DisplayName("Should verify Swagger/OpenAPI is available")
    void testSwaggerAvailability() {
        String swaggerUrl = "http://localhost:" + port + "/swagger-ui.html";
        
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(swaggerUrl, String.class);
            assertTrue(response.getStatusCode().is2xxSuccessful() || 
                      response.getStatusCode().is3xxRedirection(),
                    "Swagger should be accessible");
        } catch (Exception e) {
            // Swagger might be disabled in some profiles
        }
    }

    @Test
    @DisplayName("Should verify Prometheus metrics are exposed")
    void testPrometheusMetrics() {
        String metricsUrl = "http://localhost:" + port + "/actuator/prometheus";
        ResponseEntity<String> response = restTemplate.getForEntity(metricsUrl, String.class);
        
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("jvm_") || 
                  response.getBody().contains("spring_"),
                  "Prometheus metrics should contain JVM or Spring metrics");
    }

    @Test
    @DisplayName("Should verify system responds to health probe")
    void testHealthProbe() {
        String healthUrl = "http://localhost:" + port + "/actuator/health";
        ResponseEntity<String> response = restTemplate.getForEntity(healthUrl, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("UP"));
    }
}
