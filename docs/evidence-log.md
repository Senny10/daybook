# Daybook — Promotion Evidence Log

A running record of work on Daybook and what it demonstrates against
the Made Tech competency framework, Mid level.

Updated at the end of each working session. Not polished prose —
raw notes my future self (and the promotion panel) can mine.

---

## Day 1 — 2026-04-17

### What I did
- Clarified the promotion target (Associate → Mid) and mapped each
  personal goal to a specific framework descriptor
- Chose the tech stack deliberately: Kotlin + Spring Boot + PostgreSQL
    + React + AWS (with Terraform)
- Named the project: Daybook (a nod to the traditional accounting
  term for a chronological transaction log)
- Learned the fundamentals of double-entry bookkeeping: the accounting
  equation, account types, and debit/credit rules for each type
- Designed the system architecture at two levels:
    - External (React ↔ API ↔ PostgreSQL)
    - Internal (Controller → Service → Repository → Database)
- Wrote ADR-001 documenting the choice of Kotlin + Spring Boot,
  with four alternatives evaluated and rejected
- Wrote docs/architecture.md with three Mermaid diagrams
  (system, layered, deployment)
- Created the GitHub repository (public) and made the first two commits
- Caught and cleaned up stray .DS_Store files that I had unintentionally
  committed; updated .gitignore to prevent recurrence

### What this demonstrates (framework mapping)

**Architecture — Mid**  
*"Can design simple architectures for basic projects."*
- Evidence: `docs/architecture.md` — a written architecture document
  with layered design and explicit layer responsibilities
- Evidence: `docs/adr/001-backend-stack.md` — documented architectural
  decision with alternatives and trade-offs

**Working in teams / managing documentation — Associate+**  
*"Understands the importance of managing documentation around and within code."*
- Evidence: Set up docs/ folder hierarchy and committed written
  decisions before any code was written

**Communication styles — Associate+**  
*"Adapts communication style depending on audience and context."*
- Evidence: ADR written for a technical audience (panel/future engineers),
  README tone pitched differently

### Honest gaps to flag
- I reached for Googled content when asked to evaluate Ruby on Rails,
  rather than admit I hadn't used it. Reworked with "Not evaluated
  in depth" phrasing instead — a more honest Mid-level framing.
- New to Kotlin and Spring Boot; the whole technical stack is learning
  territory, not existing expertise.
- AWS services (ALB, ECS, RDS, Route 53, CloudFront) are only vaguely
  familiar; need hands-on learning in Month 3.

### Decisions still open
- Database ORM choice (JPA vs. JdbcClient) — will decide in Week 2
  when we hit persistence
- Monorepo vs. multi-repo — using monorepo; will write ADR-002
  to document this
- AWS certification pathway — plan to start studying for Cloud
  Practitioner (CLF-C02) from Week 5, target exam ~Week 9
---

## Day 2 — 2026-04-20

### What I did
- Wrote ADR-002 documenting the decision to use a monorepo structure
  for the project. This time I wrote it once compared
  to Day 1's ADR which took 3 iterations.
- Replaced the default README with a proper project overview including
  tech stack, architecture and project status.
- Generated a Spring Boot project using start.spring.io with three dependencies:
  Spring Web, Spring Data JPA, and PostgreSQL Driver.
- Hit a build error because Spring was searchng for JDK 17.
  Chose JDK 21 because it is the officially support LTS version.
- Created my first endpoint: GET /api/hello which returns {"message":"Hello, Daybook!"}.
- Learned what @SpringBootApplication does — it combines three annotations:
  @Configuration, @EnableAutoConfiguration, @ComponentScan.
- Caught a potential .gitignore issue by running git status before
  committing. Found IntelliJ .idea/ files in the untracked list and
  added .idea/ to the root .gitignore before they were committed.
- Fixed a messy Git history where I had 3 identical commits.
  Used git reset --soft to undo the commits but kept the files staged.

### What this demonstrates (framework mapping)

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: ADR-002 written independently in fewer iterations than
  ADR-001, showing growing competence in documenting architectural
  decisions

**Working in teams / managing documentation — Associate+**
*"Understands the importance of managing documentation around and
within code."*
- Evidence: README now serves as a proper project landing page with
  tech stack, architecture links, and project status

**Automated testing and refactoring — Associate**
*"Demonstrates writing code 'tests-first'"*
- Evidence: Not yet — test directory exists but no tests written.
  This is a gap to address in upcoming sessions.

**Cloud Engineering — Associate+**
*"Can independently implement one or more cloud services"*
- Evidence: Proactively chose JDK 21 (LTS) over JDK 23 for AWS
  deployment compatibility — demonstrates forward-thinking about
  deployment constraints

### Honest gaps to flag
- I didn't the "magic" contained within @SpringBootApplication
- I struggle with wording and structuring my sentence for ADRs and the evidence logs. 

### Decisions still open
- Database setup via Docker — next session
- Domain model design (Account, Transaction, Entry) — next session
- Remove temporary database auto-configuration exclusion once
  PostgreSQL is running