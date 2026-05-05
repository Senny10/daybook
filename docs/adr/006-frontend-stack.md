# ADR-006: Frontend Stack — React + Vite + TypeScript + Tailwind

## Status
Accepted — 2026-05-05

## Context
Daybook needs a frontend that connects to the existing Spring Boot API.
We need to choose a framework, language, build tool, and styling approach
that demonstrates Mid-level engineering competency and is appropriate
for a financial application.

## Decision
We will use React 18 with Vite, TypeScript, Tailwind CSS, TanStack Query,
React Hook Form + Zod, and Axios.

## Alternatives Considered

### Next.js
- ✅ Server-side rendering, file-based routing, production-ready
- ❌ Adds significant complexity (SSR, API routes) that Daybook doesn't need
- ❌ Overkill for a portfolio project backed by a dedicated Spring Boot API
- ❌ Harder to demonstrate separation of concerns when frontend and
  backend logic can blur

### Create React App (CRA)
- ✅ Simple setup, officially supported React starter
- ❌ No longer actively maintained by the React team
- ❌ Uses Webpack — significantly slower dev server than Vite
- ❌ Poor developer experience compared to modern alternatives

### Vue.js
- ✅ Gentler learning curve than React
- ❌ Smaller ecosystem and job market share in UK compared to React
- ❌ Less relevant to Made Tech's typical client stack

### Plain JavaScript (no framework)
- ✅ No dependencies, maximum control
- ❌ No component reusability, no state management, poor scalability
- ❌ Does not demonstrate modern frontend engineering practices

## Consequences

### Easier
- Vite's dev server starts near-instantly via native ES modules
- TypeScript catches type errors at compile time — critical for
  financial data where wrong types mean wrong money
- Tailwind enables rapid UI iteration without leaving the component
- TanStack Query handles loading, error, and cache states automatically
- Zod validation mirrors backend Bean Validation — consistent rules
  on both sides of the API

### Harder
- TypeScript adds a learning curve for developers new to typed JavaScript
- Tailwind class names can become verbose in complex components
- TanStack Query adds conceptual overhead (queries, mutations, cache
  invalidation) compared to simple useState + useEffect

### Security Note
JWT storage strategy is a separate decision — see ADR-008.