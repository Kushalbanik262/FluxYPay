package com.payment.PaymentGateway.Services;

import com.payment.PaymentGateway.Model.Payment.WebhookEventDTO;
import java.util.Map;

/**
 * Service for handling webhook operations and event delivery
 */
public interface WebhookService {
    
    /**
     * Process incoming webhook event
     * 
     * @param event The webhook event
     * @param signature HMAC signature for verification
     */
    void processWebhookEvent(WebhookEventDTO event, String signature);
    
    /**
     * Register a webhook endpoint for a client
     * 
     * @param clientId The client identifier
     * @param webhookUrl The webhook URL
     * @return Endpoint ID
     */
    String registerWebhookEndpoint(String clientId, String webhookUrl);
    
    /**
     * Retry failed webhook delivery
     * 
     * @param webhookEventId The webhook event ID
     */
    void retryWebhookDelivery(String webhookEventId);
    
    /**
     * Get webhook delivery status
     * 
     * @param webhookEventId The webhook event ID
     * @return Status details
     */
    Map<String, Object> getWebhookStatus(String webhookEventId);
    
    /**
     * Send webhook event to registered endpoint
     * 
     * @param clientId The client identifier
     * @param event The webhook event
     * @return true if successful, false otherwise
     */
    boolean sendWebhookEvent(String clientId, WebhookEventDTO event);
    
    /**
     * Verify webhook signature
     * 
     * @param payload The event payload
     * @param signature The signature to verify
     * @param clientSecret The client secret for verification
     * @return true if signature is valid
     */
    boolean verifyWebhookSignature(String payload, String signature, String clientSecret);
}
