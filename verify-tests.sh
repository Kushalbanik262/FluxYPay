#!/bin/bash

#################################################################################
# FluxyPay Automated Test Verification Script
# Purpose: Verify test suite integrity and Docker system health before execution
# Version: 1.0
#################################################################################

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Counters
PASSED=0
FAILED=0
WARNINGS=0

#################################################################################
# UTILITY FUNCTIONS
#################################################################################

print_header() {
    echo -e "\n${BLUE}═══════════════════════════════════════════════════════════${NC}"
    echo -e "${BLUE}  $1${NC}"
    echo -e "${BLUE}═══════════════════════════════════════════════════════════${NC}\n"
}

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
    ((PASSED++))
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
    ((FAILED++))
}

print_warning() {
    echo -e "${YELLOW}⚠ $1${NC}"
    ((WARNINGS++))
}

check_command_exists() {
    if command -v $1 &> /dev/null; then
        print_success "$1 is installed"
        return 0
    else
        print_error "$1 is NOT installed"
        return 1
    fi
}

check_port_open() {
    if timeout 2 bash -c "echo >/dev/tcp/localhost/$1" 2>/dev/null; then
        print_success "Port $1 is open"
        return 0
    else
        print_error "Port $1 is NOT responding"
        return 1
    fi
}

#################################################################################
# VERIFICATION FUNCTIONS
#################################################################################

verify_prerequisites() {
    print_header "PREREQUISITE CHECKS"
    
    check_command_exists "java"
    check_command_exists "mvn"
    check_command_exists "docker"
    check_command_exists "docker-compose"
    
    # Check Java version
    JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[\d.]+')
    if [[ "$JAVA_VERSION" =~ ^17 ]] || [[ "$JAVA_VERSION" =~ ^18 ]] || [[ "$JAVA_VERSION" =~ ^19 ]] || [[ "$JAVA_VERSION" =~ ^20 ]]; then
        print_success "Java version $JAVA_VERSION is compatible (17+)"
    else
        print_warning "Java version $JAVA_VERSION may not be compatible (17+ recommended)"
    fi
}

verify_project_structure() {
    print_header "PROJECT STRUCTURE VERIFICATION"
    
    # Check pom.xml
    if [ -f "pom.xml" ]; then
        print_success "pom.xml exists"
    else
        print_error "pom.xml NOT found"
    fi
    
    # Check test directory
    if [ -d "src/test/java" ]; then
        print_success "Test directory exists (src/test/java)"
    else
        print_error "Test directory NOT found"
    fi
    
    # Count test files
    TEST_FILES=$(find src/test/java -name "*Test.java" -type f | wc -l)
    if [ "$TEST_FILES" -gt 0 ]; then
        print_success "Found $TEST_FILES test files"
    else
        print_error "No test files found"
    fi
}

verify_test_files() {
    print_header "TEST FILES VERIFICATION"
    
    local test_classes=(
        "src/test/java/com/payment/PaymentGateway/Services/Implementations/AuthenticationServiceImplTest.java"
        "src/test/java/com/payment/PaymentGateway/Services/Implementations/PaymentServiceImplTest.java"
        "src/test/java/com/payment/PaymentGateway/Services/Implementations/WebhookServiceImplTest.java"
        "src/test/java/com/payment/PaymentGateway/Controller/AuthenticationControllerTest.java"
        "src/test/java/com/payment/PaymentGateway/Controller/PaymentControllerTest.java"
        "src/test/java/com/payment/PaymentGateway/Integration/SystemIntegrationTest.java"
        "src/test/java/com/payment/PaymentGateway/Integration/SystemHealthCheckTest.java"
        "src/test/java/com/payment/PaymentGateway/Integration/DockerSystemValidationTest.java"
        "src/test/java/com/payment/PaymentGateway/Config/ConfigurationTest.java"
    )
    
    for test_class in "${test_classes[@]}"; do
        if [ -f "$test_class" ]; then
            print_success "$(basename $test_class) exists"
        else
            print_error "$(basename $test_class) NOT found"
        fi
    done
}

