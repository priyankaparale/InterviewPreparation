package com.dipdeveloper.microservices;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.time.Duration;
import java.util.Random;

/**
 * Circuit Breaker Pattern Implementation
 *
 * Scenario: Payment Service API is failing intermittently
 * Solution: Implement circuit breaker to:
 *           - Stop sending requests to failing service
 *           - Return fallback response
 *           - Periodically test if service recovered
 */

// ============= Simulated External Service =============

/**
 * Simulates an external payment service that fails sometimes
 */
public class ExternalPaymentService {
    private static final Random random = new Random();
    private static int callCount = 0;

    /**
     * Simulate API call that fails 60% of the time
     */
    public static String chargePayment(double amount) throws RuntimeException {
        callCount++;

        // First 2 calls succeed, then 5 calls fail, then succeed again
        int failureWindow = (callCount - 1) / 2;

        if (failureWindow % 3 == 1) {  // Fail in certain windows
            throw new RuntimeException("Payment gateway timeout! (Call #" + callCount + ")");
        }

        System.out.println("[External Service] Successfully processed ₹" + amount + " (Call #" + callCount + ")");
        return "TXN" + System.currentTimeMillis();
    }
}

// ============= Fallback Service =============

/**
 * Fallback mechanism when circuit breaker opens
 */
public class PaymentFallbackService {

    /**
     * Return pending response when main service is down
     */
    public static PaymentResponse processFallback(double amount) {
        System.out.println("[FALLBACK] Main payment service is down. Queuing payment...");
        return new PaymentResponse(
            "PENDING",
            "Payment queued. Will process when service recovers.",
            amount,
            false
        );
    }
}

// ============= Response Object =============

/**
 * Payment response DTO
 */
public class PaymentResponse {
    private String status;      // SUCCESS, FAILED, PENDING
    private String message;
    private double amount;
    private boolean isSuccess;

    public PaymentResponse(String status, String message, double amount, boolean isSuccess) {
        this.status = status;
        this.message = message;
        this.amount = amount;
        this.isSuccess = isSuccess;
    }

    @Override
    public String toString() {
        return "PaymentResponse{" +
            "status='" + status + '\'' +
            ", message='" + message + '\'' +
            ", amount=" + amount +
            ", isSuccess=" + isSuccess +
            '}';
    }

    // Getters
    public String getStatus() { return status; }
    public String getMessage() { return message; }
    public double getAmount() { return amount; }
    public boolean isSuccess() { return isSuccess; }
}

// ============= Payment Service with Circuit Breaker =============

/**
 * Payment service with circuit breaker pattern
 */
public class PaymentServiceWithCircuitBreaker {

    // Create circuit breaker configuration
    private static final CircuitBreakerConfig config = CircuitBreakerConfig.custom()
        // Failure threshold: Open circuit if 50% calls fail
        .failureRateThreshold(50.0f)

        // Minimum number of calls before calculating failure rate
        .minimumNumberOfCalls(5)

        // Time to stay in OPEN state (wait before trying HALF_OPEN)
        .waitDurationInOpenState(Duration.ofSeconds(5))

        // How many calls allowed in HALF_OPEN state (to test recovery)
        .permittedNumberOfCallsInHalfOpenState(2)

        // What exceptions should be recorded as failures
        .recordExceptions(RuntimeException.class)
        .build();

    private CircuitBreaker circuitBreaker;

    public PaymentServiceWithCircuitBreaker() {
        // Create circuit breaker instance
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        this.circuitBreaker = registry.circuitBreaker("paymentService");

        // Add listener to track state changes
        addCircuitBreakerListener();
    }

    /**
     * Process payment with circuit breaker protection
     */
    public PaymentResponse processPayment(double amount, String orderId) {
        System.out.println("\n[Order: " + orderId + "] Processing ₹" + amount);
        System.out.println("[Circuit Breaker State] " + circuitBreaker.getState());

        try {
            // Execute payment within circuit breaker
            String transactionId = circuitBreaker.executeSupplier(() ->
                ExternalPaymentService.chargePayment(amount)
            );

            System.out.println("[SUCCESS] Transaction ID: " + transactionId);
            return new PaymentResponse("SUCCESS", "Payment successful", amount, true);

        } catch (Exception e) {
            System.out.println("[ERROR] " + e.getMessage());

            // If circuit is OPEN, return fallback response
            if (circuitBreaker.getState().toString().equals("OPEN")) {
                System.out.println("[CIRCUIT OPEN] Using fallback...");
                return PaymentFallbackService.processFallback(amount);
            }

            return new PaymentResponse("FAILED", e.getMessage(), amount, false);
        }
    }

