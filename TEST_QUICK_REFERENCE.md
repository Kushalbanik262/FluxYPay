# FluxyPay Test Execution Quick Reference

## Quick Start

### Run All Tests
```bash
mvn test
```

### Run Tests with Coverage Report
```bash
mvn test jacoco:report
# Report available at: target/site/jacoco/index.html
```

### Run All Tests with Docker Validation
```bash
bash ./run-tests.sh
```

---

## Running Specific Tests

### By Test Class Name
```bash
# Run single test class
mvn test -Dtest=PaymentServiceImplTest

# Run multiple test classes
mvn test -Dtest=PaymentServiceImplTest,AuthenticationServiceImplTest

# Run all tests matching pattern
mvn test -Dtest=*ServiceImplTest
```

### By Test Method Name
```bash
# Run single test method
mvn test -Dtest=PaymentServiceImplTest#testProcessPaymentSuccess

# Run multiple methods from same class
mvn test -Dtest=PaymentServiceImplTest#testProcessPaymentSuccess+testRefundPaymentSuccess
```

### By Test Category
```bash
# Unit Tests Only
mvn test -Dtest=*ServiceImplTest

# Integration Tests Only
mvn test -Dtest=*ControllerTest,*IntegrationTest

# Docker System Tests Only
mvn test -Dtest=DockerSystemValidationTest

# Health Checks Only
mvn test -Dtest=*HealthCheckTest

# Configuration Tests Only
mvn test -Dtest=ConfigurationTest
```

---

## Advanced Maven Commands

### Skip Tests During Build
```bash
mvn clean install -DskipTests
```

### Run Tests in Parallel
```bash
mvn test -T 1C  # 1 thread per core
mvn test -T 2   # 2 threads
mvn test -T 1   # Single thread
```

### Run Tests with Different Logging Levels
```bash
# Debug logging
mvn test -X

# Info logging
mvn test

# Quiet mode
mvn test -q
```

### Generate Test Report
```bash
# Surefire Report
mvn surefire-report:report
# Report available at: target/site/surefire-report.html

# Failsafe Report (Integration Tests)
mvn verify failsafe:integration-test failsafe:verify
```

### Run Only Failed Tests
```bash
mvn test --rf :PaymentGateway
```

---

## Docker Test Environments

### Prerequisites
- Docker and Docker Compose installed
- Containers running: `docker-compose up -d`

### Verify Docker Services
```bash
# Check all services are running
docker-compose ps

# Check PostgreSQL
docker exec fluxypay-postgres pg_isready

# Check Redis
docker exec fluxypay-redis redis-cli ping

# Check Kafka
docker exec fluxypay-kafka kafka-broker-api-versions
```

### Run Full Docker Environment Tests
```bash
# Comprehensive test suite with Docker validation
bash ./run-tests.sh

# Or manually:
mvn test -Dtest=DockerSystemValidationTest
```

---

## Test Profiles

### Run with Spring Test Profile
```bash
# Use test profile
mvn test -Dspring.profiles.active=test

# Use different application properties
mvn test -Dspring.config.location=classpath:application-test.yml
```

### Database Configuration for Tests
```bash
# Use H2 in-memory for faster tests
mvn test -Dspring.datasource.url=jdbc:h2:mem:testdb

# Use actual PostgreSQL
mvn test -Dspring.datasource.url=jdbc:postgresql://localhost:5432/fluxypay_test
```

---

## Code Coverage Reports

### Generate Coverage Report
```bash
mvn clean test jacoco:report
```

### View Coverage Report
```bash
# On Linux/Mac
open target/site/jacoco/index.html

# On Windows
start target/site/jacoco/index.html

# Or use web server
python -m http.server 8000 --directory target/site/jacoco
# Visit: http://localhost:8000
```

### Coverage Limits
```bash
# Fail build if coverage below 80%
mvn test jacoco:report jacoco:check \
  -Djacoco.haltOnFailure=true \
  -Djacoco.rules=[{element:PACKAGE,includes:[com.payment.*],limit:{counter:LINE,value:COVEREDRATIO,minimum:0.80}}]
```

---

## Continuous Integration Commands

### Jenkins Pipeline
```bash
# Build and test
mvn clean verify

# With code coverage
mvn clean verify jacoco:report sonar:sonar

# With performance report
mvn clean test jmeter:gui
```

### GitLab CI
```yaml
test:
  stage: test
  script:
    - mvn clean test jacoco:report
  artifacts:
    reports:
      junit:
        - target/surefire-reports/**/*.xml
    coverage_report:
      coverage_format: cobertura
      path: target/site/jacoco/jacoco.xml
```

### GitHub Actions
```yaml
- name: Run tests
  run: mvn clean test jacoco:report

- name: Upload coverage
  uses: codecov/codecov-action@v2
  with:
    files: ./target/site/jacoco/jacoco.xml
```

---

## Performance & Load Testing

### Run Performance Tests
```bash
# With increased heap size
mvn test -Dorg.slf4j.simpleLogger.defaultLogLevel=error -vm-Xmx1024m

# Custom timeout
mvn test -Dsurefire.timeout=600

# Fail slow tests
mvn test -Dgroups=slow -DexcludedGroups=fast
```

### Load Testing Example
```bash
# 100 concurrent test threads
mvn test -Dtest=SystemIntegrationTest \
  -Djmeter.threads=100 \
  -Djmeter.duration=60

# Generate load test report
jmeter -g target/jmeter.jtl -o target/jmeter-report
```

