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

| Layer          | Technology                |
|----------------|---------------------------|
| Backend        | Kotlin, Spring Boot, JPA  |
| Frontend       | React                     |
| Database       | PostgreSQL                |
| Infrastructure | AWS (ECS, RDS), Terraform |
| CI/CD          | TBD                       |

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
- [x] ADR-005: JWT authentication and RBAC strategy
- [x] ADR-006: Frontend stack (React + Vite + TypeScript + Tailwind)
- [x] ADR-007: Frontend state management (TanStack Query + Context)
- [x] ADR-008: JWT storage strategy (localStorage vs httpOnly cookie)
- [x] Spring Boot project setup
- [x] First API endpoint (GET /api/hello)
- [x] PostgreSQL setup via Docker
- [x] Domain model: Account, Transaction, Entry, User entities
- [x] Repository layer (Spring Data JPA)
- [x] Service layer with double-entry validation rules
- [x] REST endpoints: accounts, transactions, reporting, auth, config
- [x] Global error handling (400/500)
- [x] First TDD red-green-refactor cycle (LedgerServiceTest)
- [x] Input validation with Bean Validation
- [x] Integration tests with @DataJpaTest and H2
- [x] 41 tests passing — 100% success rate across all layers
- [x] Reporting endpoints: Trial Balance, P&L, Balance Sheet
- [x] JWT authentication with Spring Security
- [x] RBAC: USER (read) and ADMIN (read + write) roles
- [x] BCrypt password hashing
- [x] Auth endpoints: register and login
- [x] Configurable registration (feature flag)
- [x] GET /api/transactions and GET /api/transactions/{id}
- [x] GET /api/config — feature flags endpoint
- [x] Frontend plan documented (docs/frontend-plan.md)
- [x] React + Vite + TypeScript + Tailwind setup
- [x] Login page — connected to backend, JWT auth working
- [x] AuthContext — JWT storage, user state, role helpers
- [x] ProtectedRoute — auth and role-based redirects
- [x] Header with navigation and role-based menu
- [x] Accounts page — list with type badges, create form (ADMIN only)
- [x] Full stack verified end-to-end in browser

### In Progress
- [ ] Transactions page
- [ ] New Transaction page (double-entry form)
- [ ] Reports page

### Planned
- [ ] ADR-009: Configurable registration pattern
- [ ] Seed script for demo data
- [ ] Sky background with cloud effects (polish)
- [ ] Loading skeletons
- [ ] AWS deployment via Terraform
- [ ] CI/CD pipeline
- [ ] AWS Cloud Practitioner certification study

## Architecture Decision Records

All significant technical decisions are documented as ADRs:

| ADR                                              | Title                                        | Status   |
|--------------------------------------------------|----------------------------------------------|----------|
| [001](docs/adr/001-backend-stack.md)             | Use Kotlin and Spring Boot                   | Accepted |
| [002](docs/adr/002-monorepo.md)                  | Monorepo structure                           | Accepted |
| [003](docs/adr/003-entity-ids.md)                | Entity ID strategy (Long vs UUID)            | Accepted |
| [004](docs/adr/004-credential-management.md)     | Credential management (.env pattern)         | Accepted |
| [005](docs/adr/005-security-strategy.md)         | JWT Authentication and RBAC for API Security | Accepted |
| [006](docs/adr/006-frontend-stack.md)            | Frontend stack (React + Vite + TypeScript)   | Accepted |
| [007](docs/adr/007-frontend-state-management.md) | Frontend state management                    | Accepted |
| [008](docs/adr/008-jwt-storage.md)               | JWT storage strategy                         | Accepted |
## License

[MIT](LICENSE)