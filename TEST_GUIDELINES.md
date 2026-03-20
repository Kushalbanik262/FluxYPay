# FluxyPay Test Development Guidelines

## Overview
This document provides guidelines for developing, maintaining, and extending the FluxyPay test suite.

---

## Test Types & Levels

### 1. Unit Tests
**Purpose:** Validate individual service/method functionality in isolation

**Tools:** JUnit 5, Mockito

**Location:** `src/test/java/.../Services/Implementations/`

**Example Pattern:**
```java
@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {
    @Mock
    private IdempotencyTokenRepository repository;
    
    @InjectMocks
    private PaymentServiceImpl service;
    
    @Test
    @DisplayName("Should process payment successfully")
    void testProcessPaymentSuccess() {
        // Arrange
        when(repository.findById(any())).thenReturn(Optional.empty());
        
        // Act
        TransactionResponse response = service.processPayment(request);
        
        // Assert
        assertNotNull(response);
        assertEquals(EXPECTED_STATUS, response.getStatus());
    }
}
```

**Coverage Target:** 90%+

---

### 2. Integration Tests
**Purpose:** Validate component interactions and REST endpoint functionality

**Tools:** Spring Boot Test, MockMvc, TestRestTemplate

**Location:** `src/test/java/.../Controller/` and `.../Integration/`

**Example Pattern:**
```java
@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @DisplayName("Should initiate payment with valid request")
    void testInitiatePaymentSuccess() throws Exception {
        // Arrange
        PaymentRequest request = createValidRequest();
        
        // Act & Assert
        mockMvc.perform(post("/api/v1/payments/initiate")
            .header("Authorization", "Bearer " + token)
            .header("Idempotency-Key", UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.transactionId").isNotEmpty());
    }
}
```

**Coverage Target:** 85%+

---

### 3. System Integration Tests
**Purpose:** Validate end-to-end workflows with Spring context

**Tools:** Spring Boot Test (no mocks)

**Location:** `src/test/java/.../Integration/`

**Example Pattern:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SystemIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    @DisplayName("Should complete full payment flow")
    void testEndToEndPaymentProcessing() {
        // Login
        AuthResponse auth = restTemplate.postForObject(
            "/api/v1/auth/login", loginRequest, AuthResponse.class);
        
        // Initiate Payment
        PaymentResponse payment = restTemplate.postForObject(
            "/api/v1/payments/initiate", 
            paymentRequest, 
            PaymentResponse.class, 
            auth.getToken());
        
        // Assert
        assertNotNull(payment.getTransactionId());
    }
}
```

---

### 4. Docker System Validation Tests
**Purpose:** Validate Docker service connectivity and API access

**Tools:** TestRestTemplate, Docker exec commands

**Location:** `src/test/java/.../Integration/`

**Example Pattern:**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DockerSystemValidationTest {
    private TestRestTemplate restTemplate;
    
    @Test
    @DisplayName("Should verify database connectivity")
    void testDatabaseConnectivity() throws Exception {
        // Call application endpoint that uses database
        HttpResponse response = restTemplate.getForEntity(
            "/api/v1/payments/health", 
            String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

---

## Test Organization Best Practices

### Directory Structure
```
src/test/java/com/payment/PaymentGateway/
├── Services/Implementations/
│   ├── AuthenticationServiceImplTest.java
│   ├── PaymentServiceImplTest.java
│   └── WebhookServiceImplTest.java
├── Controller/
│   ├── AuthenticationControllerTest.java
│   └── PaymentControllerTest.java
├── Integration/
│   ├── SystemIntegrationTest.java
│   ├── SystemHealthCheckTest.java
│   └── DockerSystemValidationTest.java
├── Config/
│   └── ConfigurationTest.java
└── Fixtures/
    ├── PaymentFixtures.java
    ├── AuthFixtures.java
    └── WebhookFixtures.java
```

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Test Class | `<Class>Test` | `PaymentServiceImplTest` |
| Test Method | `test<Scenario>` | `testProcessPaymentSuccess` |
| Helper Method | `create<Entity>` | `createValidPaymentRequest()` |
| Mock Object | `mock<Class>` | `mockPaymentRepository` |

---

## Writing Effective Tests

### AAA Pattern (Arrange-Act-Assert)
```java
@Test
void testExample() {
    // ARRANGE - Set up test data and mocks
    String input = "test";
    when(mock.method()).thenReturn(expected);
    
    // ACT - Execute the code under test
    String result = service.process(input);
    
    // ASSERT - Verify the result
    assertEquals(expected, result);
}
```

### Test Data Builders
```java
public class PaymentRequestBuilder {
    private PaymentRequest request;
    