---

## Debugging Tests

### Run Single Test with Debug Output
```bash
mvn test -Dtest=PaymentServiceImplTest#testProcessPaymentSuccess -X
```

### Enable Debug Logging
```bash
mvn test -Dlogging.level.com.payment=DEBUG
```

### Run with IDE Debugger
```bash
# Using Maven Debug Mode
mvn -Dmaven.surefire.debug test
# Connect IDE debugger to localhost:5005

# IntelliJ Run → Edit Configurations → Debug
# Right-click test class → Debug 'TestClassName'
```

### Print Test Execution Details
```bash
mvn test -DargLine="-Dorg.slf4j.simpleLogger.defaultLogLevel=debug"
```

---

## Common Test Scenarios

### Scenario 1: Development - Run Only Modified Tests
```bash
# After making changes, run quickly
mvn test -DflippedClassNames=true -q
```

### Scenario 2: Pre-Commit - Full Suite
```bash
# Comprehensive check before commit
mvn clean verify jacoco:report
```

### Scenario 3: Merge Request - Specific Tests
```bash
# Test your changes
mvn test -Dtest=*ServiceImplTest,*ControllerTest
```

### Scenario 4: CI/CD Pipeline - Full Coverage
```bash
# Complete validation
bash ./run-tests.sh && mvn jacoco:report
```

### Scenario 5: Nightly Build - All Tests + Reports
```bash
#!/bin/bash
set -e

echo "Starting nightly test build..."
mvn clean verify jacoco:report
mvn surefire-report:report
echo "Uploading reports..."
# Upload to S3/artifact storage
echo "Build complete!"
```

---

## Troubleshooting

### Port Already in Use
```bash
# Find and kill process on port 8080
lsof -ti:8080 | xargs kill -9

# Or use random port in tests
mvn test -Dserver.port=0
```

### Database Connection Issues
```bash
# Verify PostgreSQL is running
docker exec fluxypay-postgres psql -U fluxypay -d fluxypay -c "SELECT 1"

# Check connection string
mvn test -Dspring.datasource.url=jdbc:postgresql://localhost:5432/fluxypay_test
```

### Kafka Connection Issues
```bash
# Check Kafka broker
docker exec fluxypay-kafka kafka-broker-api-versions --bootstrap-server localhost:9092

# Check topics
docker exec fluxypay-kafka kafka-topics --list --bootstrap-server localhost:9092
```

### Redis Connection Issues
```bash
# Verify Redis
docker exec fluxypay-redis redis-cli ping

# Check Redis memory
docker exec fluxypay-redis redis-cli INFO memory
```

### Tests Hang
```bash
# Add timeout to all tests
mvn test -Dsurefire.timeout=300 -Dtest=*Test

# Kill hanging tests after 10 minutes
timeout 600 mvn test
```

---

## Test Reporting Commands

### Generate HTML Health Report
```bash
mvn test-compile surefire:test surefire-report:report aggregate-report:aggregate
# Report at: target/site/surefire-report.html
```

### Generate PlantUML Diagrams
```bash
# Install diagrams
mvn dependency:tree > dependency-tree.txt

# Convert to visual
mvn doxia-module-markdown:parse
```

### Generate JSON Test Results
```bash
mvn test -DreportFormat=json -DreportOutputDirectory=target/test-results
```

---

## Environment Variables

### Override Test Configuration
```bash
# PostgreSQL
export POSTGRES_HOST=localhost
export POSTGRES_PORT=5432
export POSTGRES_DB=fluxypay_test

# Redis
export REDIS_HOST=localhost
export REDIS_PORT=6379

# Kafka
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# Run tests
mvn test
```

### Set JVM Heap Size
```bash
export MAVEN_OPTS="-Xmx2G -Xms1G"
mvn test
```

---

## Quick Reference Table

| Task | Command |
|------|---------|
| Run all tests | `mvn test` |
| Run with coverage | `mvn test jacoco:report` |
| Run service tests | `mvn test -Dtest=*ServiceImplTest` |
| Run controller tests | `mvn test -Dtest=*ControllerTest` |
| Run integration tests | `mvn test -Dtest=*IntegrationTest` |
| Run Docker validation | `mvn test -Dtest=DockerSystemValidationTest` |
| Run single test | `mvn test -Dtest=PaymentServiceImplTest#testName` |
| Run with debug | `mvn test -X` |
| Skip tests | `mvn install -DskipTests` |
| Clean and rebuild | `mvn clean verify` |
| Generate reports | `mvn surefire-report:report` |
| Run full suite + validation | `bash ./run-tests.sh` |

---

## Tips & Best Practices

✅ **Do:**
- Run tests before committing
- Keep tests fast (<100ms each)
- Run full suite regularly
- Keep test data small
- Use meaningful test names

❌ **Don't:**
- Use `Thread.sleep()` in tests
- Ignore failing tests
- Depend on execution order
- Create external resources
- Mock everything

---

## Emergency Commands

### Reset Everything
```bash
# Clean all test artifacts
mvn clean

# Remove Docker containers
docker-compose down -v

# Clear Maven cache
rm -rf ~/.m2/repository

# Start fresh
docker-compose up -d
mvn clean test
```

### Quick Validation Check
```bash
# 1-minute validation
mvn test -DflippedClassNames=true -q

# Or
bash run-tests.sh 2>&1 | tail -20
```

---

**Last Updated:** March 20, 2026  
**Version:** 1.0
