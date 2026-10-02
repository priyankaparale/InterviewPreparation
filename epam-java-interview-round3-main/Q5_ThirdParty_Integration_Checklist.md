# Q5 — What Do You Need Before Integrating a 3rd Party Service?

---

## 🎯 What They Are Testing
- Do you ask the **right questions** before writing code?
- Can you identify **risks** upfront?
- Do you think about **production readiness**, not just the happy path?

---

## 📋 Your Integration Checklist

### 🔐 1. Authentication
| Question | Why It Matters |
|---|---|
| Client ID & Client Secret? | Can't authenticate without it |
| Token-based or API Key? | Determines your auth flow |
| Token expiry / refresh strategy? | Prevent runtime auth failures |

---

### 🌐 2. API Details
| Question | Why It Matters |
|---|---|
| Base URL (prod vs staging)? | Don't call prod during dev |
| API versioning? | Breaking changes on vendor upgrade |
| Request/Response payload format? | JSON / XML / multipart |

---

### ⚡ 3. Performance & Limits
| Question | Why It Matters |
|---|---|
| Rate limit (req/min or req/day)? | Need throttling / queue |
| Average response time (SLA)? | Set correct timeouts |
| Timeout behavior on their end? | Decide your fallback strategy |

```java
// Always set explicit timeouts — never trust 3rd party SLAs
factory.setConnectTimeout(2000); // 2 seconds
factory.setReadTimeout(5000);    // 5 seconds
```

---

### 📥 4. Input / Output Capabilities
| Question | Why It Matters |
|---|---|
| Accepts text only, or image/PDF too? | Shapes your request class design |
| Max payload size? | Prevent 413 errors |
| Encoding requirements (UTF-8)? | Avoid silent data corruption |

---

### 🛡️ 5. Reliability & Error Handling
| Question | Why It Matters |
|---|---|
| What error codes can it return? | Build proper exception handling |
| Does it have retry support? | Design your retry policy |
| Does it have a sandbox/mock env? | Test without hitting real service |

```java
// Resilience4j Circuit Breaker — stop hammering a failing service
@CircuitBreaker(name = "genAiService", fallbackMethod = "fallbackPolicy")
public GenAiResponse callGenAi(GenAiRequest request) {
    return genAiClient.callGenAi(request);
}

public GenAiResponse fallbackPolicy(GenAiRequest request, Throwable t) {
    log.warn("Circuit open — using fallback for policyId={}", request.getClientId());
    return null; // triggers fallback in service layer
}
```

---

### 📊 6. Monitoring & Compliance
| Question | Why It Matters |
|---|---|
| Can we log request/response payload? | GDPR / data privacy concern |
| Do they provide usage dashboards? | Track your consumption |
| SLA guarantee %? | Define your own fallback SLA |

---

## 🗣️ How to Say This Out Loud

> "Before writing a single line of code, I gather:
> **auth details**, **API contract**, **rate limits**, **response times**,
> and **error codes**.
> I also confirm whether I can **log payloads** for compliance reasons,
> and I always ask for a **sandbox environment** for safe testing."

---

## 💡 The Line That Shows Real-World Experience
> "The most expensive production bugs I've seen came from
> assumptions made during 3rd party integration —
> so I ask every question upfront, even the uncomfortable ones."