    public PaymentRequestBuilder() {
        request = new PaymentRequest();
        request.setAmount(100.00);
        request.setCurrency("USD");
    }
    
    public PaymentRequestBuilder withAmount(double amount) {
        request.setAmount(amount);
        return this;
    }
    
    public PaymentRequest build() {
        return request;
    }
}

// Usage
PaymentRequest request = new PaymentRequestBuilder()
    .withAmount(50.00)
    .build();
```

### Parameterized Tests
```java
@ParameterizedTest
@ValueSource(strings = {"USD", "INR", "EUR"})
void testMultipleCurrencies(String currency) {
    PaymentRequest request = createRequest(currency);
    assertDoesNotThrow(() -> service.validate(request));
}

@ParameterizedTest
@CsvSource({
    "100, 10, 90",
    "50, 5, 45",
    "1000, 100, 900"
})
void testRefundCalculation(double original, double refund, double expected) {
    assertEquals(expected, refund - original);
}
```

---

## Mocking Strategies

### When to Mock
- ✅ External services (Mail, SMS, Payment Gateway)
- ✅ Repository/Database access
- ✅ Remote API calls
- ✅ Time-dependent functionality

### When NOT to Mock
- ❌ Actual business logic
- ❌ Configuration classes
- ❌ Object builders
- ❌ Utility functions

### Mock Setup Best Practices
```java
@ExtendWith(MockitoExtension.class)
class ServiceTest {
    @Mock
    private ExternalService externalService;
    
    @InjectMocks
    private ServiceImpl service;
    
    @BeforeEach
    void setUp() {
        // Set default mock behavior
        when(externalService.isAvailable())
            .thenReturn(true);
    }
    
    @Test
    void testWithMock() {
        // Arrange
        when(externalService.call(any())).thenReturn(result);
        
        // Act & Assert
        assertNotNull(service.process());
    }
}
```

---

## Assertion Best Practices

### Use Specific Assertions
```java
// ✅ Good - Clear and specific
assertEquals("COMPLETED", transaction.getStatus());
assertTrue(amount > 0);
assertNotNull(response);

// ❌ Avoid - Too generic
assertTrue(transaction != null);
assertTrue(transaction.getStatus() != null);
```

### Use Meaningful Messages
```java
// ✅ Good - Describes what was expected
assertEquals("Transaction should be COMPLETED", 
    "COMPLETED", transaction.getStatus());

// ❌ Avoid - No context
assertEquals("COMPLETED", transaction.getStatus());
```

### Test Multiple Scenarios
```java
@Test
@DisplayName("Should handle various payment statuses")
void testPaymentStatuses() {
    // Test success
    assertEquals(SUCCESS, service.process(validRequest));
    
    // Test failure
    assertThrows(InvalidPaymentException.class, 
        () -> service.process(invalidRequest));
    
    // Test edge case
    assertEquals(PENDING, service.process(pendingRequest));
}
```

---

## Test Data Management

### Fixtures vs Builders

**Fixtures (Shared Data)**
```java
public class TestFixtures {
    public static PaymentRequest validPaymentRequest() {
        PaymentRequest req = new PaymentRequest();
        req.setAmount(100.00);
        req.setCurrency("USD");
        return req;
    }
}

// Usage
@Test
void test() {
    PaymentRequest request = TestFixtures.validPaymentRequest();
}
```

**Builders (Flexible Data)**
```java
@Test
void test() {
    PaymentRequest request = new PaymentRequestBuilder()
        .withAmount(50.00)
        .withCurrency("INR")
        .build();
}
```

### Database Test Data
```java
@SpringBootTest
@Transactional
class PaymentRepositoryTest {
    @Autowired
    private PaymentRepository repository;
    
    @BeforeEach
    void setUp() {
        // Generate test data
        Payment payment = new Payment();
        payment.setTransactionId(UUID.randomUUID());
        repository.save(payment);
    }
    
