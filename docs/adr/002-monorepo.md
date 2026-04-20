# ADR-002:  Monorepo versus Multi-repo

## Status
Accepted

## Context
Daybook has two codebases — backend and frontend — and we need to decide how to organise them in Git.

## Decision
We will use a monorepo structure, with both the backend (Kotlin/Spring Boot) and frontend (React) in a single Git repository.

## Alternatives Considered

### Monorepo
 - ✅Simpler for a solo developer
 - ✅Easier to keep docs alongside code
 - ✅One PR can change both frontend and backend together

### Multi-repo
- ✅The two codebases can be designed and built independent of each other - loosely coupled
- ✅Each repo has independent CI/CD pipelines
- ✅Teams can work independently without merge conflicts
- ❌Requires more maintenance with the two codebase living in separate repos
- ❌Daybook is a solo project — the benefits of multi-repo (team independence, separate deploy cycles) don't apply when there's one developer

## Consequences

### Easier
- Maintenance of the codebase for a solo engineer becomes easier.

### Harder
- Frontend and backend code living side-by-side makes it tempting to create tight coupling between them — e.g., importing backend types directly into frontend code. We'll need discipline to keep them independent.

