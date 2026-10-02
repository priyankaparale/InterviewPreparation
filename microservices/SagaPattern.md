# SAGA Pattern 📖

Pattern for managing distributed transactions in microservices.

---

## Problem: Distributed Transactions

In monolithic systems, ACID transactions are simple:
```java
@Transactional
public void createOrder(OrderRequest req) {
    // All or nothing - if any step fails, everything rolls back
    orderDB.save(order);
    paymentDB.save(payment);
    inventoryDB.update(inventory);
}
```

But in microservices with different databases:
```
Order Service → Payment Service → Inventory Service
   MySQL          PostgreSQL        MongoDB

ACID doesn't work across databases!
```

**Problem**: What if Order Service succeeds but Payment Service fails?

---

## Solution: SAGA Pattern

A sequence of local transactions where each step publishes an event that triggers the next step. If a step fails, compensating transactions undo previous steps.

---

## Two Approaches

### 1. **Choreography** 🎵 (Event-Driven)

Services communicate through events. Each service listens to events and publishes new events.

```
┌─────────────────────────────────────────────────────────────┐
│                      Event Bus (Kafka)                      │
└────┬───────────────────────────────────────────────────────┬─
     │                                                       │
     ▼                                                       │
┌──────────────┐                                             │
│Order Service │────┐                                        │
│              │    │ OrderCreated                           │
└──────────────┘    │ Event                                  │
                    ▼                                        │
                ┌──────────────┐                             │
                │Payment Service│ ────┐                      │
                │              │      │ PaymentProcessed    │
                └──────────────┘      │ Event               │
                                      ▼                     │
                                  ┌──────────────┐          │
                                  │Inventory Svc │──────────┘
                                  │              │
                                  └──────────────┘
```

#### Example:
```
User places order
        ↓
Order Service: Creates order, publishes "OrderCreated"
        ↓
Payment Service listens to "OrderCreated":
  ├─ Processes payment
  ├─ If success: Publishes "PaymentSucceeded"
  └─ If failure: Publishes "PaymentFailed"
        ↓
Inventory Service listens to "PaymentSucceeded":
  ├─ Reserves items
  ├─ If success: Publishes "OrderCompleted"
  └─ If failure: Publishes "OrderFailed" → Refund triggered
```

#### Advantages ✅
- Decoupled: Services don't know about each other
- Simple for small number of services
- Easy to understand flow

#### Disadvantages ❌
- Hard to track: Where's the transaction?
- Difficult to debug: Events flying everywhere
- Doesn't scale: Many services → Spaghetti code

---

### 2. **Orchestration** 🎼 (Centralized)

A central orchestrator (Saga Orchestrator) directs each service on what to do.

```
┌──────────────────────────────────────┐
│      Saga Orchestrator               │
│  (Manages transaction flow)          │
└──────────────────────────────────────┘
          ↓        ↓        ↓
    ┌─────┴───┬────┴───┬────┴─────┐
    ▼         ▼        ▼          ▼
┌────────┐ ┌────────┐ ┌────────┐ ┌────────┐
│Order   │ │Payment │ │Inventory│ │Notif.  │
│Service │ │Service │ │Service  │ │Service │
└────────┘ └────────┘ └────────┘ └────────┘
```

#### Example Flow:
```
Saga Orchestrator receives order request
        ↓
Step 1: Command Order Service → Create order
        ├─ Success: Continue
        └─ Failure: End saga (failed)
        ↓
Step 2: Command Payment Service → Process payment
        ├─ Success: Continue
        └─ Failure: Compensate (undo order), End saga
        ↓
Step 3: Command Inventory Service → Reserve items
        ├─ Success: Continue
        └─ Failure: Compensate (refund + undo order), End saga
        ↓
Step 4: Command Notification Service → Send confirmation
        ↓
Transaction complete! 🎉
```

#### Code Example:
```java
@Service
public class OrderSagaOrchestrator {
    
    public void executeOrderSaga(OrderRequest request) {
        OrderSagaData sagaData = new OrderSagaData(request);
        
        try {
            // Step 1: Create Order
            orderService.createOrder(sagaData);
            
            // Step 2: Process Payment
            paymentService.processPayment(sagaData);
            
            // Step 3: Reserve Inventory
            inventoryService.reserveItems(sagaData);
            
            // Step 4: Send Notification
            notificationService.sendConfirmation(sagaData);
            
        } catch (PaymentFailedException e) {
            // Compensation: Undo created order
            orderService.cancelOrder(sagaData);
            throw e;
        } catch (InventoryFailedException e) {
            // Compensation: Refund + Cancel order
            paymentService.refund(sagaData);
            orderService.cancelOrder(sagaData);
            throw e;
        }
    }
}
```

#### Advantages ✅
- Clear transaction flow: All logic in one place
- Easy to debug and trace
- Centralized control: Saga knows state
- Better for complex workflows

#### Disadvantages ❌
- Orchestrator becomes bottleneck
- Single point of failure
- More complex to implement
- Requires service API contracts

---

## Compensation Transactions (Rollback)

If something fails, undo previous steps:

```
Order Created ✅
        ↓
Payment Failed ❌
        ↓
Compensation: Delete Order ↩️
        ↓
User sees: "Payment failed, order cancelled"
```

#### Example:
```java
public void compensateOrder(OrderSagaData data) {
    // Compensation step: Delete created order
    orderService.deleteOrder(data.getOrderId());
    
    // Mark saga as failed
    logger.info("Order saga compensated for: " + data.getOrderId());
}
```

---

## Choreography vs Orchestration: Decision Matrix

| Factor | Choreography | Orchestration |
|--------|-------------|---------------|
| **Complexity** | Simple flows | Complex flows |
| **Team Size** | 2-3 services | 4+ services |
| **Debugging** | Hard | Easy |
| **Performance** | Good | Better control |
| **Coupling** | Low | High (to orchestrator) |
| **Failure Handling** | Implicit | Explicit |

---

## Real-World: Paytm-like Platform

**Choreography Approach:**
```
User orders → Order Service creates order
            → Publishes "OrderCreated"
            
Wallet Service listens → Deducts money
                      → Publishes "PaymentProcessed"

Merchant Service listens → Updates shop inventory
                        → Publishes "InventoryUpdated"

Notification Service listens → Sends confirmation SMS/Email
```

**Orchestration Approach (Better for Paytm):**
```
Order Saga Orchestrator controls:
1. Create order (Order Service)
2. Deduct wallet (Wallet Service)
3. Update inventory (Merchant Service)
4. Send notification (Notification Service)

If wallet deduction fails:
→ Delete order
→ Send failure notification
```

---

## Tools & Frameworks

- **Temporal**: SAGA orchestration platform
- **Cadence**: Distributed transaction execution
- **Axon Framework**: Event sourcing + SAGA
- **Spring Cloud**: Netflix Hystrix (deprecated, use Resilience4j)

---

## Key Takeaway 🎯

**SAGA Pattern** = Manage distributed transactions without ACID

✅ Works across multiple databases  
✅ Handles failures gracefully  
✅ Enables microservices autonomy  

But:
❌ Eventual consistency (not immediate)  
❌ Complex to implement and debug  
❌ Requires compensation transactions  
