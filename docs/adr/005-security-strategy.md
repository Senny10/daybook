# ADR-005: JWT Authentication and RBAC for API Security

## Status
Accepted — 2026-05-01

## Context
Daybook is a financial API handling sensitive accounting data. All
endpoints need to be protected from unauthorised access. We need to
decide on an authentication and authorisation strategy before the
API is deployed.

## Decision
We will use JWT (JSON Web Token) for authentication and Role-Based
Access Control (RBAC) for authorisation, implemented via Spring
Security.

Two roles are defined:
- USER: read-only access to all GET endpoints
- ADMIN: full access including creating accounts and posting
  transactions

Public endpoints (no authentication required):
- POST /api/auth/register
- POST /api/auth/login

## Alternatives Considered

### Session-based authentication
- ✅ Simpler to implement; built into Spring Security by default
- ❌ Stateful — requires server-side session storage, which doesn't
  scale horizontally
- ❌ Cookie-based, which is awkward for REST API clients

### OAuth2 with an external provider (e.g. Auth0, Keycloak)
- ✅ Production-grade; handles token refresh, revocation, MFA
- ❌ Adds external dependency and significant complexity for a
  portfolio project
- ❌ Not evaluated in depth for this stage of the project

### API Keys
- ✅ Simple to implement and understand
- ❌ No expiry mechanism — compromised keys are valid forever
- ❌ No built-in role information

## Consequences

### Easier
- Stateless authentication — any server can validate any token
  without shared state
- Role information embedded in JWT payload — no database lookup
  needed for authorisation
- Spring Security handles BCrypt password hashing, authentication
  manager, and filter chain automatically

### Harder
- JWT tokens cannot be revoked before expiry — a stolen token
  remains valid for 24 hours (mitigated in production by short
  expiry times and token refresh mechanisms)
- JWT secret must be kept secure — exposure allows anyone to
  generate valid tokens (mitigated by environment variables and
  AWS Secrets Manager in production)
- Spring Security 6 requires explicit SecurityContext persistence
  via RequestAttributeSecurityContextRepository — not required
  in older Spring Security versions

### Security Notes
- Passwords stored as BCrypt hashes — plain text never persisted
- JWT secret loaded from environment variable — never committed
  to version control
- RBAC implements principle of least privilege — USERs cannot
  modify financial data