verify_docker_compose() {
    print_header "DOCKER COMPOSE VERIFICATION"
    
    if [ -f "docker-compose.yaml" ]; then
        print_success "docker-compose.yaml exists"
    else
        print_error "docker-compose.yaml NOT found"
    fi
    
    # Check if services are running
    echo -e "\nChecking Docker services status:"
    
    local services=("fluxypay-postgres" "fluxypay-redis" "fluxypay-kafka" "fluxypay-zookeeper" "fluxypay" "prometheus" "grafana")
    
    for service in "${services[@]}"; do
        if docker ps --format '{{.Names}}' | grep -q "$service"; then
            print_success "$service is running"
        else
            print_warning "$service is NOT running"
        fi
    done
}

verify_database_connectivity() {
    print_header "DATABASE CONNECTIVITY VERIFICATION"
    
    if check_port_open 5432; then
        # Try to connect to PostgreSQL
        if docker exec fluxypay-postgres pg_isready -U fluxypay -q 2>/dev/null; then
            print_success "PostgreSQL is accessible"
        else
            print_warning "PostgreSQL port open but not responding to queries"
        fi
    fi
}

verify_redis_connectivity() {
    print_header "REDIS CONNECTIVITY VERIFICATION"
    
    if check_port_open 6379; then
        # Try Redis PING
        if docker exec fluxypay-redis redis-cli ping 2>/dev/null | grep -q "PONG"; then
            print_success "Redis is responding to PING"
        else
            print_warning "Redis port open but not responding"
        fi
    fi
}

verify_kafka_connectivity() {
    print_header "KAFKA CONNECTIVITY VERIFICATION"
    
    if check_port_open 9092; then
        print_success "Kafka broker port 9092 is open"
        
        # Check Zookeeper
        if check_port_open 2181; then
            print_success "Zookeeper port 2181 is open"
        fi
    else
        print_warning "Kafka is not accessible on port 9092"
    fi
}

verify_application() {
    print_header "APPLICATION VERIFICATION"
    
    if check_port_open 8080; then
        # Try to connect to health endpoint
        if curl -s -f http://localhost:8080/actuator/health > /dev/null 2>&1; then
            print_success "Application health endpoint responding"
        else
            print_warning "Port 8080 open but health endpoint not responding"
        fi
    else
        print_warning "Application port 8080 not open (start Docker with: docker-compose up -d)"
    fi
}

verify_maven_dependencies() {
    print_header "MAVEN DEPENDENCIES VERIFICATION"
    
    echo "Checking Maven dependencies can be resolved..."
    
    if mvn -q dependency:resolve 2>&1 > /tmp/maven_check.log; then
        print_success "All Maven dependencies resolved"
    else
        print_warning "Some Maven dependencies may not be resolved"
        tail -10 /tmp/maven_check.log
    fi
}

verify_test_compile() {
    print_header "TEST COMPILATION VERIFICATION"
    
    echo "Compiling test sources..."
    
    if mvn -q test-compile 2>&1 > /tmp/test_compile.log; then
        print_success "Test sources compiled successfully"
    else
        print_error "Test compilation failed"
        tail -20 /tmp/test_compile.log
    fi
}

estimate_test_count() {
    print_header "TEST METHOD ESTIMATION"
    
    local test_methods=$(grep -r "@Test" src/test/java --include="*.java" | wc -l)
    echo -e "Estimated test methods: ${BLUE}$test_methods${NC}"
    
    if [ "$test_methods" -ge 40 ]; then
        print_success "Found $test_methods test methods (40+ target reached)"
    else
        print_warning "Found $test_methods test methods (40+ target not yet reached)"
    fi
}

dry_run_tests() {
    print_header "DRY RUN TEST VALIDATION"
    
    echo "Performing dry run of test suite..."
    
    if mvn test -DryRun 2>&1 | grep -q "Tests run:"; then
        print_success "Dry run completed"
    else
        print_warning "Could not perform full dry run, continuing..."
    fi
}

#################################################################################
# SUMMARY AND RECOMMENDATIONS
#################################################################################

