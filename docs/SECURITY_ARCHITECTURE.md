# 🔒 Security Architecture - Catrimonial AI

## Security Layers

```
┌─────────────────────────────────────────────────┐
│ Layer 1: Network Security                        │
│ • HTTPS/TLS 1.3 • Firewall • Rate Limiting      │
├─────────────────────────────────────────────────┤
│ Layer 2: Application Gateway                     │
│ • Nginx • CORS • Security Headers • WAF         │
├─────────────────────────────────────────────────┤
│ Layer 3: Authentication                          │
│ • JWT (RS256) • Refresh Tokens • Email Verify   │
├─────────────────────────────────────────────────┤
│ Layer 4: Authorization                           │
│ • RBAC (User/Admin/Vet) • Resource ownership    │
├─────────────────────────────────────────────────┤
│ Layer 5: Input Validation                        │
│ • Bean Validation • Sanitization • Type safety  │
├─────────────────────────────────────────────────┤
│ Layer 6: Data Protection                         │
│ • Bcrypt (12 rounds) • Encryption at rest       │
├─────────────────────────────────────────────────┤
│ Layer 7: Audit & Monitoring                      │
│ • Audit logs • Prometheus • Alerts              │
└─────────────────────────────────────────────────┘
```

## Authentication Flow

```
User → Login Request → Spring Security
                           ↓
                    Verify Credentials (Bcrypt)
                           ↓
                    Generate JWT (15 min) + Refresh Token (7 days)
                           ↓
                    Return to Client → Stored securely
                           ↓
               Subsequent Requests: Bearer JWT
                           ↓
               JwtAuthFilter validates → Grant/Deny access
```

## Implemented Security Measures

### Authentication & Authorization
- [x] JWT with HMAC-SHA256 (configurable to RS256)
- [x] Refresh token rotation on use
- [x] Password hashing with BCrypt (12 rounds)
- [x] Email verification before sensitive operations
- [x] Password reset with time-limited tokens
- [x] Role-based access control (USER, ADMIN, VETERINARIAN)
- [x] Resource ownership validation

### Input Security
- [x] Bean Validation on all DTOs
- [x] SQL injection prevention via JPA parameterized queries
- [x] XSS prevention via output encoding
- [x] File upload type/size validation
- [x] Request size limits

### Network Security
- [x] HTTPS enforcement (Nginx)
- [x] CORS configuration (whitelist)
- [x] Rate limiting (auth: 5/s, API: 30/s, upload: 3/s)
- [x] Security headers (CSP, X-Frame-Options, etc.)
- [x] WebSocket origin validation

### Data Protection
- [x] Passwords never stored in plaintext
- [x] Sensitive data not logged
- [x] JWT secrets in environment variables
- [x] Database credentials secured
- [x] Soft-delete for data recovery

### Monitoring & Audit
- [x] Audit log table for all critical actions
- [x] Prometheus metrics collection
- [x] Failed login attempt tracking
- [x] Rate limit violation alerts

### DevSecOps
- [x] GitHub Actions security scanning (Trivy)
- [x] Dependency vulnerability scanning
- [x] Non-root Docker containers
- [x] Docker health checks
- [x] .env secrets management

## Rate Limiting Strategy

| Endpoint Category | Limit | Burst |
|------------------|-------|-------|
| Authentication | 5/second | 10 |
| General API | 30/second | 50 |
| File Upload | 3/second | 5 |
| AI Endpoints | 10/second | 20 |
| WebSocket | Connection-based | N/A |

## Roles & Permissions

| Action | USER | ADMIN | VET |
|--------|------|-------|-----|
| Create cat profile | ✅ | ✅ | ✅ |
| Edit own cat | ✅ | ✅ | ✅ |
| Delete own cat | ✅ | ✅ | ✅ |
| View all cats | ✅ | ✅ | ✅ |
| Verify cat profiles | ❌ | ✅ | ❌ |
| Disable users | ❌ | ✅ | ❌ |
| Moderate posts | ❌ | ✅ | ❌ |
| View admin dashboard | ❌ | ✅ | ❌ |
| Add health records | ✅ | ✅ | ✅ |
| Verify health records | ❌ | ❌ | ✅ |
