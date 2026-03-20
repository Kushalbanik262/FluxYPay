package com.payment.PaymentGateway.Services.Kafka;

import com.payment.PaymentGateway.Model.Payment.WebhookEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * Kafka Event Producer for Payment Events
 * Produces events to various Kafka topics for async processing
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.payments-initiated:payments.initiated}")
    private String paymentsInitiatedTopic;

    @Value("${app.kafka.topics.payments-completed:payments.completed}")
    private String paymentsCompletedTopic;

    @Value("${app.kafka.topics.payments-failed:payments.failed}")
    private String paymentsFailedTopic;

    @Value("${app.kafka.topics.webhooks-queue:webhooks.queue}")
    private String webhooksQueueTopic;

    @Value("${app.kafka.topics.refunds-initiated:refunds.initiated}")
    private String refundsInitiatedTopic;

    /**
     * Publish payment initiated event
     */
    public void publishPaymentInitiated(WebhookEventDTO event) {
        try {
            Message<WebhookEventDTO> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, paymentsInitiatedTopic)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.getTransactionId())
                    .setHeader("eventType", event.getEventType())
                    .setHeader("timestamp", System.currentTimeMillis())
                    .build();

            kafkaTemplate.send(message);
            log.info("Published payment initiated event for transaction: {}", event.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to publish payment initiated event: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to publish payment event", e);
        }
    }

    /**
     * Publish payment completed event
     */
    public void publishPaymentCompleted(WebhookEventDTO event) {
        try {
            Message<WebhookEventDTO> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, paymentsCompletedTopic)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.getTransactionId())
                    .setHeader("eventType", event.getEventType())
                    .setHeader("timestamp", System.currentTimeMillis())
                    .build();

            kafkaTemplate.send(message);
            log.info("Published payment completed event for transaction: {}", event.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to publish payment completed event: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to publish payment event", e);
        }
    }

    /**
     * Publish payment failed event
     */
    public void publishPaymentFailed(WebhookEventDTO event) {
        try {
            Message<WebhookEventDTO> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, paymentsFailedTopic)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.getTransactionId())
                    .setHeader("eventType", event.getEventType())
                    .setHeader("timestamp", System.currentTimeMillis())
                    .build();

            kafkaTemplate.send(message);
            log.warn("Published payment failed event for transaction: {}", event.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to publish payment failed event: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to publish payment event", e);
        }
    }

    /**
     * Publish webhook event for delivery
     */
    public void publishWebhookEvent(WebhookEventDTO event) {
        try {
            Message<WebhookEventDTO> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, webhooksQueueTopic)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.getEventId())
                    .setHeader("eventType", event.getEventType())
                    .setHeader("timestamp", System.currentTimeMillis())
                    .build();

            kafkaTemplate.send(message);
            log.info("Published webhook event to queue: {}", event.getEventId());
        } catch (Exception e) {
            log.error("Failed to publish webhook event: {}", event.getEventId(), e);
            throw new RuntimeException("Failed to publish webhook event", e);
        }
    }

    /**
     * Publish refund initiated event
     */
    public void publishRefundInitiated(WebhookEventDTO event) {
        try {
            Message<WebhookEventDTO> message = MessageBuilder
                    .withPayload(event)
                    .setHeader(KafkaHeaders.TOPIC, refundsInitiatedTopic)
                    .setHeader(KafkaHeaders.MESSAGE_KEY, event.getTransactionId())
                    .setHeader("eventType", event.getEventType())
                    .setHeader("timestamp", System.currentTimeMillis())
                    .build();

            kafkaTemplate.send(message);
            log.info("Published refund initiated event for transaction: {}", event.getTransactionId());
        } catch (Exception e) {
            log.error("Failed to publish refund initiated event: {}", event.getTransactionId(), e);
            throw new RuntimeException("Failed to publish refund event", e);
        }
    }
}
