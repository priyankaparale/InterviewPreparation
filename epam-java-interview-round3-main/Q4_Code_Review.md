# Q4 — Code Review as a Senior Developer

---

## 🎯 What They Are Testing
- Do you think beyond "does it work?"
- Can you catch issues before they hit production?
- Do you have a **structured review process**?

---

## 🔍 Your Code Review Checklist (In Order)

### 1. ✅ Readability & Naming
```java
// ❌ Bad
int x = p.getPD();

// ✅ Good
int expiryDays = policy.getPolicyDuration();
```
> Methods and variables must express **intent**, not implementation.

---

### 2. ✅ Exception Handling
```java
// ❌ Bad — swallowing exception silently
try {
    callGenAi(request);
} catch (Exception e) {
    // do nothing
}

// ✅ Good — log + fallback
try {
    callGenAi(request);
} catch (Exception e) {
    log.error("Gen AI call failed for policyId={}", request.getPolicyId(), e);
    return fallbackResponse(request.getPolicyId());
}
```
> Empty catch blocks are production **time bombs**.

---

### 3. ✅ Security
```java
// ❌ Bad — hardcoded credentials
String secret = "mySecret123";

// ✅ Good — externalize via config/vault
@Value("${genai.clientSecret}")
private String clientSecret;
```
> Flag any hardcoded secrets, SQL injection risks, or missing input validation.

---

### 4. ✅ Performance
```java
// ❌ Bad — N+1 query inside loop
for (String id : policyIds) {
    Policy p = repo.findById(id); // N DB calls
}

// ✅ Good — batch fetch
List<Policy> policies = repo.findAllById(policyIds); // 1 DB call
```
> Always look for loops that hide DB calls.

---

### 5. ✅ Logging
```java
// ❌ Bad — no context
log.info("Policy processed");

// ✅ Good — structured, traceable
log.info("Policy processed successfully policyId={} userId={} durationMs={}",
    policyId, userId, elapsed);
```
> In microservices, bad logs = impossible debugging in production.

---

### 6. ✅ Test Coverage
- Is there a unit test for the **happy path**?
- Is there a test for the **failure/fallback path**?
- Are edge cases covered (null, empty, timeout)?

```java
@Test
void shouldReturnFallbackWhenGenAiFails() {
    when(genAiClient.callGenAi(any())).thenThrow(new RuntimeException("timeout"));

    PolicyResponse response = policyService.analyzePolicyWithGenAi(request);

    assertNotNull(response);
    assertEquals("existing-summary", response.getSummary()); // fallback returned
}
```

---

### 7. ✅ SOLID / Design Principles
| Principle | Quick Check |
|---|---|
| SRP | Does this class do ONE thing? |
| OCP | Can I extend without modifying? |
| DI | Are dependencies injected, not created with `new`? |

---

## 🗣️ How to Structure Your Answer Out Loud

> "As a senior developer, I review code in layers:
> First **readability**, then **exception handling**,
> then **security** — especially hardcoded values.
> I check for **performance anti-patterns** like N+1 queries.
> I verify **logging** has enough context for production debugging.
> Finally I check **test coverage** — not just the happy path."

---

## 💡 The One Line That Impresses Architects
> "Working code is the baseline — my review ensures it's also
> **maintainable, secure, and observable in production**."
