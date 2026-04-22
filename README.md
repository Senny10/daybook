# Daybook 🌞📖

A double-entry bookkeeping REST API built with Kotlin and Spring Boot,
with a React frontend.

## Why Daybook?

Daybook is a full-stack project I'm building to close the gaps between
knowing individual technologies and delivering a complete, production-ready
application. It's a proof-of-concept bookkeeping system where users can
create accounts and manage money using real double-entry accounting
principles.

The name "Daybook" is a traditional accounting term for a chronological
transaction log — and a nod to bringing daylight to a discipline most
software engineers never touch.

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Kotlin, Spring Boot, JPA |
| Frontend | React |
| Database | PostgreSQL |
| Infrastructure | AWS (ECS, RDS), Terraform |
| CI/CD | TBD |

## Architecture

Daybook uses a layered architecture (Controller → Service → Repository)
to separate HTTP handling, business logic, and data access.

Full architecture documentation with diagrams:
[docs/architecture.md](docs/architecture.md)

## Getting Started

> 🚧 **Coming soon** — the backend is currently being scaffolded.
> Setup instructions will be added once the first endpoint is running.

### Prerequisites

- JDK 21 (currently using JDK 23)
- Docker Desktop (for PostgreSQL)
- Node.js 20+ (for frontend, later)

## Project Status

🟢 **Active development** — Phase 1 of 3

### Done
- [x] Architecture design and documentation
- [x] ADR-001: Backend stack decision (Kotlin + Spring Boot)
- [x] ADR-002: Monorepo structure
- [x] ADR-003: Entity ID strategy (Long vs UUID)
- [x] ADR-004: Credential management (.env pattern)
- [x] Spring Boot project setup
- [x] First API endpoint (GET /api/hello)
- [x] PostgreSQL setup via Docker
- [x] Domain model: Account, Transaction, Entry entities

### In Progress
- [ ] Repository layer (CRUD operations)
- [ ] Service layer (double-entry validation)

### Planned
- [ ] First real CRUD endpoints
- [ ] Double-entry validation (debits must equal credits)
- [ ] JWT authentication + RBAC
- [ ] React frontend
- [ ] AWS deployment via Terraform

## Architecture Decision Records

All significant technical decisions are documented as ADRs:

| ADR | Title | Status |
|---|---|---|
| [001](docs/adr/001-backend-stack.md) | Use Kotlin and Spring Boot | Accepted |
| [002](docs/adr/002-monorepo.md) | Monorepo structure | Accepted |

## License

[MIT](LICENSE)