    /**
     * Add listener to monitor circuit breaker state changes
     */
    private void addCircuitBreakerListener() {
        circuitBreaker.getEventPublisher()
            .onStateTransition(event -> {
                System.out.println("\n>>> [STATE TRANSITION] " +
                    event.getStateTransition().getFromState() + " -> " +
                    event.getStateTransition().getToState());
            });

        circuitBreaker.getEventPublisher()
            .onError(event -> {
                System.out.println(">>> [ERROR] Call failed: " + event.getThrowable().getMessage());
            });

        circuitBreaker.getEventPublisher()
            .onSuccess(event -> {
                System.out.println(">>> [SUCCESS] Call succeeded");
            });
    }

    /**
     * Get current circuit breaker metrics
     */
    public void printMetrics() {
        System.out.println("\n===== Circuit Breaker Metrics =====");
        System.out.println("State: " + circuitBreaker.getState());
        System.out.println("Total calls: " +
            circuitBreaker.getMetrics().getNumberOfNotPermittedCalls() +
            circuitBreaker.getMetrics().getNumberOfBufferedCalls());
        System.out.println("Successful calls: " +
            circuitBreaker.getMetrics().getNumberOfSuccessfulCalls());
        System.out.println("Failed calls: " +
            circuitBreaker.getMetrics().getNumberOfFailedCalls());
        System.out.println("Failure rate: " +
            circuitBreaker.getMetrics().getFailureRate() + "%");
        System.out.println("===================================\n");
    }
}

// ============= Demo: Circuit Breaker in Action =============

/**
 * Demonstrates circuit breaker pattern:
 * - CLOSED: Normal operation, forwarding requests
 * - OPEN: Failing, rejecting requests immediately
 * - HALF_OPEN: Testing if service recovered
 */
public class CircuitBreakerDemo {
    public static void main(String[] args) throws InterruptedException {
        PaymentServiceWithCircuitBreaker paymentService =
            new PaymentServiceWithCircuitBreaker();

        System.out.println("========================================");
        System.out.println("Circuit Breaker Pattern Demonstration");
        System.out.println("========================================");

        // Phase 1: CLOSED - All requests pass through
        System.out.println("\n[PHASE 1] Circuit Breaker: CLOSED (Normal operation)");
        System.out.println("─────────────────────────────────────────────");

        for (int i = 1; i <= 7; i++) {
            PaymentResponse response = paymentService.processPayment(1000 * i, "ORD-00" + i);
            System.out.println("Result: " + response);
            Thread.sleep(500);
        }

        paymentService.printMetrics();

        // Phase 2: OPEN - Requests rejected immediately (circuit opened due to failures)
        System.out.println("\n[PHASE 2] Circuit Breaker: OPEN (Too many failures!)");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("Attempting more requests while circuit is OPEN...");

        for (int i = 8; i <= 10; i++) {
            PaymentResponse response = paymentService.processPayment(1000 * i, "ORD-00" + i);
            System.out.println("Result: " + response);
            Thread.sleep(500);
        }

        paymentService.printMetrics();

        // Phase 3: Wait for timeout, then HALF_OPEN
        System.out.println("\n[PHASE 3] Waiting 6 seconds for circuit to attempt recovery...");
        Thread.sleep(6000);

        // Phase 4: HALF_OPEN - Limited requests to test recovery
        System.out.println("\n[PHASE 4] Circuit Breaker: HALF_OPEN (Testing recovery)");
        System.out.println("─────────────────────────────────────────────");

        for (int i = 11; i <= 15; i++) {
            PaymentResponse response = paymentService.processPayment(5000, "ORD-0" + i);
            System.out.println("Result: " + response);
            Thread.sleep(500);
        }

        paymentService.printMetrics();

        System.out.println("\n========================================");
        System.out.println("Demo Complete!");
        System.out.println("========================================");
    }
}

/*
 * Circuit Breaker States:
 *
 * 1. CLOSED (Normal) ✅
 *    └─ All requests pass to external service
 *    └─ Failure counter incremented on failures
 *    └─ If failures > threshold → OPEN
 *
 * 2. OPEN (Failing) ❌
 *    └─ Requests rejected immediately
 *    └─ Return fallback response
 *    └─ After wait duration → HALF_OPEN
 *
 * 3. HALF_OPEN (Testing) ⚠️
 *    └─ Limited requests allowed (permittedCalls)
 *    └─ If success → CLOSED (recovered)
 *    └─ If failure → OPEN (still failing)
 *
 * Configuration:
 * - failureRateThreshold: 50% (open if >50% fail)
 * - minimumNumberOfCalls: 5 (need 5+ calls to check rate)
 * - waitDurationInOpenState: 5s (wait before trying recovery)
 * - permittedNumberOfCallsInHalfOpenState: 2 (test with 2 calls)
 *
 * Use Cases:
 * - External API calls (Payment, Email, SMS)
 * - Database connections
 * - Remote service calls
 * - Any operation that could fail
 *
 * Benefits:
 * ✅ Prevents cascading failures
 * ✅ Fast failure (don't wait for timeout)
 * ✅ Protects failing service (stop overload)
 * ✅ Service recovery time
 * ✅ Graceful degradation
 */