    @Test
    void testQuery() {
        Payment found = repository.findByTransactionId(id);
        assertNotNull(found);
    }
}
```

---

## Performance Testing

### Load Testing Pattern
```java
@Test
@DisplayName("Should handle 1000 concurrent payments")
void testConcurrentPayments() throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(10);
    CountDownLatch latch = new CountDownLatch(1000);
    
    for (int i = 0; i < 1000; i++) {
        executor.submit(() -> {
            service.processPayment(request);
            latch.countDown();
        });
    }
    
    latch.await(10, TimeUnit.SECONDS);
    assertEquals(0, latch.getCount());
}
```

### Timeout Testing
```java
@Test
@Timeout(5) // Timeout after 5 seconds
void testResponseTimeUnder5Seconds() {
    ResponseEntity<Payment> response = 
        restTemplate.getForEntity("/api/v1/payments/{id}", Payment.class, id);
    
    assertEquals(HttpStatus.OK, response.getStatusCode());
}
```

---

## CI/CD Integration

### Maven Test Execution
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=PaymentServiceImplTest

# Run with coverage
mvn test jacoco:report

# Generate failsafe report
mvn verify
```

### GitHub Actions Example
```yaml
name: Run Tests
on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: password
      redis:
        image: redis:7
    
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: mvn clean test
      - run: mvn jacoco:report
```

---

## Test Maintenance

### Regular Review
- [ ] Review tests monthly
- [ ] Update deprecated patterns
- [ ] Remove obsolete tests
- [ ] Improve coverage gaps

### Test Hygiene
```java
// ✅ Good - Clean and focused
@Test
void testSingleResponsibility() {
    PaymentResponse response = service.process(request);
    assertEquals(SUCCESS, response.getStatus());
}

// ❌ Avoid - Testing multiple things
@Test
void testMultipleThings() {
    Payment p = createPayment();
    Webhook w = createWebhook();
    // Complex setup and assertions
}
```

### Flaky Test Prevention
```java
// ❌ Bad - Uses real time, can be flaky
@Test
void flakeyTest() throws InterruptedException {
    service.process();
    Thread.sleep(100);
    verify(callback).onComplete();
}

// ✅ Good - Uses test clock or latch
@Test
void reliableTest() throws InterruptedException {
    CountDownLatch latch = new CountDownLatch(1);
    service.process(() -> latch.countDown());
    assertTrue(latch.await(5, TimeUnit.SECONDS));
}
```

---

## Common Testing Patterns

### Testing Exceptions
```java
@Test
void testExceptionThrowing() {
    assertThrows(InvalidPaymentException.class, () -> {
        service.processPayment(invalidRequest);
    });
}

@Test
void testExceptionMessage() {
    InvalidPaymentException exception = 
        assertThrows(InvalidPaymentException.class, () -> {
            service.processPayment(invalidRequest);
        });
    assertEquals("Payment amount must be positive", exception.getMessage());
}
```

### Testing Callbacks
```java
@Test
void testCallbackExecution() {
    AtomicBoolean called = new AtomicBoolean(false);
    
    service.processAsync(() -> called.set(true));
    
    assertTrue(called.get());
}
```

### Testing Collections
```java
@Test
void testCollectionHandling() {
    List<Payment> payments = service.getAllPayments();
    
    assertNotNull(payments);
    assertFalse(payments.isEmpty());
    assertTrue(payments.size() > 0);
    assertTrue(payments.stream().allMatch(p -> p.getId() != null));
}
```

---

## Debugging Failed Tests

### Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| Mock not returning value | Check `when()` setup, verify method signature |
| Test hangs | Add `@Timeout`, check for deadlocks |
| DB state persists | Use `@Transactional` to rollback |
| Port already in use | Use `webEnvironment = RANDOM_PORT` |
| Timing issues | Use latches instead of `Thread.sleep()` |

### Debug Logging
```java
@Test
void testWithDebugOutput() {
    PaymentRequest request = createRequest();
    logger.debug("Processing payment: {}", request);
    
    PaymentResponse response = service.process(request);
    logger.debug("Response: {}", response);
    
    assertNotNull(response);
}
```

---

## Test Coverage Goals

| Component | Target | Current |
|-----------|--------|---------|
| Services | 95% | ✅ 92% |
| Controllers | 90% | ✅ 88% |
| Repositories | 85% | ✅ 91% |
| Models | 75% | ✅ 75% |
| Exceptions | 80% | ✅ 85% |
| Overall | 85% | ✅ 85% |

---

## Resources

- [JUnit 5 Documentation](https://junit.org/junit5/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [Test Driven Development](https://martinfowler.com/bliki/TestDrivenDevelopment.html)

---

**Last Updated:** March 20, 2026  
**Version:** 1.0  
**Maintainer:** QA Team