print_summary() {
    print_header "VERIFICATION SUMMARY"
    
    echo -e "Checks Passed:  ${GREEN}$PASSED${NC}"
    echo -e "Checks Failed:  ${RED}$FAILED${NC}"
    echo -e "Warnings:       ${YELLOW}$WARNINGS${NC}"
    
    if [ "$FAILED" -eq 0 ]; then
        echo -e "\n${GREEN}✓ All critical checks passed!${NC}"
        echo -e "${GREEN}✓ System is ready for test execution${NC}"
    else
        echo -e "\n${RED}✗ Some checks failed - review above${NC}"
    fi
}

print_recommendations() {
    print_header "RECOMMENDATIONS"
    
    if [ ! -f "docker-compose.yaml" ]; then
        echo -e "${YELLOW}1. Ensure docker-compose.yaml exists${NC}"
    fi
    
    if ! docker ps --format '{{.Names}}' | grep -q "fluxypay"; then
        echo -e "${YELLOW}2. Start Docker services:${NC}"
        echo -e "   ${BLUE}docker-compose up -d${NC}"
    fi
    
    if [ ! -f "src/test/java/com/payment/PaymentGateway/Integration/DockerSystemValidationTest.java" ]; then
        echo -e "${YELLOW}3. Ensure test files are in correct location${NC}"
    fi
    
    echo -e "\n${GREEN}Next Steps:${NC}"
    echo -e "1. ${BLUE}Run all tests:${NC}"
    echo -e "   ${BLUE}mvn test${NC}"
    echo -e "\n2. ${BLUE}Run with coverage:${NC}"
    echo -e "   ${BLUE}mvn test jacoco:report${NC}"
    echo -e "\n3. ${BLUE}Run specific test suite:${NC}"
    echo -e "   ${BLUE}mvn test -Dtest=PaymentServiceImplTest${NC}"
    echo -e "\n4. ${BLUE}Run full validation:${NC}"
    echo -e "   ${BLUE}bash ./run-tests.sh${NC}"
}

print_help() {
    echo -e "\n${BLUE}FluxyPay Test Verification Script${NC}"
    echo -e "Usage: bash verify-tests.sh [OPTIONS]\n"
    echo "Options:"
    echo "  --verify-all       Run all verification checks (default)"
    echo "  --check-ports      Only check port connectivity"
    echo "  --check-docker     Only verify Docker services"
    echo "  --compile          Only compile tests"
    echo "  --help             Show this help message"
}

#################################################################################
# MAIN EXECUTION
#################################################################################

main() {
    local start_time=$(date +%s)
    
    echo -e "${BLUE}"
    echo "╔═════════════════════════════════════════════════════════╗"
    echo "║   FluxyPay Automated Test Verification Script v1.0       ║"
    echo "║   Validating test suite and system readiness             ║"
    echo "╚═════════════════════════════════════════════════════════╝"
    echo -e "${NC}"
    
    # Default: run all checks
    if [ $# -eq 0 ]; then
        verify_prerequisites
        verify_project_structure
        verify_test_files
        verify_maven_dependencies
        verify_test_compile
        estimate_test_count
        verify_docker_compose
        verify_database_connectivity
        verify_redis_connectivity
        verify_kafka_connectivity
        verify_application
    else
        case "$1" in
            --verify-all)
                verify_prerequisites
                verify_project_structure
                verify_test_files
                verify_maven_dependencies
                verify_test_compile
                estimate_test_count
                verify_docker_compose
                verify_database_connectivity
                verify_redis_connectivity
                verify_kafka_connectivity
                verify_application
                ;;
            --check-ports)
                verify_database_connectivity
                verify_redis_connectivity
                verify_kafka_connectivity
                verify_application
                ;;
            --check-docker)
                verify_docker_compose
                verify_database_connectivity
                verify_redis_connectivity
                verify_kafka_connectivity
                ;;
            --compile)
                verify_maven_dependencies
                verify_test_compile
                ;;
            --help)
                print_help
                exit 0
                ;;
            *)
                print_error "Unknown option: $1"
                print_help
                exit 1
                ;;
        esac
    fi
    
    print_summary
    print_recommendations
    
    local end_time=$(date +%s)
    local duration=$((end_time - start_time))
    
    echo -e "\n${BLUE}Verification completed in ${duration}s${NC}\n"
    
    # Exit with error code if any checks failed
    if [ "$FAILED" -gt 0 ]; then
        exit 1
    fi
    
    exit 0
}

# Run main function
main "$@"
