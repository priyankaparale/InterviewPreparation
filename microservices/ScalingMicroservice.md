# Scaling Microservices 📈

Strategies to handle increased load and improve performance in microservices architecture.

---

## Types of Scaling

### 1. **Vertical Scaling** (Scale Up)
Add more resources to a single server.

```
Before: 1 Server with 2GB RAM, 2 CPU
         ↓
After:  1 Server with 16GB RAM, 8 CPU
```

#### Advantages ✅
- Simple to implement
- No code changes needed
- Lower latency (single machine)

#### Disadvantages ❌
- Single point of failure
- Limited by hardware ceiling
- Expensive (top-tier hardware)
- Downtime needed for upgrades

#### When to Use:
- Low traffic scenarios
- Internal/non-critical services
- Temporary solutions

---

### 2. **Horizontal Scaling** (Scale Out) 🚀
Add more servers/instances.

```
Before: 1 Service Instance
        
After:  ┌──────────────────┐
        │  Load Balancer   │
        └───────┬──────────┘
         ┌──────┼──────┐
         ▼      ▼      ▼
    Instance Instance Instance
      (App)   (App)   (App)
```

#### Advantages ✅
- No single point of failure
- Better resource utilization
- Cost-effective scaling
- Auto-scaling possible
- Better resilience

#### Disadvantages ❌
- More complex setup
- Network overhead
- Distributed system challenges
- State management issues

#### When to Use:
- High traffic services
- Production applications
- Services need high availability

---

## Scaling Strategies

### 1. **Load Balancing** 🎯

Distribute requests across multiple instances.

```
Client requests
       ↓
┌─────────────────┐
│  Load Balancer  │
├─────────────────┤
│  Round Robin?   │
│  Least Conn?    │
│  IP Hash?       │
└─────────────────┘
   ↓      ↓      ↓
Server1 Server2 Server3
```

#### Common Algorithms:
- **Round Robin**: 1→2→3→1→2→3... (equal distribution)
- **Least Connections**: Route to server with fewest connections
- **IP Hash**: Same IP always goes to same server (sticky sessions)
- **Weighted**: Different servers get different loads

#### Tools:
- **Nginx**: High-performance load balancer
- **HAProxy**: Open-source load balancer
- **AWS ELB**: Elastic Load Balancer
- **Kubernetes Service**: Built-in load balancing

---

### 2. **Caching** 💾

Store frequently accessed data to avoid repeated computations.

#### Levels of Caching:

**a) Application-Level Cache**
```java
@Service
public class UserService {
    @Cacheable(value = "users", key = "#userId")
    public User getUser(String userId) {
        // Hit DB only first time
        return userRepository.findById(userId);
    }
}
```

**b) Distributed Cache (Redis/Memcached)**
```
┌─────────────────────────────────┐
│     Application Instances       │
├─────────────────────────────────┤
│  All connect to central Redis   │
└─────────────────────────────────┘
       ↓
    Redis Cache
    (Shared state)
```

**c) CDN Cache**
```
User Request → CDN Edge Server (cached copy)
            → If miss → Origin Server
```

#### Cache Invalidation Strategies:
- **TTL**: Time-based expiration
- **Event-based**: Update cache on data change
- **Manual**: Explicitly clear cache
- **LRU**: Least Recently Used eviction

---

### 3. **Database Scaling** 🗄️

#### Replication (Read Scaling)
```
┌─────────────────┐
│  Master (RW)    │ ← Writes
├─────────────────┤
│     ↓     ↓     │
│  Slave1   Slave2│ ← Reads only
│ (Read)    (Read)│
└─────────────────┘
```

Benefits: Distribute read load, increased availability

#### Sharding (Horizontal Partitioning)
```
Data partitioned by key (e.g., User ID)

User ID 1-1000   → Shard 1 (DB1)
User ID 1001-2000 → Shard 2 (DB2)
User ID 2001-3000 → Shard 3 (DB3)

Queries: Find which shard, then query
```

Benefits: Distribute both read and write load

#### Trade-offs:
- Increased complexity
- Cross-shard queries harder
- Data consistency challenges
- Need distributed transactions (SAGA)

---

### 4. **Message Queue/Async Processing** 📨

Move heavy operations to background.

```
Synchronous (Blocking):
POST /order → Process → Return response (slow if payment slow)

Asynchronous (Non-blocking):
POST /order → Queue message → Return immediately
              ↓
              Background Worker processes payment
              ↓
              Send notification
```

