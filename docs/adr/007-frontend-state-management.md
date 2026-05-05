# ADR-007: Frontend State Management — TanStack Query + Context

## Status
Accepted — 2026-05-05

## Context
The Daybook frontend needs to manage two types of state:
1. **Server state** — data fetched from the API (accounts,
   transactions, reports). This data lives on the server and
   needs loading, error, and caching behaviour.
2. **Client state** — local UI state (current user, JWT token,
   whether a form is open). This data lives only in the browser.

We need to decide how to manage both.

## Decision
- **Server state:** TanStack Query (React Query)
- **Client state:** React Context (AuthContext)

## Alternatives Considered

### Redux (+ Redux Toolkit)
- ✅ Industry standard for large applications
- ✅ Excellent developer tools
- ❌ Significant boilerplate even with Redux Toolkit
- ❌ Overkill for a project of this size
- ❌ Treats server state and client state the same way —
  requires manual cache invalidation and loading state management

### Zustand
- ✅ Lightweight, minimal boilerplate
- ✅ Good for client state
- ❌ Still requires manual handling of server state concerns
  (loading, errors, caching, refetching)
- ❌ Not evaluated in depth for this project

### useState + useEffect (no library)
- ✅ No dependencies, maximum control
- ❌ Every component manages its own loading/error state
- ❌ No caching — same data fetched repeatedly on navigation
- ❌ Race conditions possible with multiple concurrent requests
- ❌ Does not scale beyond simple apps

### TanStack Query + Context (chosen)
- ✅ TanStack Query handles all server state concerns:
  loading, error, caching, background refetching,
  cache invalidation
- ✅ React Context handles simple client state (auth) cleanly
  without additional dependencies
- ✅ Queries are declarative — components describe what data
  they need, not how to fetch it
- ✅ 5-minute stale time means navigating back to Accounts
  doesn't re-fetch if data is fresh

## Consequences

### Easier
- Loading and error states handled automatically per query
- Cache invalidation via queryKey — calling refetch() after
  creating an account updates the list immediately
- AuthContext is simple — just token + user + login/logout functions
- No global store to reason about for most UI state

### Harder
- Two different mental models for server vs client state
- TanStack Query cache invalidation must be managed carefully —
  stale data can appear if queryKeys aren't structured correctly
- Context re-renders all consumers when value changes — mitigated
  by keeping AuthContext lean (only token + user)