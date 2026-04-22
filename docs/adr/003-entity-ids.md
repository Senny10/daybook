# ADR-003: Use Auto-increment Long for Entity IDs

## Status
Accepted — 2026-04-22

## Context
Daybook's three core entities (Account, Transaction, Entry) each require
a primary key. Two common approaches exist: auto-incrementing Long integers
(BIGSERIAL in PostgreSQL) and UUIDs. We need to decide which to use before
any data is persisted.

## Decision
We will use auto-incrementing Long IDs for all entities in the initial
implementation, with the option to add a separate UUID field for external
API exposure in a future iteration.

## Alternatives Considered

### UUID
- ✅ Non-guessable — prevents attackers from enumerating records
  via sequential IDs (e.g. /accounts/1, /accounts/2)
- ✅ Safe for distributed systems — any server can generate a UUID
  independently without coordination
- ❌ Slower database performance — random UUIDs fragment indexes,
  causing more disk reads on large datasets
- ❌ 16 bytes vs 8 bytes — doubles the storage cost of every
  foreign key reference
- ❌ Harder to read in logs and debugging sessions

### Auto-increment Long (chosen)
- ✅ Fast sequential inserts — database index stays compact and ordered
- ✅ Half the storage of UUID (8 bytes)
- ✅ Readable in logs and debugging
- ❌ Sequential IDs expose record counts to API consumers
- ❌ Not safe for distributed systems without coordination

## Consequences

### Easier
- Simpler JPA configuration — no UUID generation strategy needed
- Faster query performance on joins between entities
- Readable IDs during development and debugging

### Harder
- API endpoints that expose IDs (e.g. GET /accounts/1) reveal
  record counts — mitigated in a future iteration by exposing a
  separate UUID field externally while using Long internally
- Not suitable for distributed deployment without modification

### Future Mitigation
If Daybook were deployed for real users, we would add a `publicId`
UUID field to each entity for external API exposure:

    @Column(nullable = false, unique = true)
    val publicId: UUID = UUID.randomUUID()

Internal operations continue using Long for performance; the API
exposes only publicId for security.