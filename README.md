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
| CI/CD          | GitHub Actions            |

## Architecture

Daybook uses a layered architecture (Controller → Service → Repository)
to separate HTTP handling, business logic, and data access.

Full architecture documentation with diagrams:
[docs/architecture.md](docs/architecture.md)

## Getting Started

### Prerequisites
- JDK 21
- Docker Desktop
- Node.js 20+
- An `.env` file (see `.env.example`)

### Run the backend
```bash
docker compose up -d
cd backend
./gradlew bootRun
```

### Run with seed data
```bash
# Add to your .env:
SPRING_PROFILES_ACTIVE=seed
```

### Run the frontend
```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`

### Default demo credentials
- **Admin:** `admin` / `admin123!`
- **Bookkeeper:** `bookkeeper` / `book123!`

## Project Status

🟢 **Complete** — All criteria evidenced

### Done
- [x] Architecture design: 9 ADRs, layered architecture documentation
- [x] Backend: Kotlin Spring Boot + PostgreSQL, 41 tests passing
- [x] JWT authentication + RBAC (backend + frontend)
- [x] Frontend: React + TypeScript, 11 tests passing
- [x] Total: 52 tests across full stack, 100% passing
- [x] GitHub Actions CI: both workflows on every PR
- [x] Branch protection: PRs required, both checks must pass
- [x] Seed script: 8 accounts, 5 transactions, 2 users
- [x] Terraform infrastructure: 34 AWS resources across 5 modules
- [x] Dockerfile: multi-stage build, non-root user, linux/amd64
- [x] Full stack deployed to AWS: ECS + RDS + ALB
- [x] 6 deployment issues debugged and resolved independently
- [x] AWS Cost Explorer: $0.00 total spend confirmed
- [x] Zero-spend budget alert configured
- [x] Cost analysis: docs/cost-analysis.md

### Planned
- [ ] AWS Cloud Practitioner certification
- [ ] Frontend deployment to S3/CloudFront (stretch goal)
- [ ] Re-apply infrastructure before panel conversation

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
| [009](docs/adr/009-configurable-registration.md) | Configurable registration feature flag | Accepted |

## License

[MIT](LICENSE)