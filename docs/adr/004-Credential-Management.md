# ADR-004: Use Environment Variables for Credential Management

## Status
Accepted — 2026-04-22

## Context
Daybook is a public GitHub repository. The application requires
database credentials (username, password, database name) for both
the Spring Boot backend (application.properties) and the Docker
Compose database setup (docker-compose.yml). Committing credentials
directly to these files would expose them publicly — a serious
security risk even for a development project, and a dangerous habit
to establish before working with production systems.

## Decision
We will use environment variables for all credentials, loaded from
a local .env file that is never committed to version control. A
.env.example file documents the required variables with placeholder
values and is committed so new developers know what to configure.

## Alternatives Considered

### Hardcoded credentials in application.properties
- ✅ Simple — no extra setup required for new developers
- ❌ Credentials committed to a public repo are visible to anyone —
  a common cause of real-world security breaches
- ❌ Establishes a dangerous habit — the same approach on a
  production project exposes real customer data

### Environment variables with .env file (chosen)
- ✅ Credentials never appear in version control — safe for
  public repos
- ✅ Industry-standard pattern used on every professional project
- ✅ .env.example documents required variables without
  exposing real values
- ❌ New developers must create their own .env file before
  running the project — a small onboarding friction

## Consequences

### Easier
- Credentials can be rotated without changing committed code
- Different environments (development, staging, production) can
  use different credentials via different .env files
- Safe to keep the repo public without credential exposure risk

### Harder
- New developers cloning the repo must create a .env file before
  the app will run — mitigated by .env.example documenting
  exactly what's needed
- Local .env files must never be committed accidentally —
  mitigated by .gitignore entry

### Security Note
This risk was identified independently before committing to the
public repository — catching it at the right moment, before
credentials were ever exposed.