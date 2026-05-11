# ADR-009: Configurable Registration — Feature Flag Pattern

## Status
Accepted — 2026-05-11

## Context
Daybook needs to support two registration modes:
1. **Public registration** — anyone can create an account
   (useful for demos and open deployments)
2. **Admin-only registration** — only ADMINs can create users
   (more secure for production deployments)

The mode needs to be switchable without changing or redeploying
code — only configuration should change.

## Decision
We will use a Spring Boot property `daybook.registration.public`
loaded from an environment variable `REGISTRATION_PUBLIC` to
control registration behaviour at runtime.

- `REGISTRATION_PUBLIC=true` → anyone can register
- `REGISTRATION_PUBLIC=false` (default) → only ADMINs can register,
  public requests return 403 Forbidden

## Alternatives Considered

### Hardcode one mode
- ✅ Simpler — no configuration needed
- ❌ Forces a code change and redeployment to switch modes
- ❌ Cannot serve both demo and production from the same build

### Database-driven feature flag
- ✅ Changeable at runtime without restart
- ❌ Requires a feature flags table and admin UI to toggle
- ❌ Significant complexity for a single boolean value
- ❌ Not evaluated in depth for this project

### Environment variable (chosen)
- ✅ One codebase, two modes — same build serves demo and production
- ✅ Secure by default — `false` requires explicit opt-in to enable
- ✅ Standard 12-factor app pattern for configuration
- ✅ Surfaced via GET /api/config so the frontend can adapt its UI
  (show/hide the Register link based on the flag)
- ❌ Requires app restart to change — acceptable for this use case

## Consequences

### Easier
- Demo deployments use REGISTRATION_PUBLIC=true — panels can
  self-register without database access
- Production deployments use REGISTRATION_PUBLIC=false — only
  ADMINs can add users, reducing attack surface
- Frontend reads /api/config on startup and conditionally renders
  the Register link — consistent UX in both modes

### Harder
- Developers must remember to set REGISTRATION_PUBLIC in their
  .env file — mitigated by .env.example documenting the variable
- Changing the mode requires an app restart — acceptable trade-off
  given the simplicity of the implementation

### Security Note
Defaulting to false implements the principle of least privilege —
the more secure option is the default. Public registration must
be explicitly enabled.