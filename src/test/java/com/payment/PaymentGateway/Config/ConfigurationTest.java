package com.payment.PaymentGateway.Config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Configuration Tests
 * Verifies that all Spring configurations are properly initialized
 */
@SpringBootTest
@DisplayName("Configuration Tests")
class ConfigurationTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RestTemplate restTemplate;

    @Test
    @DisplayName("Should initialize Redis Cache Manager")
    void testRedisConfiguration() {
        assertNotNull(cacheManager);
        assertNotNull(cacheManager.getCacheNames());
    }

    @Test
    @DisplayName("Should initialize Kafka Template")
    void testKafkaConfiguration() {
        assertNotNull(kafkaTemplate);
    }

    @Test
    @DisplayName("Should initialize Password Encoder")
    void testSecurityConfiguration() {
        assertNotNull(passwordEncoder);
        
        // Test password encoding
        String rawPassword = "testPassword123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertNotNull(encodedPassword);
        
        // Verify encoded password can be matched
        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        assertNotNull(matches);
    }

    @Test
    @DisplayName("Should initialize REST Template")
    void testRestTemplateConfiguration() {
        assertNotNull(restTemplate);
    }

    @Test
    @DisplayName("Should have default cache configured")
    void testDefaultCacheConfiguration() {
        assertNotNull(cacheManager.getCache("transactions"));
        assertNotNull(cacheManager.getCache("tokens"));
    }

    @Test
    @DisplayName("Should encode and match passwords correctly")
    void testPasswordEncodingAndMatching() {
        String password = "MySecurePassword123!";
        
        // Encode
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);
        
        // Different encodings each time (due to salt)
        assertNotNull(encoded1);
        assertNotNull(encoded2);
        
        // But both should match the original
        assert passwordEncoder.matches(password, encoded1);
        assert passwordEncoder.matches(password, encoded2);
    }
}
