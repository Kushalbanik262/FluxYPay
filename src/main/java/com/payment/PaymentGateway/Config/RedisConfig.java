package com.payment.PaymentGateway.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serialization.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serialization.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis Configuration for Caching
 * Provides cache management for transactions, tokens, and webhook statuses
 */
@Configuration
@EnableCaching
public class RedisConfig {

    @Value("${app.cache.default-ttl-seconds:3600}")
    private long defaultTtlSeconds;

    @Value("${app.cache.transaction-ttl-seconds:86400}")
    private long transactionTtlSeconds;

    @Value("${app.cache.token-ttl-seconds:86400}")
    private long tokenTtlSeconds;

    /**
     * Configure Redis Connection Factory
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    /**
     * Configure Redis Template for generic operations
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = 
                new GenericJackson2JsonRedisSerializer();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // Set key serialization
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);

        // Set value serialization
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * Configure Cache Manager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(defaultTtlSeconds))
                .serializeKeysWith(
                        org.springframework.data.redis.serialization.RedisSerializationContext
                                .SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        org.springframework.data.redis.serialization.RedisSerializationContext
                                .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withCacheConfiguration("transactions", 
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofSeconds(transactionTtlSeconds)))
                .withCacheConfiguration("tokens", 
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofSeconds(tokenTtlSeconds)))
                .withCacheConfiguration("webhook-status", 
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofSeconds(3600)))
                .build();
    }
}
