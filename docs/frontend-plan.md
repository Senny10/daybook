# Daybook Frontend — Plan

## Scope & Goals

A React frontend that exposes Daybook's existing API to users through
a clean, professional interface.

**Goals:**

1. Demonstrate full-stack integration (auth, CRUD, role-based UI)
2. Practice React patterns (hooks, routing, state management)
3. Show secure handling of JWT in a browser context
4. Provide a usable demo for the promotion panel

**Non-goals:**

- Personal finance features (budgets, cash flow, weather animations)
- Pixel-perfect product polish
- Mobile-native experience

## Tech Stack

| Concern     | Choice                       |
| ----------- | ---------------------------- |
| Framework   | React 18 + Vite              |
| Language    | TypeScript                   |
| Routing     | React Router v6              |
| Styling     | Tailwind CSS                 |
| API state   | TanStack Query (React Query) |
| Forms       | React Hook Form + Zod        |
| HTTP client | Axios                        |

## Pages

1. **Login** (`/login`) — username + password, POST to `/api/auth/login`
2. **Register** (`/register`) — public when `daybook.registration.public=true`
3. **Accounts** (`/accounts`) — list with balances; ADMIN sees create button
4. **New Account** (`/accounts/new`) — ADMIN only
5. **New Transaction** (`/transactions/new`) — ADMIN only; live balance check
6. **Transactions** (`/transactions`) — list with filters
7. **Transaction Detail** (`/transactions/:id`) — full breakdown with entries
8. **Reports** (`/reports`) — Trial Balance, P&L, Balance Sheet (replaces dashboard)

## Visual Design

Sky/Sun palette inspired by the project name "Daybook":

| Role        | Hex               | Usage                    |
| ----------- | ----------------- | ------------------------ |
| Sky Light   | #EBF4FA → #D6EAF8 | Page background gradient |
| Sky Deep    | #1E3A5F           | Headings                 |
| Sky Muted   | #5B7FA5           | Secondary text           |
| Sun Primary | #FDB813           | Primary CTAs             |
| Sun Warm    | #F5A623           | CTA hover                |
| Coral       | #EF6B6B           | Errors                   |
| Cloud       | #FFFFFF           | Cards                    |

Typography: Inter (Google Fonts).

## Architecture

```
frontend/
├── src/
│   ├── api/           # Axios + endpoint clients
│   ├── components/    # UI + layout components
│   ├── contexts/      # AuthContext
│   ├── hooks/         # TanStack Query hooks
│   ├── pages/         # Route components
│   ├── types/         # TypeScript types
│   ├── App.tsx
│   └── main.tsx
└── tailwind.config.js
```

## Backend Additions Required

To support the frontend, the following endpoints must be added
to the backend (small additions, ~30 min each):

- `GET /api/transactions` — list all transactions
- `GET /api/transactions/{id}` — get one transaction with its entries
- `GET /api/config` — returns feature flags (e.g., `publicRegistration`)
- `POST /api/auth/register` — modified to honour the registration
  feature flag

## Configurable Registration

A feature flag `daybook.registration.public` controls whether
registration is open or admin-only:

- `true`: anyone can register via `/register`
- `false` (default): only ADMINs can create users

This is implemented via environment variable `REGISTRATION_PUBLIC`
to keep one codebase serving both modes.

## Build Order

| Phase | Work                                                | Sessions |
| ----- | --------------------------------------------------- | -------- |
| 1     | Project setup, Tailwind, base components            | 1        |
| 2     | AuthContext, Login, ProtectedRoute                  | 1        |
| 3     | Accounts list + Create Account form                 | 1        |
| 4     | New Transaction page (centrepiece)                  | 2        |
| 5     | Transactions list + detail (with backend additions) | 2        |
| 6     | Reports page (3 tabs)                               | 1        |
| 7     | Polish, error handling, empty states                | 1        |
| 8     | Frontend tests (Vitest + React Testing Library)     | 1-2      |

**Estimated total: 10-12 sessions.**

## ADRs Planned

- **ADR-006:** Frontend stack (React + Vite + TypeScript + Tailwind)
- **ADR-007:** Frontend state management (TanStack Query + Context)
- **ADR-008:** JWT storage strategy (localStorage vs httpOnly cookie)
- **ADR-009:** Configurable registration (feature flag pattern)
