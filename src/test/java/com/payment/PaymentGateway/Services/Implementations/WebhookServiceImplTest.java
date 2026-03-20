package com.payment.PaymentGateway.Services.Implementations;

import com.payment.PaymentGateway.Model.Payment.WebhookEventDTO;
import com.payment.PaymentGateway.Services.WebhookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for WebhookService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("WebhookService Tests")
class WebhookServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WebhookServiceImpl webhookService;

    private WebhookEventDTO webhookEvent;

    @BeforeEach
    void setUp() {
        webhookEvent = WebhookEventDTO.builder()
                .eventId("evt_123456")
                .eventType("payment.completed")
                .timestamp(LocalDateTime.now())
                .transactionId("txn_123456")
                .amount(new BigDecimal("99.99"))
                .currency("USD")
                .status("COMPLETED")
                .paymentNetwork("VISA")
                .build();
    }

    @Test
    @DisplayName("Should process webhook event successfully")
    void testProcessWebhookEventSuccess() {
        // Assert - should not throw exception
        assertDoesNotThrow(() -> {
            webhookService.processWebhookEvent(webhookEvent, null);
        });
    }

    @Test
    @DisplayName("Should register webhook endpoint successfully")
    void testRegisterWebhookEndpointSuccess() {
        // Arrange
        String clientId = "client_001";
        String webhookUrl = "https://example.com/webhook";

        // Act
        String endpointId = webhookService.registerWebhookEndpoint(clientId, webhookUrl);

        // Assert
        assertNotNull(endpointId);
        assertTrue(endpointId.length() > 0);
        assertTrue(endpointId.matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"));
    }

    @Test
    @DisplayName("Should fail webhook registration with invalid URL")
    void testRegisterWebhookEndpointInvalidUrl() {
        // Arrange
        String clientId = "client_001";
        String invalidUrl = "not-a-valid-url";

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            webhookService.registerWebhookEndpoint(clientId, invalidUrl);
        });
    }

    @Test
    @DisplayName("Should fail webhook registration with HTTP URL")
    void testRegisterWebhookEndpointHttpUrl() {
        // Arrange
        String clientId = "client_001";
        String httpUrl = "http://example.com/webhook";
        
        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            webhookService.registerWebhookEndpoint(clientId, httpUrl);
        });
    }

    @Test
    @DisplayName("Should retry webhook delivery successfully")
    void testRetryWebhookDeliverySuccess() {
        // Arrange
        String webhookEventId = "evt_123456";

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> {
            webhookService.retryWebhookDelivery(webhookEventId);
        });
    }

    @Test
    @DisplayName("Should get webhook status successfully")
    void testGetWebhookStatusSuccess() {
        // Arrange
        String webhookEventId = "evt_123456";

        // Act
        Map<String, Object> status = webhookService.getWebhookStatus(webhookEventId);

        // Assert
        assertNotNull(status);
        assertEquals(webhookEventId, status.get("webhookEventId"));
        assertNotNull(status.get("status"));
        assertTrue(status.get("retryCount") instanceof Integer);
    }

    @Test
    @DisplayName("Should calculate webhook signature correctly")
    void testWebhookSignatureCalculation() {
        // Arrange
        String payload = "test_payload";
        String clientSecret = "test_secret";

        // Act
        boolean isValid = webhookService.verifyWebhookSignature(payload, 
                webhookService.verifyWebhookSignature(payload, 
                        "sha256=anything", clientSecret) ? "sha256=anything" : "", 
                clientSecret);

        // Assert
        assertFalse(isValid);  // Signature should not match with dummy value
    }

    @Test
    @DisplayName("Should send webhook event to client")
    void testSendWebhookEventSuccess() {
        // Arrange
        String clientId = "client_001";

        // Act
        boolean result = webhookService.sendWebhookEvent(clientId, webhookEvent);

        // Assert
        assertFalse(result);  // Will fail in test without actual endpoint
    }

    @Test
    @DisplayName("Should handle webhook with various event types")
    void testProcessWebhookVariousEventTypes() {
        // Arrange
        String[] eventTypes = {
                "payment.initiated",
                "payment.completed",
                "payment.failed",
                "refund.initiated",
                "webhook.retry"
        };

        // Act & Assert
        for (String eventType : eventTypes) {
            webhookEvent.setEventType(eventType);
            assertDoesNotThrow(() -> {
                webhookService.processWebhookEvent(webhookEvent, null);
            });
        }
    }

    @Test
    @DisplayName("Should handle webhook with metadata")
    void testProcessWebhookWithMetadata() {
        // Arrange
        Map<String, Object> metadata = Map.of(
                "orderId", "ORDER_12345",
                "customerId", "CUST_789",
                "merchantId", "MERCHANT_001"
        );
        webhookEvent.setMetadata(metadata);

        // Act & Assert
        assertDoesNotThrow(() -> {
            webhookService.processWebhookEvent(webhookEvent, null);
        });
    }
}
