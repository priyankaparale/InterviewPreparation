# Circuit Breaker Pattern 🔌

Prevents cascading failures by stopping requests to a failing service and allowing it time to recover.

---

## Why Do We Need Circuit Breaker?

### Problem: Cascading Failures
```
Service A → Service B (failing) → Service A waits → A's threads exhaust → A fails → C fails → D fails
```

### Solution: Circuit Breaker
```
Service A → Circuit Breaker → Service B (failing)
           ↓
        (Fails fast without waiting)
           ↓
        Service returns fallback response
```

---

## Three States of Circuit Breaker

### 1. **CLOSED** ✅ (Normal Operation)
```
Request incoming
         ↓
     [CLOSED]
         ↓
   Forward to Service
         ↓
Service responds (success)
         ↓
Failure counter reset
```
- All requests pass through to the service
- Failure counter starts at 0
- If failures < threshold: stay CLOSED

### 2. **OPEN** ❌ (Service Failing)
```
Failure threshold exceeded
         ↓
    [OPEN]
         ↓
Requests rejected immediately
(Without calling service)
         ↓
Return fallback/error response
         ↓
Wait for timeout period
```
- Requests fail immediately without calling service
- Prevents overwhelming failing service
- After timeout → Go to HALF_OPEN

### 3. **HALF_OPEN** ⚠️ (Testing Recovery)
```
Timeout period elapsed
         ↓
  [HALF_OPEN]
         ↓
Allow limited requests to test
         ↓
Service responds?
├─ Success (few requests)
│  └─→ CLOSED (recovered)
│
└─ Failure
   └─→ OPEN (still failing)
```
- Limited requests allowed to test if service recovered
- If successful: Back to CLOSED
- If fails: Back to OPEN

---

## State Transition Diagram

```
                    ┌────────────────┐
                    │    CLOSED      │
                    │ (Normal ops)   │
                    └────────┬───────┘
                             │
                   Failure count > threshold
                             │
                    ┌────────▼───────┐
                    │     OPEN       │
    ┌──────────────→│  (Rejecting)   │←──────────────┐
    │               └────────┬───────┘               │
    │                        │                       │
    │                    After timeout              Limited
    │                   (wait period)              requests fail
    │                        │
    │               ┌────────▼───────┐
    │               │  HALF_OPEN     │
    │               │(Testing)       │
    │               └────────┬───────┘
    │                        │
    │           Limited requests succeed
    │                        │
    └────────────────────────┘
```

---

## Example Implementation

### Using Resilience4j (Industry Standard)

```java
// Configuration
@Configuration
public class CircuitBreakerConfig {
    
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }
}

// Usage
@Service
public class PaymentService {
    
    private final CircuitBreaker circuitBreaker;
    private final PaymentClient paymentClient;
    
    public PaymentService(CircuitBreakerRegistry registry, PaymentClient client) {
        this.circuitBreaker = registry.circuitBreaker("paymentService");
        this.paymentClient = client;
    }
    
    // Call payment API with circuit breaker protection
    public PaymentResponse processPayment(PaymentRequest request) {
        try {
            return circuitBreaker.executeSupplier(
                () -> paymentClient.charge(request)
            );
        } catch (Exception e) {
            // Fallback: Return pending status
            return PaymentResponse.pending("Payment pending - service temporarily unavailable");
        }
    }
}
```

---

## Configuration Parameters

```yaml
resilience4j:
  circuitbreaker:
    instances:
      paymentService:
        # Failure threshold to open circuit (%)
        failureRateThreshold: 50
        
        # Minimum calls before calculating failure rate
        minimumNumberOfCalls: 5
        
        # Time to stay in OPEN state before trying HALF_OPEN
        waitDurationInOpenState: 30s
        
        # Calls allowed in HALF_OPEN state
        permittedNumberOfCallsInHalfOpenState: 3
        
        # Record these exceptions as failures
        recordExceptions:
          - java.net.SocketTimeoutException
          - java.io.IOException
```

---

## Real-World Scenario: Paytm-like Platform

```
User initiates payment
        ↓
Circuit Breaker checks Payment Service
        ├─ CLOSED: Forward to Payment Service
        │  ├─ Success: Return payment confirmation
        │  └─ Failure count increases
        │
        ├─ OPEN: Service is failing
        │  └─ Return: "Payment processing later, try again"
        │     (Don't overload failing service)
        │
        └─ HALF_OPEN: Testing if recovered
           ├─ Success: Reset to CLOSED
           └─ Failure: Back to OPEN
```

---

## Benefits ✅

1. **Prevents Cascading Failures**: Stop cascading errors
2. **Fast Failure**: Fails immediately instead of waiting for timeout
3. **Resource Protection**: Prevents resource exhaustion
4. **Service Recovery**: Allows time for service to recover
5. **Graceful Degradation**: Show fallback UI instead of errors
6. **Monitoring**: Track service health

---

## When to Use

✅ External service calls (Payment, Email, SMS APIs)  
✅ Database connections  
✅ Remote REST/SOAP calls  
✅ Third-party integrations  
✅ Any operation that could fail repeatedly  

---

## Common Mistakes ❌

1. ❌ Not configuring thresholds properly (too low/high)
2. ❌ Not providing fallback responses
3. ❌ Setting timeout too short
4. ❌ Not monitoring circuit breaker state changes
5. ❌ Using for local service failures (use retry instead)