#### Implementation:
```java
// Using Kafka
@Service
public class OrderService {
    
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest req) {
        // Save order
        Order order = orderRepository.save(new Order(req));
        
        // Publish event (non-blocking)
        kafkaTemplate.send("order-events", 
            new OrderCreatedEvent(order.getId()));
        
        // Return immediately (don't wait for payment processing)
        return ResponseEntity.ok(order);
    }
}

// Separate service processes payment
@Service
public class PaymentProcessor {
    
    @KafkaListener(topics = "order-events")
    public void processPayment(OrderCreatedEvent event) {
        // Process payment (can take time)
        Payment payment = paymentService.charge(event.getOrderId());
        
        // Publish result
        kafkaTemplate.send("payment-events", payment);
    }
}
```

Benefits:
- Non-blocking calls
- Better resource utilization
- Resilient to failures
- Easy to scale independent workers

---

### 5. **Database Query Optimization** 🚀

```sql
-- ❌ Slow Query: N+1 problem
SELECT * FROM users;
FOR EACH user:
    SELECT * FROM orders WHERE user_id = user.id;  -- Extra queries!

-- ✅ Optimized: Join query
SELECT u.*, o.* FROM users u
JOIN orders o ON u.id = o.user_id;

-- ✅ With Index
CREATE INDEX idx_orders_user_id ON orders(user_id);
```

Strategies:
- Add database indexes
- Use JOIN instead of N+1 queries
- Pagination for large results
- Database denormalization (for read-heavy)
- Query result caching

---

### 6. **Service Separation/Decomposition** 🔀

Split large services into smaller focused services.

```
Before (Monolithic):
┌────────────────────────────┐
│    E-commerce Service      │
├────────────────────────────┤
│ User + Order + Payment +   │
│ Inventory + Notification   │
└────────────────────────────┘
    All scale together → Inefficient

After (Microservices):
┌─────────────────┐  ┌──────────────┐  ┌─────────────────┐
│   User Service  │  │ Order Service│  │ Payment Service │
│  (1 instance)   │  │ (3 instances)│  │ (2 instances)   │
└─────────────────┘  └──────────────┘  └─────────────────┘

Scale only what needs scaling
```

---

## Scaling Paytm-like Platform

### Architecture:
```
                    ┌─────────────────────┐
                    │   Nginx (LB)        │
                    └──────────┬──────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
        ▼                      ▼                      ▼
    ┌────────┐             ┌────────┐            ┌────────┐
    │API GW  │             │API GW  │            │API GW  │
    │(inst1) │             │(inst2) │            │(inst3) │
    └────┬───┘             └────┬───┘            └────┬───┘
         │                      │                      │
  ┌──────┴──────┬───────────────┼───────────────┬─────┴──────┐
  │              │               │               │             │
  ▼              ▼               ▼               ▼             ▼
┌────────┐  ┌─────────┐  ┌─────────┐  ┌──────────┐  ┌────────┐
│ User   │  │ Wallet  │  │ Order   │  │ Payment  │  │Notif   │
│ Svc(3) │  │ Svc(5)  │  │ Svc(2)  │  │ Svc(4)   │  │Svc(2)  │
└────────┘  └─────────┘  └─────────┘  └──────────┘  └────────┘

Redis Cache     Kafka Message Bus       MySQL+Replica  MongoDB
```

---

## Auto-Scaling (Cloud-Native) ☁️

Automatically adjust resources based on metrics.

```yaml
# Kubernetes HPA Example
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: wallet-service-hpa
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: wallet-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70  # Scale up if CPU > 70%
  - type: Resource
    resource:
      name: memory
      target:
        type: Utilization
        averageUtilization: 80  # Scale up if Memory > 80%
```

---

## Monitoring & Metrics 📊

Key metrics to monitor:
- **CPU Usage**: > 70% → scale up
- **Memory Usage**: > 80% → scale up
- **Response Time**: > threshold → investigate
- **Error Rate**: > 1% → alert
- **Throughput**: Requests/second

Tools:
- Prometheus: Metrics collection
- Grafana: Visualization
- ELK Stack: Logging and analysis

---

## Scaling Checklist ✅

Before scaling horizontally:
1. ✅ Profile your application (identify bottlenecks)
2. ✅ Optimize code and queries
3. ✅ Add caching where applicable
4. ✅ Use database replication for read-heavy loads
5. ✅ Implement async processing
6. ✅ Then add load balancing
7. ✅ Finally auto-scaling

---

## Key Takeaway 🎯

**Scaling = Understanding your bottleneck**

Don't just throw more servers. Find:
- Is it CPU bound? (Code optimization)
- Is it I/O bound? (Caching, async)
- Is it Memory? (Reduce objects)
- Is it Database? (Indexing, replication)
- Is it Network? (CDN, compression)

Then scale accordingly!
