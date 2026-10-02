# Q3 — Integrate Gen AI Service into Policy Service

---

## 📋 Scenario Given by Interviewer

### ❓ Unknown (Provided Later / At Runtime)
- Client ID & Client Secret
- Number of requests allowed (rate limit)
- Average response time
- Input types supported (text, image, etc.)
- Accuracy percentage

### ✅ Known
- Existing **Policy Service** with policy ID and response
- Sample **request/response payload** from Gen AI

### 🎯 Expectation
- Optimized & better response using Gen AI
- **Do NOT break** existing functionality
- If Gen AI **fails** → return previous policy response
- Log Gen AI interactions separately
- Separate metrics for Gen AI calls

---

## 🗺️ Architecture Overview

```
Client
  │
  ▼
PolicyController  ──────────────────────────────────────────┐
  │                                                          │
  ▼                                                          │
PolicyService                                                │
  │                                                          │
  ├──► [Authenticate Request] (JWT Validator)                │
  │                                                          │
  ├──► [Call Gen AI Client]                                  │
  │         │                                                │
  │         ├── SUCCESS → map Gen AI response ──────────────►│
  │         │                                                │
  │         └── FAILURE → fallback: existing policy response►│
  │                                                          │
  └──► [Log + Metrics]                                  Response
```

---

## Step 0 — JWT Token Utility Class

```java
@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String clientId) {
        return Jwts.builder()
            .setSubject(clientId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 3600_000))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false; // expired or tampered
        }
    }
}
```

---

## Step 1 — Request / Response Models

```java
// Incoming policy request
public class PolicyRequest {
    private String policyId;
    private String policyDocument; // text content sent to Gen AI
    // getters, setters
}

// Existing policy response (fallback)
public class PolicyResponse {
    private String policyId;
    private String summary;
    private String status;
    // getters, setters
}

// Gen AI specific request wrapper
public class GenAiRequest {
    private String inputText;
    private String clientId;
    private Map<String, Object> metadata;
    // getters, setters
}

// Gen AI response wrapper
public class GenAiResponse {
    private String result;
    private double confidenceScore;
    private long responseTimeMs;
    // getters, setters
}
```

---

## Step 2 — API Layer (Controller)

```java
@RestController
@RequestMapping("/api/v1/policy")
public class PolicyController {

    @Autowired private PolicyService policyService;
    @Autowired private JwtTokenUtil jwtTokenUtil;

    @PostMapping("/analyze")
    public ResponseEntity<PolicyResponse> analyzePolicy(
            @RequestHeader("Authorization") String token,
            @RequestBody PolicyRequest request) {

        // Step: Validate token
        if (!jwtTokenUtil.validateToken(token.replace("Bearer ", ""))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        PolicyResponse response = policyService.analyzePolicyWithGenAi(request);
        return ResponseEntity.ok(response);
    }
}
```

---

## Step 3 — Gen AI Client (HTTP Integration)

```java
@Component
public class GenAiClient {

    @Value("${genai.url}")
    private String genAiUrl;

    @Value("${genai.clientId}")
    private String clientId;

    @Value("${genai.clientSecret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public GenAiResponse callGenAi(GenAiRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Client-Id", clientId);
        headers.set("X-Client-Secret", clientSecret);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GenAiRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(genAiUrl, entity, GenAiResponse.class);
    }
}
```

---

## Step 4 — Custom Exception Class

```java
public class GenAiServiceException extends RuntimeException {

    private final int statusCode;

    public GenAiServiceException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public GenAiServiceException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 503;
    }

    public int getStatusCode() { return statusCode; }
}
```

---

## Step 5 — Service Layer (Fallback + Logging + Metrics)

```java
@Service
@Slf4j
public class PolicyService {

    @Autowired private GenAiClient genAiClient;
    @Autowired private PolicyRepository policyRepository;
    @Autowired private MeterRegistry meterRegistry; // Micrometer

    public PolicyResponse analyzePolicyWithGenAi(PolicyRequest request) {

        // Fetch existing policy (always available as fallback)
        PolicyResponse existingResponse = policyRepository.findById(request.getPolicyId());

        long startTime = System.currentTimeMillis();
        try {
            GenAiRequest genAiRequest = new GenAiRequest();
            genAiRequest.setInputText(request.getPolicyDocument());
            genAiRequest.setClientId("policy-service");

            GenAiResponse genAiResponse = genAiClient.callGenAi(genAiRequest);

            long elapsed = System.currentTimeMillis() - startTime;

            // Log Gen AI interaction (separate log marker)
            log.info("[GEN_AI_LOG] policyId={} responseTimeMs={} confidence={}",
                request.getPolicyId(), elapsed, genAiResponse.getConfidenceScore());

            // Metrics
            meterRegistry.counter("genai.calls.success").increment();
            meterRegistry.timer("genai.response.time").record(elapsed, TimeUnit.MILLISECONDS);

            // Map Gen AI response into PolicyResponse
            return mapGenAiToPolicy(existingResponse, genAiResponse);

        } catch (Exception ex) {
            long elapsed = System.currentTimeMillis() - startTime;

            // Log failure
            log.error("[GEN_AI_LOG] FAILED policyId={} error={} responseTimeMs={}",
                request.getPolicyId(), ex.getMessage(), elapsed);

            // Metrics
            meterRegistry.counter("genai.calls.failure").increment();

            // ✅ FALLBACK — return existing policy response, no system break
            return existingResponse;
        }
    }

    private PolicyResponse mapGenAiToPolicy(PolicyResponse existing, GenAiResponse genAi) {
        existing.setSummary(genAi.getResult()); // enrich with Gen AI output
        return existing;
    }
}
```

---

## Step 6 — Optimization Points to Mention

| Optimization | Why |
|---|---|
| **Cache** Gen AI responses (Redis) | Avoid repeated API calls for same policy |
| **Rate limiter** (Resilience4j) | Respect Gen AI API limits |
| **Timeout config** (RestTemplate) | Don't hang if Gen AI is slow |
| **Circuit Breaker** | Stop hammering a failing Gen AI service |

```java
// Timeout config example
@Bean
public RestTemplate restTemplate() {
    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory();
    factory.setConnectTimeout(2000);   // 2s connect
    factory.setReadTimeout(5000);      // 5s read
    return new RestTemplate(factory);
}
```

---

## 💡 Senior-Level Points to Say Out Loud
- "I would not expose Gen AI failure to the end user"
- "Fallback ensures **backward compatibility**"
- "Separate log markers make **debugging in production** easier"
- "Metrics let us decide if Gen AI is worth keeping"
