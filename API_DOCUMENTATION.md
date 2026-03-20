# FluxyPay API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Table of Contents
1. [Authentication](#authentication)
2. [Payment Operations](#payment-operations)
3. [Transaction Queries](#transaction-queries)
4. [Webhook Management](#webhook-management)
5. [Error Handling](#error-handling)

---

## Authentication

### Login

**POST** `/auth/login`

Authenticate client and obtain JWT token.

**Request:**
```json
{
  "clientId": "client_001",
  "clientSecret": "your_client_secret"
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400,
  "tokenType": "Bearer",
  "clientId": "client_001"
}
```

### Refresh Token

**POST** `/auth/refresh`

Refresh JWT token using refresh token.

**Request:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "expiresIn": 86400,
  "tokenType": "Bearer",
  "clientId": "client_001"
}
```

### Validate Token

**POST** `/auth/validate`

Validate if JWT token is still valid.

**Request:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response (200 OK):**
```json
{
  "valid": true,
  "timestamp": "2025-06-20T10:30:45Z"
}
```

---

## Payment Operations

### Initiate Payment

**POST** `/payments/initiate`

Start a new payment transaction with idempotency support.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>` (Required)
- `Idempotency-Key: <UNIQUE_VALUE>` (Required)
- `Content-Type: application/json`

**Request:**
```json
{
  "amount": 9999,
  "currency": "USD",
  "paymentMethod": "CARD",
  "cardDetails": {
    "cardNumber": "4111111111111111",
    "expiryMonth": "12",
    "expiryYear": "2025",
    "cvv": "123",
    "holderName": "John Doe"
  },
  "merchantId": "MERCHANT_001",
  "webhookUrl": "https://your-domain.com/webhook",
  "metadata": {
    "orderId": "ORDER_12345",
    "customerId": "CUST_789"
  }
}
```

**Response (201 Created):**
```json
{
  "transactionId": "txn_1abc2def3ghi4jkl",
  "status": "COMPLETED",
  "amount": 9999,
  "currency": "USD",
  "createdAt": "2025-06-20T10:30:45Z",
  "completedAt": "2025-06-20T10:30:55Z",
  "paymentMethod": "CARD",
  "lastCardDigits": "1111",
  "referenceId": "ref_abc123xyz",
  "message": "Payment processed successfully"
}
```

### Get Transaction Status

**GET** `/payments/{transactionId}`

Retrieve current transaction status and details.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>` (Required)

**Response (200 OK):**
```json
{
  "transactionId": "txn_1abc2def3ghi4jkl",
  "status": "COMPLETED",
  "amount": 9999,
  "currency": "USD",
  "createdAt": "2025-06-20T10:30:45Z",
  "updatedAt": "2025-06-20T10:30:55Z",
  "retryCount": 0,
  "failureReason": null
}
```

### Refund Payment

**POST** `/payments/{transactionId}/refund`

Initiate refund for a completed payment.

**Headers:**
- `Authorization: Bearer <JWT_TOKEN>` (Required)
- `Idempotency-Key: <UNIQUE_VALUE>` (Required)

**Request:**
```json
{
  "amount": 9999,
  "reason": "Customer request",
  "metadata": {
    "refundReason": "NOT_APPLICABLE"
  }
}
```

**Response (202 Accepted):**
```json
{
  "transactionId": "txn_1abc2def3ghi4jkl",
  "status": "REFUNDED",
  "amount": 9999,
  "message": "Refund processed successfully"
}
```

---

## Webhook Management

### Register Webhook

**POST** `/webhooks/register?clientId=CLIENT_001&webhookUrl=https://your-domain.com/webhook`

Register webhook endpoint for payment notifications.

**Response (201 Created):**
```json
{
  "status": "REGISTERED",
  "endpointId": "endpoint_abc123xyz",
  "webhookUrl": "https://your-domain.com/webhook"
}
```

### Get Webhook Status

**GET** `/webhooks/{webhookEventId}/status`

Check delivery status of webhook event.

**Response (200 OK):**
```json
{
  "webhookEventId": "evt_1234567890",
  "status": "DELIVERED",
  "retryCount": 0,
  "lastRetryTime": null,
  "nextRetryTime": null
}
```

### Retry Webhook

**POST** `/webhooks/{webhookEventId}/retry`

Manually retry failed webhook delivery.

**Response (202 Accepted):**
```json
{
  "status": "RETRY_QUEUED",
  "webhookEventId": "evt_1234567890",
  "message": "Webhook delivery queued for retry"
}
```

---

## Error Handling

### Error Response Format

All errors follow this format:

```json
{
  "timestamp": "2025-06-20T10:30:45Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid request parameters",
  "path": "/api/v1/payments/initiate",
  "validationErrors": {
    "amount": "Amount must be greater than 0"
  }
}
```

### Common Error Codes

| Status | Error | Reason |
|--------|-------|--------|
| 400 | Bad Request | Invalid request parameters or validation failed |
| 401 | Unauthorized | Missing or invalid JWT token |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Idempotency violation or duplicate request |
| 429 | Too Many Requests | Rate limit exceeded |
| 500 | Internal Server Error | Server processing error |
| 503 | Service Unavailable | Service temporarily unavailable |

---

## Rate Limiting

Rate limits are applied per client ID:

- **Requests per minute:** 1000
- **Requests per hour:** 50000

When rate limit is exceeded, the API returns `429 Too Many Requests`:

```json
{
  "timestamp": "2025-06-20T10:30:45Z",
  "status": 429,
  "error": "Too Many Requests",
  "message": "Rate limit exceeded. Maximum 1000 requests per minute allowed",
  "path": "/api/v1/payments/initiate"
}
```

---

## Webhook Events

### Webhook Event Payload

```json
{
  "eventId": "evt_1234567890",
  "eventType": "payment.completed",
  "timestamp": "2025-06-20T10:30:55Z",
  "transactionId": "txn_1abc2def3ghi4jkl",
  "amount": 9999,
  "currency": "USD",
  "status": "COMPLETED",
  "paymentNetwork": "VISA",
  "metadata": {
    "merchantId": "MERCHANT_001",
    "orderId": "ORDER_12345"
  }
}
```

### Event Types

- `payment.initiated` - Payment processing started
- `payment.completed` - Payment successfully completed
- `payment.failed` - Payment processing failed
- `refund.initiated` - Refund processing started
- `refund.completed` - Refund successfully completed
- `webhook.retry` - Webhook delivery retry attempt

### Webhook Signature

All webhooks include signature verification:

**Header:** `X-Webhook-Signature: sha256=<BASE64_ENCODED_SIGNATURE>`

**Verification:**
```
HMAC-SHA256(payload, client_secret) = signature
```

---

## Examples

### Complete Payment Flow

```bash
# 1. Authenticate
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "clientId": "client_001",
    "clientSecret": "secret_key"
  }'

# Response: { "token": "...", "refreshToken": "...", "expiresIn": 86400 }

# 2. Initiate Payment
curl -X POST http://localhost:8080/api/v1/payments/initiate \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: payment_12345" \
  -d '{
    "amount": 9999,
    "currency": "USD",
    "paymentMethod": "CARD",
    "cardDetails": {
      "cardNumber": "4111111111111111",
      "expiryMonth": "12",
      "expiryYear": "2025",
      "cvv": "123"
    },
    "merchantId": "MERCHANT_001",
    "webhookUrl": "https://your-domain.com/webhook"
  }'

# Response: { "transactionId": "txn_...", "status": "COMPLETED", ... }

# 3. Check Status
curl -X GET http://localhost:8080/api/v1/payments/txn_1abc2def3ghi4jkl \
  -H "Authorization: Bearer <JWT_TOKEN>"

# 4. Process Refund (if needed)
curl -X POST http://localhost:8080/api/v1/payments/txn_1abc2def3ghi4jkl/refund \
  -H "Authorization: Bearer <JWT_TOKEN>" \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: refund_12345" \
  -d '{
    "amount": 9999,
    "reason": "Customer request"
  }'
```

---

## Best Practices

1. **Always use Idempotency-Key:** Prevents duplicate charges on retries
2. **Implement exponential backoff:** When retrying failed requests
3. **Verify webhook signatures:** Ensure webhook authenticity
4. **Use HTTPS:** Always communicate over encrypted connections
5. **Store secrets securely:** Never hardcode API keys or secrets
6. **Log transactions:** Maintain audit trail for compliance
7. **Monitor rate limits:** Track usage to stay within limits
8. **Implement timeout handling:** Have proper timeout strategies

---

For more information, visit: https://docs.fluxypay.io
