package com.payment.PaymentGateway.Model.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

/**
 * Webhook Event DTO
 * Used for webhook event processing and delivery
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookEventDTO {
    private String eventId;
    private String eventType;
    private String transactionId;
    private Map<String, Object> payload;
    private String signature;
    private Long timestamp;
    private Integer retryCount;
    private String status;
    private String webhookUrl;
    private String metadata;

    // Explicit getters for Lombok processing fallback
    public String getEventId() { return this.eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    
    public String getEventType() { return this.eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    
    public String getTransactionId() { return this.transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    
    public Map<String, Object> getPayload() { return this.payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }
    
    public String getSignature() { return this.signature; }
    public void setSignature(String signature) { this.signature = signature; }
    
    public Long getTimestamp() { return this.timestamp; }
    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
    
    public Integer getRetryCount() { return this.retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }
    
    public String getStatus() { return this.status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getWebhookUrl() { return this.webhookUrl; }
    public void setWebhookUrl(String webhookUrl) { this.webhookUrl = webhookUrl; }
    
    public String getMetadata() { return this.metadata; }
    public void setMetadata(String metadata) { this.metadata = metadata; }
}
