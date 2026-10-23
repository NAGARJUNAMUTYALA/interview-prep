# Spring Security 6 & JWT Architecture Guide

**Target Audience:** Senior Java Backend Engineers  
**Key Topics:** SecurityFilterChain, Stateless JWT Authentication, RBAC, Password Hashing, CORS & CSRF  

---

## 1. Spring Security 6 Architecture & Filter Chain

Spring Security is built on standard Servlet Filters. The entry point is the **`DelegatingFilterProxy`**, which delegates incoming requests to Spring's internal **`FilterChainProxy`**.

```text
HTTP Request
    │
    ▼
[Standard Servlet Container (Tomcat)]
    │
    ▼
[DelegatingFilterProxy]
    │
    ▼
[FilterChainProxy] (Spring SecurityFilterChain)
    │
    ├── 1. CorsFilter (Validates Origin, Headers, Methods)
    ├── 2. CsrfFilter (Disabled for stateless REST APIs)
    ├── 3. JwtAuthenticationFilter (Extracts 'Bearer <token>', verifies signature)
    ├── 4. UsernamePasswordAuthenticationFilter
    ├── 5. ExceptionTranslationFilter (Converts AuthenticationException ➔ 401, AccessDeniedException ➔ 403)
    └── 6. AuthorizationFilter (Checks @PreAuthorize / Role authorities)
    │
    ▼
[DispatcherServlet] ──▶ Controller Method
```

---

## 2. JWT Stateless Authentication Flow

```text
Client                              Auth Server / API Gateway
  │                                           │
  ├── 1. POST /api/v1/auth/login ────────────▶│
  │      {email, password}                    │ Validates credentials (BCrypt)
  │                                           │ Generates Access Token (15m TTL)
  │                                           │ Generates Refresh Token (7d TTL)
  │◀── 2. Return Tokens {accessToken, ...} ───┤
  │                                           │
  ├── 3. GET /api/v1/employees ──────────────▶│
  │      Authorization: Bearer <accessToken>  │ JwtFilter parses claims
  │                                           │ Sets SecurityContextHolder.setAuthentication()
  │◀── 4. 200 OK + Payload ───────────────────┤
```

### Key JWT Concepts (RFC 7519)
- **Header:** Algorithm & token type (e.g., `{"alg": "HS256", "typ": "JWT"}`).
- **Payload (Claims):** `sub` (Subject/User ID), `roles` (e.g. `["ROLE_ADMIN"]`), `iat` (Issued At), `exp` (Expiration).
- **Signature:** `HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)`. Tampering with any payload character invalidates the cryptographic signature.

---

## 3. High-Frequency Security Interview Questions

### Q1: Why disable CSRF protection in REST APIs using JWT?
**Answer:**
CSRF (Cross-Site Request Forgery) attacks rely on browsers automatically attaching **session cookies** to cross-origin requests.
In stateless REST APIs using JWT, authentication tokens are sent explicitly in the `Authorization: Bearer <token>` HTTP header by client-side JavaScript. Browsers never automatically attach custom authorization headers to cross-origin form submissions, so CSRF attacks are fundamentally impossible.

### Q2: What is the difference between 401 Unauthorized and 403 Forbidden?
**Answer:**
- **401 Unauthorized:** The client is unauthenticated (token missing, invalid, or expired). The client must provide valid authentication credentials.
- **403 Forbidden:** The client is authenticated, but lacks sufficient permissions (authorities/roles) to access the requested resource.

### Q3: How do you handle JWT revocation before token expiration?
**Answer:**
Since JWT is stateless, the server cannot natively revoke an issued token. Senior strategies:
1. **Short-lived access tokens (5–15 mins)** with sliding **Refresh Token Rotation**.
2. **Redis Blocklist:** Maintain a Redis blacklist of revoked `jti` (JWT Token IDs) with TTL matching the remaining token lifetime. The filter checks Redis on incoming calls.
