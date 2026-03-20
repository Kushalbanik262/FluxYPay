package com.payment.PaymentGateway.Config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Kafka Configuration for FluxyPay Payment Processor
 * Configures topics, producers, and consumers for event streaming
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

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

    @Value("${app.kafka.topics.transactions-reconciliation:transactions.reconciliation}")
    private String transactionsReconciliationTopic;

    /**
     * Configure Kafka Admin for topic management
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    /**
     * Create payment initiated topic
     */
    @Bean
    public NewTopic paymentsInitiatedTopic() {
        return TopicBuilder.name(paymentsInitiatedTopic)
                .partitions(10)
                .replicas(1)
                .config("retention.ms", String.valueOf(7 * 24 * 60 * 60 * 1000))  // 7 days
                .config("compression.type", "snappy")
                .build();
    }

    /**
     * Create payment completed topic
     */
    @Bean
    public NewTopic paymentsCompletedTopic() {
        return TopicBuilder.name(paymentsCompletedTopic)
                .partitions(10)
                .replicas(1)
                .config("retention.ms", String.valueOf(30 * 24 * 60 * 60 * 1000))  // 30 days
                .config("compression.type", "snappy")
                .build();
    }

    /**
     * Create payment failed topic
     */
    @Bean
    public NewTopic paymentsFailedTopic() {
        return TopicBuilder.name(paymentsFailedTopic)
                .partitions(5)
                .replicas(1)
                .config("retention.ms", String.valueOf(7 * 24 * 60 * 60 * 1000))  // 7 days
                .build();
    }

    /**
     * Create webhook queue topic
     */
    @Bean
    public NewTopic webhooksQueueTopic() {
        return TopicBuilder.name(webhooksQueueTopic)
                .partitions(5)
                .replicas(1)
                .config("retention.ms", String.valueOf(24 * 60 * 60 * 1000))  // 24 hours
                .build();
    }

    /**
     * Create refunds initiated topic
     */
    @Bean
    public NewTopic refundsInitiatedTopic() {
        return TopicBuilder.name(refundsInitiatedTopic)
                .partitions(5)
                .replicas(1)
                .config("retention.ms", String.valueOf(30 * 24 * 60 * 60 * 1000))  // 30 days
                .build();
    }

    /**
     * Create transactions reconciliation topic
     */
    @Bean
    public NewTopic transactionsReconciliationTopic() {
        return TopicBuilder.name(transactionsReconciliationTopic)
                .partitions(3)
                .replicas(1)
                .config("retention.ms", String.valueOf(24 * 60 * 60 * 1000))  // 1 day
                .build();
    }

    /**
     * Configure Kafka Producer Factory
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 10);
        
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Configure Kafka Template for sending messages
     */
    @Bean
    public org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate() {
        return new org.springframework.kafka.core.KafkaTemplate<>(producerFactory());
    }

    /**
     * Configure Kafka Consumer Factory
     */
    @Bean
    public org.springframework.kafka.core.ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, "fluxypay-consumer");
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
                  org.apache.kafka.common.serialization.StringDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
                  org.springframework.kafka.support.serializer.JsonDeserializer.class);
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(org.apache.kafka.clients.consumer.ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(org.springframework.kafka.support.serializer.JsonDeserializer.TRUSTED_PACKAGES, "*");
        
        return new org.springframework.kafka.core.DefaultKafkaConsumerFactory<>(props);
    }

    /**
     * Configure Kafka Listener Container Factory
     */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> 
            kafkaListenerContainerFactory() {
        org.springframework.kafka.listener.ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new org.springframework.kafka.listener.ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setConcurrency(5);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        
        return factory;
    }
}
