# Q2 — What Is the Most Difficult Task You Handled?

---

## 🎯 What They Are Testing
- **Problem-solving ability**
- **Ownership mindset** — did you step up?
- **Impact awareness** — did you measure results?

---

## 📐 Use This Structure (STAR Method)

| Step | What to Say |
|---|---|
| **S**ituation | What was the system / context? |
| **T**ask | What was the problem? |
| **A**ction | What did YOU do? |
| **R**esult | What was the measurable impact? |

---

## 🔥 Example: Production Performance Issue

### Situation
> Policy Service was responding slowly under high load.

### Task
> Response time exceeded **5 seconds** — SLA breach risk.

### Action

```java
// Before: N+1 query issue — fetching policies one by one
for (String policyId : policyIds) {
    Policy p = policyRepo.findById(policyId); // ❌ N DB calls
}

// After: Batch fetch in a single query
List<Policy> policies = policyRepo.findAllById(policyIds); // ✅ 1 DB call
```

Also added:
- **Redis caching** for frequently accessed policies
- **DB index** on `policy_id` column
- **Connection pool tuning** (HikariCP)

### Result
> Response time dropped from **5s → 400ms**.
> Zero SLA breaches after fix.

---

## ⚠️ Key Points to Emphasize
- You **identified** the problem (don't wait to be told)
- You **owned** the fix end to end
- You **measured** before and after
- You **prevented** future occurrence (index, cache)

---

## 💡 Senior-Level Add-On
> Always mention what you would do differently next time.
> This shows **continuous improvement mindset**.
