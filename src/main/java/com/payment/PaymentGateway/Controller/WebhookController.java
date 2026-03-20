package com.payment.PaymentGateway.Controller;

import com.payment.PaymentGateway.Model.Payment.WebhookEventDTO;
import com.payment.PaymentGateway.Services.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for Webhook Operations
 * Handles webhook event processing and retries
 */
@Slf4j
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
@Tag(name = "Webhook Controller", description = "Webhook event management endpoints")
public class WebhookController {

    private final WebhookService webhookService;

    /**
     * Process incoming webhook event
     * 
     * @param event Webhook event payload
     * @param signature HMAC-SHA256 signature for verification
     * @return Webhook processing result
     */
    @PostMapping("/event")
    @Operation(summary = "Process Webhook Event", description = "Process incoming webhook with signature verification")
    public ResponseEntity<Map<String, Object>> processWebhookEvent(
            @RequestBody WebhookEventDTO event,
            @RequestHeader(value = "X-Webhook-Signature", required = false) String signature) {
        
        log.info("Processing webhook event: {} for transaction: {}", 
                 event.getEventType(), event.getTransactionId());
        
        webhookService.processWebhookEvent(event, signature);
        
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "status", "ACCEPTED",
                "eventId", event.getEventId(),
                "message", "Webhook event queued for processing"
        ));
    }

    /**
     * Register webhook endpoint for client
     * 
     * @param clientId Client identifier
     * @param webhookUrl Webhook URL to register
     * @return Registration response
     */
    @PostMapping("/register")
    @Operation(summary = "Register Webhook URL", description = "Register a webhook endpoint for payment notifications")
    public ResponseEntity<Map<String, Object>> registerWebhook(
            @RequestParam String clientId,
            @RequestParam String webhookUrl) {
        
        log.info("Registering webhook for client: {} with URL: {}", clientId, webhookUrl);
        
        String endpointId = webhookService.registerWebhookEndpoint(clientId, webhookUrl);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "status", "REGISTERED",
                "endpointId", endpointId,
                "webhookUrl", webhookUrl
        ));
    }

    /**
     * Retry failed webhook delivery
     * 
     * @param webhookEventId Webhook event ID
     * @return Retry result
     */
    @PostMapping("/{webhookEventId}/retry")
    @Operation(summary = "Retry Webhook Delivery", description = "Manually retry a failed webhook delivery")
    public ResponseEntity<Map<String, Object>> retryWebhookDelivery(
            @PathVariable String webhookEventId) {
        
        log.info("Retrying webhook delivery for event ID: {}", webhookEventId);
        
        webhookService.retryWebhookDelivery(webhookEventId);
        
        return ResponseEntity.accepted().body(Map.of(
                "status", "RETRY_QUEUED",
                "webhookEventId", webhookEventId,
                "message", "Webhook delivery queued for retry"
        ));
    }

    /**
     * Get webhook delivery status
     * 
     * @param webhookEventId Webhook event ID
     * @return Webhook delivery status details
     */
    @GetMapping("/{webhookEventId}/status")
    @Operation(summary = "Get Webhook Status", description = "Check the delivery status of a webhook event")
    public ResponseEntity<Map<String, Object>> getWebhookStatus(
            @PathVariable String webhookEventId) {
        
        log.info("Fetching webhook status for event ID: {}", webhookEventId);
        
        Map<String, Object> status = webhookService.getWebhookStatus(webhookEventId);
        
        return ResponseEntity.ok(status);
    }
}
