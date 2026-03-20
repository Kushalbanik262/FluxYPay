package com.payment.PaymentGateway.Services.Kafka;

import com.payment.PaymentGateway.Model.Payment.WebhookEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.rebalance.ConsumerAwareRebalanceListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * Kafka Event Consumer for Processing Payment Events
 * Consumes events from various Kafka topics
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    /**
     * Consume webhook events for delivery
     */
    @KafkaListener(
            topics = "${app.kafka.topics.webhooks-queue:webhooks.queue}",
            groupId = "webhook-processor-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeWebhookEvent(
            @Payload WebhookEventDTO event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            Acknowledgment acknowledgment) {

        try {
            log.info("Consuming webhook event: {} from partition: {} offset: {}", 
                     event.getEventId(), partition, offset);

            // Process webhook event
            processWebhookDelivery(event);

            // Acknowledge message after successful processing
            acknowledgment.acknowledge();
            log.debug("Acknowledged webhook event: {}", event.getEventId());

        } catch (Exception e) {
            log.error("Failed to consume webhook event: {} - {}", event.getEventId(), e.getMessage());
            // Don't acknowledge - message will be retried
        }
    }

    /**
     * Consume completed payment events
     */
    @KafkaListener(
            topics = "${app.kafka.topics.payments-completed:payments.completed}",
            groupId = "payment-processor-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentCompleted(
            @Payload WebhookEventDTO event,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            Acknowledgment acknowledgment) {

        try {
            log.info("Processing completed payment event: {} from topic: {}", 
                     event.getTransactionId(), topic);

            // Update transaction status in database
            updateTransactionStatus(event);

            // Send webhook notification
            scheduleWebhookDelivery(event);

            acknowledgment.acknowledge();
            log.debug("Acknowledged payment completed event: {}", event.getTransactionId());

        } catch (Exception e) {
            log.error("Failed to process payment completed event: {} - {}", 
                     event.getTransactionId(), e.getMessage());
        }
    }

    /**
     * Consume failed payment events
     */
    @KafkaListener(
            topics = "${app.kafka.topics.payments-failed:payments.failed}",
            groupId = "payment-processor-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumePaymentFailed(
            @Payload WebhookEventDTO event,
            Acknowledgment acknowledgment) {

        try {
            log.warn("Processing failed payment event: {}", event.getTransactionId());

            // Update transaction status as failed
            updateTransactionStatus(event);

            // Send failure webhook notification
            scheduleWebhookDelivery(event);

            acknowledgment.acknowledge();
            log.debug("Acknowledged payment failed event: {}", event.getTransactionId());

        } catch (Exception e) {
            log.error("Failed to process payment failed event: {} - {}", 
                     event.getTransactionId(), e.getMessage());
        }
    }

    /**
     * Consume refund initiated events
     */
    @KafkaListener(
            topics = "${app.kafka.topics.refunds-initiated:refunds.initiated}",
            groupId = "refund-processor-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeRefundInitiated(
            @Payload WebhookEventDTO event,
            Acknowledgment acknowledgment) {

        try {
            log.info("Processing refund initiated event: {}", event.getTransactionId());

            // Process refund
            processRefund(event);

            acknowledgment.acknowledge();
            log.debug("Acknowledged refund initiated event: {}", event.getTransactionId());

        } catch (Exception e) {
            log.error("Failed to process refund initiated event: {} - {}", 
                     event.getTransactionId(), e.getMessage());
        }
    }

    /**
     * Consume reconciliation events
     */
    @KafkaListener(
            topics = "${app.kafka.topics.transactions-reconciliation:transactions.reconciliation}",
            groupId = "reconciliation-processor-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeReconciliationEvent(
            @Payload WebhookEventDTO event,
            Acknowledgment acknowledgment) {

        try {
            log.info("Processing reconciliation event: {}", event.getTransactionId());

            // Perform reconciliation checks
            reconcileTransaction(event);

            acknowledgment.acknowledge();
            log.debug("Acknowledged reconciliation event: {}", event.getTransactionId());

        } catch (Exception e) {
            log.error("Failed to process reconciliation event: {} - {}", 
                     event.getTransactionId(), e.getMessage());
        }
    }

    // Private helper methods

    private void processWebhookDelivery(WebhookEventDTO event) {
        // TODO: Implement webhook HTTP delivery logic
        log.debug("Processing webhook delivery for event: {}", event.getEventId());
    }

    private void updateTransactionStatus(WebhookEventDTO event) {
        // TODO: Update transaction status in database
        log.debug("Updating transaction status for: {}", event.getTransactionId());
    }

    private void scheduleWebhookDelivery(WebhookEventDTO event) {
        // TODO: Schedule webhook delivery to client
        log.debug("Scheduling webhook delivery for event: {}", event.getEventId());
    }

    private void processRefund(WebhookEventDTO event) {
        // TODO: Process refund logic
        log.debug("Processing refund for transaction: {}", event.getTransactionId());
    }

    private void reconcileTransaction(WebhookEventDTO event) {
        // TODO: Implement reconciliation logic
        log.debug("Reconciling transaction: {}", event.getTransactionId());
    }
}
