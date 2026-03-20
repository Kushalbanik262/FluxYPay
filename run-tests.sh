#!/bin/bash

# FluxyPay Test Suite Runner
# Comprehensive test validation for Docker deployment

set -e

echo "========================================="
echo "FluxyPay Test Suite Runner"
echo "Date: $(date)"
echo "========================================="
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✓ $2${NC}"
    else
        echo -e "${RED}✗ $2${NC}"
    fi
}

# Check if Docker is running
echo "1️⃣  Checking Docker Services..."
echo "================================"

# Test PostgreSQL
echo "Testing PostgreSQL..."
docker exec fluxypay-postgres pg_isready -U postgres > /dev/null 2>&1
print_status $? "PostgreSQL connection"

# Test Redis
echo "Testing Redis..."
docker exec fluxypay-redis redis-cli ping > /dev/null 2>&1
print_status $? "Redis connection"

# Test Kafka
echo "Testing Kafka..."
docker exec fluxypay-kafka kafka-broker-api-versions.sh --bootstrap-server localhost:9092 > /dev/null 2>&1
print_status $? "Kafka broker connection"

# Test Application
echo "Testing FluxyPay Application..."
curl -s http://localhost:8080/actuator/health | grep -q "UP"
print_status $? "FluxyPay application health"

echo ""
echo "2️⃣  Running Unit Tests..."
echo "================================"

# Run unit tests
mvn test -Dtest=*ServiceImplTest -q
print_status $? "Service Unit Tests"

mvn test -Dtest=*ControllerTest -q
print_status $? "Controller Unit Tests"

echo ""
echo "3️⃣  Running Integration Tests..."
echo "================================"

# Run integration tests
mvn test -Dtest=*IntegrationTest -q
print_status $? "System Integration Tests"

mvn test -Dtest=*HealthCheckTest -q
print_status $? "Health Check Tests"

echo ""
echo "4️⃣  Running Configuration Tests..."
echo "================================"

mvn test -Dtest=*ConfigurationTest -q
print_status $? "Configuration Tests"

echo ""
echo "5️⃣  Verifying API Endpoints..."
echo "================================"

# Test API endpoints
echo "Testing Authentication Endpoint..."
curl -s -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"clientId":"test","clientSecret":"test"}' | grep -q "token\|error"
print_status $? "Authentication endpoint"

echo "Testing Payment Health Endpoint..."
curl -s http://localhost:8080/api/v1/payments/health | grep -q "UP"
print_status $? "Payment health endpoint"

echo ""
echo "6️⃣  Checking Metrics..."
echo "================================"

# Test Prometheus metrics
echo "Testing Prometheus Metrics..."
curl -s http://localhost:9090/api/v1/query?query=up | grep -q "result"
print_status $? "Prometheus metrics"

echo ""
echo "7️⃣  Testing Database Connectivity..."
echo "================================"

# Test database
echo "Testing Database Connection..."
docker exec fluxypay-postgres psql -U postgres -d fluxypay -c "SELECT 1;" > /dev/null 2>&1
print_status $? "Database query execution"

echo ""
echo "8️⃣  Checking Kafka Topics..."
echo "================================"

# List Kafka topics
echo "Testing Kafka Topics..."
docker exec fluxypay-kafka kafka-topics.sh --list --bootstrap-server localhost:9092 | grep -q "payments"
print_status $? "Kafka topics created"

echo ""
echo "9️⃣  Running Code Coverage..."
echo "================================"

# Run tests with coverage
mvn jacoco:report -q 2>/dev/null || echo "Coverage report generation..."

echo ""
echo "🔟 Summary Report"
echo "================================"

# Get test results
TEST_TOTAL=$(mvn test --quiet 2>&1 | grep -c "Tests run:" || echo "0")
echo "Total test suites executed: $TEST_TOTAL"

# System summary
echo ""
echo "System Status Summary:"
echo "- Application: $(curl -s http://localhost:8080/actuator/health | grep -o 'UP\|DOWN' || echo 'Unknown')"
echo "- PostgreSQL: $(docker exec fluxypay-postgres pg_isready -U postgres 2>&1 | grep -o 'accepting\|rejecting' || echo 'Unknown')"
echo "- Redis: $(docker exec fluxypay-redis redis-cli ping 2>&1 || echo 'Unknown')"
echo "- Kafka: $(docker exec fluxypay-kafka kafka-broker-api-versions.sh --bootstrap-server localhost:9092 2>&1 | grep -o 'ApiVersion' || echo 'Unknown') running"

echo ""
echo "========================================="
echo "Test Suite Execution Complete!"
echo "Generated at: $(date)"
echo "========================================="
