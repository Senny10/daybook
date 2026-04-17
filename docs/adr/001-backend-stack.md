# ADR-001: Use Kotlin and Spring Boot for the Daybook Backend

## Status
Accepted — 2026-04-17

## Context
Daybook is a double-entry bookkeeping REST API. It needs to:
- Enforce strict business rules (debits must equal credits on every transaction)
- Persist financial data reliably to a relational database
- Expose endpoints for a React frontend to consume
- Be maintainable by a small team and deployable to AWS

We need to choose a backend language and framework before we can start building.

## Decision
We will use **Kotlin** as the language and **Spring Boot** as the framework.

## Alternatives Considered

### Python + FastAPI
- ✅ Faster to learn; less ceremony
- ❌ Dynamic typing is risky for financial code where type errors
  (e.g., Double instead of BigDecimal) can silently corrupt money values
- ❌ Smaller enterprise/financial-services footprint than JVM languages

### Node.js + Express
- ✅ Same language as the frontend (JavaScript)
- ❌ JavaScript's Number type uses IEEE 754 floating point, which cannot
  safely represent decimal currency (e.g., 0.1 + 0.2 = 0.30000000000000004).
  TypeScript doesn't fix this — it only adds compile-time type checking,
  then compiles down to plain JavaScript. Arithmetic still runs on IEEE
  754 floats at runtime.
- ❌ Weaker type system than Kotlin (even with TypeScript)

### Java + Spring Boot
- ✅ Most mature option; enormous ecosystem
- ❌ More verbose than Kotlin; slower to write
- ❌ Null safety is not built into the language

### Ruby on Rails
- ✅ Convention over Configuration" philosophy; new app from a single command
- ❌ Not evaluated in depth; JVM stack is preferred by infrastructure and security teams in UK financial services

## Consequences

### Easier
- Kotlin's null safety and strong typing help prevent bugs in money-handling code
- Spring Boot provides battle-tested solutions for REST APIs, validation,
  security (JWT/RBAC), and database access — features we'll need later
- Kotlin + Spring is widely used in UK financial services, making this
  project relevant to real-world work


### Harder
- Steep learning curve: learning Kotlin, Spring, JPA, and REST conventions
  simultaneously
- "Spring magic" (annotations, auto-configuration) can hide what's happening;
  debugging takes longer early on
- Longer build times than a simpler Python/Node setup