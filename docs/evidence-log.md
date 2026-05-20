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
---

## Day 3 — 2026-04-22

### What I did
- Set up PostgreSQL 16 using Docker Compose with credentials
  managed via environment variables
- Created three JPA entities: Account, Transaction, and Entry
- Created two enum classes: AccountType (ASSET, LIABILITY, EQUITY,
  REVENUE, EXPENSE) and EntryType (DEBIT, CREDIT)
- Used BigDecimal instead of Double for monetary amounts — exact
  decimal arithmetic, no floating-point errors
- Established database relationships:
  - Entry has ManyToOne relationship to Transaction
  - Entry has ManyToOne relationship to Account
  - Transaction has OneToMany relationship to Entries
- Verified Hibernate created all three tables by inspecting
  PostgreSQL directly via psql
- Independently identified credential exposure risk before
  committing to the public repo — replaced hardcoded passwords
  with environment variables and added .env to .gitignore
- Learned that deleting an Account with existing Entries fails
  because PostgreSQL enforces referential integrity via foreign
  key constraints at the database level
- Confirmed full stack works: Docker (PostgreSQL) → Spring Boot
  (Hibernate) → REST endpoint

### What this demonstrates (framework mapping)

**Secure coding — Mid**
*"Regularly applies secure coding best practices and identifies
security flaws in code reviews."*
- Evidence: Independently identified credential exposure risk
  before committing. Implemented .env pattern with .env.example
  and explicit .gitignore entries. Documented decision in ADR-004.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Designed three-entity domain model (Account,
  Transaction, Entry) correctly implementing double-entry
  bookkeeping relationships. Entity relationships enforce
  the accounting model at the database level, not just
  in application code.

**Databases — Associate+**
*"Understands basic database design principles such as tables,
primary keys, foreign keys, and indexes."*
- Evidence: Verified table structure in PostgreSQL directly —
  confirmed primary keys, foreign keys, unique constraints,
  and check constraints all created correctly from JPA entities.

**Security & Compliance — Associate+**
*"Understands basic security concepts such as authentication,
authorisation, encryption, and secure coding practices."*
- Evidence: Identified and mitigated credential exposure before
  it became a real vulnerability. Applied industry-standard
  .env pattern independently.

### Honest gaps to flag
- UUID vs Long ID trade-off — needed prompting to think about
  the security implications of sequential IDs. ADR-003 documents
  the decision and future mitigation.
- Balance calculation logic — understand the concept (DEBIT minus
  CREDIT depends on account type) but haven't written the code yet.
- Tired by end of session — evidence log and ADR-004 written with
  assistance rather than independently. Will demonstrate independent
  ADR writing in a future session.

### Decisions still open
- ADR-003: UUID vs Long — accepted Long for now, UUID public ID
  mitigation planned for future iteration
- ADR-004: Credential management — accepted, .env pattern in place
- Repository layer — next session
- Service layer validation (debits must equal credits) — next session
- First real CRUD endpoints — next session
---

## Day 4 — 2026-04-23

### What I did
- Created Repository layer: AccountRepository, TransactionRepository,
  EntryRepository using Spring Data JPA
- Learned that Spring Data JPA generates query implementations
  automatically from method names (e.g. findByName → WHERE name = ?)
- Created Service layer: AccountService and LedgerService
- Implemented five business validation rules in LedgerService:
  - Must have at least two entries
  - Must have at least one DEBIT and one CREDIT entry
  - Debits must equal credits
  - Reference must be unique
  - All amounts must be positive
- Created DTOs (Data Transfer Objects) to decouple the API from
  the domain model — separate request/response objects for each endpoint
- Created AccountController and TransactionController with endpoints:
  - POST /api/accounts
  - GET /api/accounts
  - GET /api/accounts/{id}
  - GET /api/accounts/{id}/balance
  - POST /api/transactions
- Added GlobalExceptionHandler — returns HTTP 400 for business rule
  violations and HTTP 500 for unexpected errors
- Completed a full TDD red-green-refactor cycle:
  - RED: wrote three failing tests for entry type validation
  - GREEN: implemented validateEntryTypes() to make tests pass
  - REFACTOR: extracted validation into named private methods
- Verified double-entry validation works end-to-end via curl
- Hit a Mockito/JDK 23 compatibility issue with JPA lazy-loading
  proxies on the happy path test — made a pragmatic decision to
  defer to @DataJpaTest integration test in a later session

### What this demonstrates (framework mapping)

**Automated Testing & Refactoring — Mid**
*"Writes comprehensive unit tests... regularly uses red-green-refactor
cycle to ensure code is maintainable and robust."*
- Evidence: LedgerServiceTest.kt — three unit tests written before
  implementation. validateEntryTypes() written to make failing tests
  pass. Full red-green-refactor cycle documented and committed.

**Secure Coding — Mid**
*"Can implement role-based access control (RBAC) and secure API
authentication mechanisms."*
- Evidence: Not yet — JWT/RBAC planned for Week 5. Current session
  established the endpoint structure that security will wrap.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Full layered architecture now implemented and working —
  Controller → Service → Repository → Database. Each layer has
  exactly one responsibility. DTOs enforce clean separation between
  API contracts and domain model.

**APIs and Integration — Associate+**
*"Can independently consume and integrate with basic APIs."*
- Evidence: Five REST endpoints built and verified via curl. Proper
  HTTP status codes (201 Created, 400 Bad Request, 500 Internal
  Server Error) returned consistently via GlobalExceptionHandler.

**Databases — Mid**
*"Has a solid understanding of database design, optimisation, and
integration into applications."*
- Evidence: Custom @Query using JPQL for balance calculation.
  Repository layer correctly separates data access from business
  logic. Foreign key constraints verified working in PostgreSQL.

### Honest gaps to flag
- Happy path unit test blocked by Mockito/JDK 23 compatibility
  issue with JPA lazy-loading proxies. Root cause identified —
  deferred to integration test session rather than fighting
  framework incompatibility indefinitely.
- TDD cycle was retrofitted — service layer was written before
  tests in the initial pass. One genuine red-green-refactor cycle
  completed for validateEntryTypes(). Will apply TDD from the
  start in future features.
- @Transactional behaviour not yet verified under failure
  conditions — rollback works in theory, needs integration test
  to confirm.

### Decisions still open
- JWT authentication + RBAC — Week 5
- Integration tests with @DataJpaTest + H2 — next session
- Input validation with @Valid and Bean Validation — next session
- React frontend — Month 2
- AWS deployment via Terraform — Month 3
---

## Day 5 — 2026-04-30

### What I did
- Fixed local development environment — configured IntelliJ EnvFile
  plugin to load .env file automatically for run configurations
- Added Bean Validation to all request DTOs using
  spring-boot-starter-validation:
  - CreateAccountRequest: @NotBlank, @Size on name; @NotNull on type
  - CreateTransactionRequest: @NotNull on date; @NotBlank on
    description and reference; @Valid cascade on entries
  - EntryRequest: @Positive on accountId; @DecimalMin and @Digits
    on amount; @NotNull on type
- Added @Valid to AccountController and TransactionController
- Added two new exception handlers to GlobalExceptionHandler:
  - MethodArgumentNotValidException → 400 with all field errors
  - HttpMessageNotReadableException → 400 for missing/invalid fields
- Added H2 in-memory database for test isolation
- Created src/test/resources/application.properties to configure
  H2 for test runs
- Wrote 5 integration tests in AccountRepositoryTest using
  @DataJpaTest:
  - findByName: found and not found cases
  - existsByName: true and false cases
  - findByType: filters correctly by account type
  - sumAmountByAccountIdAndType: custom @Query verified correct
- All 9 tests passing: 1 context, 5 repository, 3 service (100%)
- Cleaned up invalid account (blank name) that was created before
  validation was added

### What this demonstrates (framework mapping)

**Automated Testing & Refactoring — Mid**
*"Writes comprehensive unit tests and integration tests."*
- Evidence: 9 tests passing across 3 test classes. Mix of unit
  tests (LedgerServiceTest — no database) and integration tests
  (AccountRepositoryTest — H2 in-memory). Test report screenshot
  saved showing 100% pass rate.

**Secure Coding — Mid**
*"Regularly applies secure coding best practices."*
- Evidence: Input validation added to all endpoints — blank names,
  missing required fields, invalid amounts all rejected with 400
  before reaching the service layer. Validation is the first line
  of defence against malformed data.

**APIs and Integration — Associate+**
*"Can independently consume and integrate with basic APIs."*
- Evidence: All five endpoints now validate input correctly and
  return consistent, structured error responses with specific
  field-level messages.

**Working in teams / managing documentation — Associate+**
*"Understands the importance of managing documentation around
and within code."*
- Evidence: Configured EnvFile plugin and documented the approach
  so any developer cloning the repo can follow the same setup
  using .env.example.

### Honest gaps to flag
- H2Dialect warning in test output — removed explicit dialect
  setting from test application.properties as H2 auto-detects it
- Test report shows ApiApplicationTests with 1 test — this is
  Spring Boot's generated context load test, not something I wrote
- Evidence log and README updated with assistance to preserve
  session time — will aim to write independently in future sessions

### Decisions still open
- AccountService unit tests — Day 6
- Controller tests with @WebMvcTest — Day 6
- JWT authentication + RBAC — Week 5
- React frontend — Month 2
- AWS deployment via Terraform — Month 3
---

## Day 6 — 2026-04-30

### What I did
- Wrote 6 unit tests for AccountService (AccountServiceTest):
  - createAccount happy path with doReturn pattern for save stub
  - createAccount duplicate name throws with correct message
  - verify save never called on duplicate (negative assertion)
  - getAccountById returns account when found
  - getAccountById throws when not found (id in error message)
  - getAllAccounts returns full list
  - getAccountsByType filters correctly
- Wrote 7 controller tests for AccountController (AccountControllerTest):
  - GET /api/accounts returns 200 with correct JSON structure
  - POST /api/accounts returns 201 with created account
  - POST /api/accounts returns 400 on blank name (validation layer)
  - POST /api/accounts returns 400 on duplicate name (service layer)
  - GET /api/accounts/{id} returns 200 with correct account
  - GET /api/accounts/{id} returns 400 when not found
  - GET /api/accounts/{id}/balance returns 200 with balance
- Fixed deprecated @MockBean → @MockitoBean (Spring Boot 3.4.x)
  by accepting IntelliJ's suggested import path over my initial guess
- Full test suite: 22 tests across 5 classes, 100% passing
- Learned that @DataJpaTest > @WebMvcTest > MockitoExtension in terms
  of Spring context loaded — less context = faster test

### What this demonstrates (framework mapping)

**Automated Testing & Refactoring — Mid**
*"Writes comprehensive unit tests and integration tests across
all layers of the application."*
- Evidence: 22 tests spanning Controller (@WebMvcTest), Service
  (MockitoExtension), and Repository (@DataJpaTest) layers.
  Test report screenshots saved showing 9 → 15 → 22 test growth
  across Days 5-6. Mix of positive and negative assertions.
  verify() used to assert save() never called on invalid requests.

**Secure Coding — Associate+**
*"Applies secure coding best practices."*
- Evidence: Controller tests explicitly verify that validation
  errors return 400 (not 500) — confirming the security boundary
  between user error and system error is correctly implemented.

**APIs and Integration — Mid**
*"Understands and can implement RESTful API design principles."*
- Evidence: Controller tests verify correct HTTP status codes
  (200, 201, 400) for each scenario — demonstrating understanding
  of REST conventions beyond just making endpoints work.

### Honest gaps to flag
- TransactionController has no tests yet — only AccountController
  covered. Will add in a future session.
- doReturn pattern required for save() stubs due to Mockito/JDK 23
  compatibility — noted consistently across sessions.
- Evidence log and README written with assistance to preserve
  session time.

### Decisions still open
- TransactionController tests — next session
- JWT authentication + RBAC — Week 5
- React frontend — Month 2
- AWS deployment via Terraform — Month 3
---
## Day 7 — 2026-04-30

### What I did
- Wrote 5 controller tests for TransactionController
  (TransactionControllerTest):
  - POST /api/transactions: returns 201 with created transaction
  - POST /api/transactions: returns 400 when description is blank
  - POST /api/transactions: returns 400 when debits != credits
  - POST /api/transactions: returns 400 when entries list is empty
  - POST /api/transactions: returns 400 when reference already exists
- Full test suite now 27 tests across 6 classes, 100% passing
- Built three financial reporting endpoints:
  - GET /api/reports/trial-balance — all accounts with debit/credit
    balances, isBalanced flag verifies double-entry integrity
  - GET /api/reports/profit-and-loss — Revenue minus Expenses =
    netIncome, isProfit flag
  - GET /api/reports/balance-sheet — Assets vs Liabilities + Equity,
    isBalanced flag
- Verified all three reports against real data from Day 4 transaction
- Balance Sheet correctly reports isBalanced: false — no Equity
  accounts in current data, accurate reporting not a bug
- Understood closing entries concept — Revenue/Expense balances
  flow into Equity at period end (planned for later session)

### What this demonstrates (framework mapping)

**Automated Testing — Mid**
*"Writes comprehensive unit and integration tests."*
- Evidence: Test suite complete — 27 tests, 6 classes, all layers,
  100% passing. Growth documented: 9 → 15 → 22 → 27 across Days 5-7.
  Three test report screenshots saved showing consistent progress.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Reporting layer added cleanly without touching existing
  layers. ReportingService reads from AccountRepository and
  EntryRepository — correct separation of concerns maintained.

**Domain Knowledge — Associate+**
*"Applies domain knowledge to technical decisions."*
- Evidence: Trial Balance, P&L, and Balance Sheet correctly implement
  real accounting concepts. isBalanced flags detect data integrity
  issues. Balance Sheet accurately identifies missing Equity accounts
  rather than hiding the gap.

### Honest gaps to flag
- ReportingController has no tests yet — deferred to next session
- Balance Sheet isBalanced: false in current data — expected,
  needs Equity accounts and closing entries to resolve fully
- Evidence log and README written with assistance to preserve
  session time

### Decisions still open
- ReportingController tests — next session
- Closing entries implementation — later session
- JWT authentication + RBAC — Week 5
- React frontend — Month 2
- AWS deployment via Terraform — Month 3
---

## Day 8 — 2026-05-01

### What I did
- Added ReportingController tests (3 tests) — closed last testing gap
- Full test suite: 30 tests across 4 packages, 100% passing
- Implemented JWT authentication and RBAC with Spring Security:
  - Added spring-boot-starter-security, jjwt-api, jjwt-impl,
    jjwt-jackson dependencies
  - Created User entity with UserRole enum (USER, ADMIN)
  - Created UserRepository with findByUsername and existsByUsername
  - Created JwtService — generates and validates JWT tokens using
    HMAC-SHA, reads secret from environment variable
  - Created JwtAuthenticationFilter — validates JWT on every request,
    sets Spring Security context using
    RequestAttributeSecurityContextRepository (required for
    Spring Security 6)
  - Created DaybookUserDetailsService — loads users from PostgreSQL
  - Created SecurityConfig — stateless session, CSRF disabled,
    endpoint protection rules, BCrypt password encoder
  - Created AuthService — register (BCrypt hash) and login (JWT)
  - Created AuthController — POST /api/auth/register and login
  - Created RegisterRequest, LoginRequest, AuthResponse DTOs
  - Added JWT_SECRET and JWT_EXPIRATION to .env and .env.example
- Wrote ADR-005 documenting JWT vs session vs OAuth2 vs API keys
- Verified complete auth flow end-to-end:
  - No token → 401 Unauthorized
  - USER token + POST /api/accounts → 403 Forbidden (RBAC working)
  - ADMIN token + POST /api/accounts → 201 Created
  - ADMIN token + GET /api/accounts → 200 OK
- Debugged Spring Security 6 context persistence issue —
  RequestAttributeSecurityContextRepository required to persist
  authentication through the filter chain

### What this demonstrates (framework mapping)

**Secure Coding — Mid**
*"Can implement role-based access control (RBAC) and secure API
authentication mechanisms (e.g., OAuth, JWT) independently
in a project."*
- Evidence: Working JWT authentication and RBAC in a real codebase.
  Two roles (USER, ADMIN) with different permissions enforced by
  Spring Security. Passwords BCrypt-hashed. JWT secret in
  environment variables. ADR-005 documents the design decisions
  and trade-offs considered. Verified end-to-end with curl.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Security layer added cleanly as a separate package
  (com.daybook.api.security) without touching existing business
  logic. Filter chain, service, and config separate
---

## Day 9 — 2026-05-04

### What I did
- Fixed 16 controller tests broken by Spring Security addition:
  - Created TestSecurityConfig to permit all requests in test context
  - Added @MockitoBean for JwtService and DaybookUserDetailsService
  - Added JWT properties to test application.properties
  - Added @ActiveProfiles("test") to ApiApplicationTests
- Added frontend plan to docs/frontend-plan.md — realigned from
  personal finance app to double-entry bookkeeping tool
- Added three new backend endpoints to support the frontend:
  - GET /api/transactions — lists all transactions with entries
    using LEFT JOIN FETCH to avoid N+1 problem
  - GET /api/transactions/{id} — single transaction with entries
  - GET /api/config — feature flags (no auth required)
- Created EntryResponse and TransactionDetailResponse DTOs
- Added findAllWithEntries() and findByIdWithEntries() to
  TransactionRepository using JPQL JOIN FETCH
- Added configurable registration property
  (daybook.registration.public)
- Fixed ConfigController @Value injection issue —
  property injection pattern required for Boolean @Value in Kotlin
- Test suite: 34 tests, 100% passing (4 new tests added)
- Learned about the N+1 problem — one query to get N records,
  then N more queries for related data; solved with JOIN FETCH

### What this demonstrates (framework mapping)

**Automated Testing — Mid**
*"Writes comprehensive unit and integration tests."*
- Evidence: Test suite grew from 30 to 34. Fixed 16 broken tests
  after major architectural change (Spring Security). TestSecurityConfig
  pattern established for reuse across all future controller tests.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: N+1 problem identified and solved at the repository layer
  using JPQL JOIN FETCH. Frontend plan written with deliberate scope
  decisions documented (what we're building and why we cut certain
  features).

**APIs and Integration — Mid**
*"Understands and can implement RESTful API design principles."*
- Evidence: Three new endpoints follow existing REST conventions.
  Config endpoint correctly identified as public (no auth).
  TransactionDetailResponse includes nested EntryResponse — correct
  representation of the domain relationship.

**Secure Coding — Mid**
*"Regularly applies secure coding best practices."*
- Evidence: Configurable registration defaults to false (secure by
  default). Config endpoint deliberately public — returns only
  non-sensitive feature flags. JWT properties kept in environment
  variables throughout.

### Honest gaps to flag
- Auth endpoint tests (register/login) still not written —
  carried over from Day 8
- Configurable registration backend logic not yet implemented —
  currently register is always public regardless of flag
- Evidence log and README written with assistance to preserve
  session time

### Decisions still open
- Configurable registration enforcement in AuthService — Day 10
- React frontend setup — Day 10
- JWT authentication + RBAC frontend implementation — Day 10+
- AWS deployment via Terraform — Month 3

---
## Days 10 & 11 — 2026-05-05

### What I did
- Enforced configurable registration in AuthService:
  - REGISTRATION_PUBLIC=true → anyone can register
  - REGISTRATION_PUBLIC=false → only ADMINs can register (403)
- Wrote 7 auth endpoint tests (AuthControllerTest) — closed last
  testing gap. Full backend test suite: 41 tests, 100% passing
- Built React frontend foundation:
  - Vite + TypeScript + Tailwind + Sky/Sun palette configured
  - Login page with Zod validation, error handling, loading state
  - AuthContext: JWT in localStorage, user state, role helpers
  - Axios client with JWT request interceptor and 401 response
    interceptor (auto-redirect to login on token expiry)
  - ProtectedRoute: unauthenticated → /login, wrong role → /accounts
  - Header: navigation, username/role display, role-based menu
  - AppLayout: consistent page wrapper
  - Accounts page: lists accounts with colour-coded type badges,
    create form visible to ADMIN only (RBAC in UI)
- Wrote ADR-006 (frontend stack), ADR-007 (state management),
  ADR-008 (JWT storage strategy)
- Verified full stack end-to-end in browser:
  - Login → JWT stored → redirect to /accounts
  - Real accounts from PostgreSQL displayed in React
  - ADMIN role correctly shows admin-only UI elements

### What this demonstrates (framework mapping)

**Secure Coding — Mid**
*"Can implement RBAC and secure API authentication independently."*
- Evidence: RBAC implemented in both backend (Spring Security) AND
  frontend (ProtectedRoute + conditional UI rendering). JWT stored
  securely with localStorage, trade-off documented in ADR-008.
  Configurable registration enforced server-side.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Frontend architecture mirrors backend layered approach:
  api/ (data access) → contexts/ (state) → pages/ (presentation).

---

## Day 12 — 2026-05-06

### What I did
- Built Transactions page with search/filter and real-time count
- Built New Transaction modal with:
  - 2 entry rows by default, + Add entry for more
  - Live balance indicator (red difference → green ✓ Balanced)
  - Post Transaction button disabled until debits = credits
  - Automatic list refresh after successful post (TanStack Query)
- Built Reports page with three tabs:
  - Trial Balance: account table, totals, isBalanced badge
  - Profit & Loss: Revenue, Expenses, Net Income, isProfit badge
  - Balance Sheet: Assets vs Liabilities + Equity, honest
    "Incomplete data" warning when equity accounts missing
- Created API clients: transactions.ts and reports.ts
- Verified full double-entry flow end-to-end in browser:
  - Posted £200 transaction via modal
  - Trial Balance updated to £700 debits = £700 credits
  - P&L shows £700 net income
  - Balance Sheet correctly identifies missing equity accounts

### What this demonstrates (framework mapping)

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Frontend architecture mirrors backend — api/ layer
  separates concerns cleanly. Modal component encapsulates all
  transaction posting logic. Reports page uses tab pattern to
  surface three distinct financial statements cleanly.

**APIs and Integration — Mid**
*"Can independently consume and integrate with basic APIs."*
- Evidence: All Daybook API endpoints now consumed by React
  frontend. TanStack Query handles caching, loading, and
  cache invalidation automatically. Full stack verified
  end-to-end with real data flowing from PostgreSQL through
  Spring Boot to React.

**Domain knowledge — Associate+**
*"Applies domain knowledge to technical decisions."*
- Evidence: Balance Sheet "Incomplete data" warning correctly
  identifies missing equity accounts — not hidden or suppressed.
  Trial Balance isBalanced flag surfaced prominently. Double-entry
  rule enforced in UI (disabled submit) AND backend (validation).

### Honest gaps to flag
- Frontend tests not yet written (Vitest + RTL planned)
- Balance indicator could show which side is heavier
  (e.g. "£200 more in debits") — noted for polish week
- Background images committed to repo — should be in
  public/ not src/assets/ for optimal loading
- Evidence log and README written with assistance

### Decisions still open
- Frontend tests — Day 13
- Seed script — polish week
- Background image implementation — polish week
- AWS deployment via Terraform — Month 3
---

## Day 13 — 2026-05-06

### What I did
- Set up Vitest + React Testing Library + MSW for frontend testing
- Created localStorage mock for AuthContext test compatibility
- Wrote 5 LoginPage tests:
  - Form renders correctly
  - Validation error when username empty
  - Validation error when password empty
  - Error message shown on failed login (MSW override)
  - Button enabled when form is valid
- Wrote 6 NewTransactionModal tests:
  - Two entry rows render by default
  - Post Transaction disabled when unbalanced
  - Add entry row via + Add entry button
  - Difference indicator shown when unbalanced
  - Balanced indicator shown when debits = credits
  - Post Transaction enabled when balanced
- 11 frontend tests passing, 100% success rate
- MSW intercepts API calls at network level — more realistic
  than mocking Axios directly

### What this demonstrates (framework mapping)

**Automated Testing — Mid**
*"Writes comprehensive unit and integration tests."*
- Evidence: Full test coverage across backend (41 tests) AND
  frontend (11 tests). Frontend tests verify user-visible
  behaviour using React Testing Library philosophy. MSW used
  for realistic API mocking. Total: 52 tests across the stack.

**Secure Coding — Associate+**
*"Applies secure coding best practices."*
- Evidence: LoginPage tests explicitly verify error messages
  shown on failed authentication — confirming the security
  boundary between valid and invalid credentials is correctly
  communicated to users.

### Honest gaps to flag
- Only two components tested — LoginPage and NewTransactionModal
- AccountsPage and TransactionsPage have no frontend tests yet
- Loading state test replaced with simpler assertion due to
  timing issues in jsdom — noted for future improvement
- Evidence log and README written with assistance

### Decisions still open
- More frontend tests — polish week
- Seed script — polish week
- Background images — polish week
- AWS deployment via Terraform — Month 3

---

## Day 14 — 2026-05-11

### What I did
- Set up GitHub Actions CI workflows:
  - Backend CI: JDK 21 + Gradle cache + 41 tests on every PR
  - Frontend CI: Node 20 + npm ci + 11 tests on every PR
  - Both workflows pass in under 2 minutes
- Configured branch protection via GitHub Rulesets:
  - Direct pushes to main blocked
  - Both CI checks required before merge
  - Verified: direct push rejected with GH013 error
- Completed first full PR workflow:
  - Created feature/seed-script branch
  - Committed DataSeeder.kt
  - Opened PR #1
  - Both CI checks passed automatically
  - Merged to main with "2 checks passed"
  - Feature branch deleted
- Built seed script (DataSeeder.kt):
  - @Profile("seed") — only runs when seed profile active
  - Idempotent — skips if data already exists
  - 2 users: admin (ADMIN) and bookkeeper (USER)
  - 8 accounts covering all 5 account types
  - 5 transactions telling a complete business story:
    T1: Owner invests £10,000 (opening capital)
    T2: Buys office equipment £2,500 (asset purchase)
    T3: Issues invoice for £3,000 (revenue earned)
    T4: Customer pays invoice (cash received)
    T5: Pays rent £1,200 + salaries £4,000 (operating expenses)
- Fixed CI paths filter issue — removed paths filter so both
  checks always run on every PR regardless of which files changed

### What this demonstrates (framework mapping)

**IaC / CI-CD — Mid**
*"Understands the importance of CI/CD pipelines and has
experience contributing to or maintaining them."*
- Evidence: Two GitHub Actions workflows running automatically
  on every PR. Branch protection enforces both checks before
  merge. First PR merged with "2 checks passed" — screenshot
  saved. Full workflow demonstrated: branch → commit → PR →
  CI → merge → delete branch.

**Automated Testing — Mid**
*"Regularly applies automated testing practices."*
- Evidence: CI pipeline runs all 52 tests (41 backend + 11
  frontend) automatically on every PR. Tests are the gate —
  broken code cannot reach main.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: Seed script uses @Profile pattern — clean
  separation between demo and production configuration.
  Idempotent design prevents data duplication.

### Honest gaps to flag
- Frontend CI takes longer than backend on cold cache —
  subsequent runs use npm cache and complete in ~17s
- Paths filter removed from both workflows — both run on
  every PR regardless of which files changed. Minor
  inefficiency but necessary for required checks to work
- Evidence log and README written with assistance

### Decisions still open
- ADR-009: Configurable registration pattern
- Polish week: background images, loading skeletons
- AWS deployment via Terraform — Month 3
- AWS Cloud Practitioner certification study

---

## Day 15 — 2026-05-11

### What I did
- Wrote ADR-009 documenting configurable registration feature flag
  pattern — secure by default, explicit opt-in for public mode
- Implemented paper-cut sky/sun background images:
  - Login page: sun top-left with clouds (Image 1)
  - App shell: consistent sky background across all pages
- Added loading skeletons (animate-pulse) to:
  - AccountsPage — 3 skeleton cards while loading
  - TransactionsPage — 3 skeleton cards while loading
  - ReportsPage — skeleton rows for each tab
- Updated README Getting Started with real setup instructions
  including seed data credentials
- Practiced promotion panel elevator pitch:
  "Daybook is a full-stack double-entry bookkeeping application
  I built to demonstrate Mid-level engineering competency..."
- Completed Month 2 — core application complete

### What this demonstrates (framework mapping)

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: ADR-009 completes the ADR collection — 9 ADRs
  documenting every major decision across backend, frontend,
  security, infrastructure, and configuration. Consistent
  pattern throughout the project.

**Working in teams — Associate+**
*"Manages documentation around and within code."*
- Evidence: README Getting Started updated with real setup
  instructions, demo credentials, and seed data documentation.
  A new developer can clone and run Daybook in under 5 minutes.

### Honest gaps to flag
- Loading skeletons are functional but could be more
  pixel-perfect — good enough for a portfolio project
- ADR-009 written with assistance — pattern was implemented
  independently but documentation written collaboratively
- Month 3 (AWS, Terraform, certification) still ahead

### Decisions still open
- AWS deployment via Terraform — Month 3
- IAM policies — Month 3
- AWS Cloud Practitioner exam — Month 3
- CD pipeline (auto-deploy on merge) — Month 3

---

## Day 16 (Part 1) — 2026-05-12

### What I did
- Created fresh IAM user (sen-daybook-dev) in daybook-developers group
- Configured AWS CLI with access keys for eu-west-2 (London)
- Installed Terraform v1.11.4
- Wrote Terraform infrastructure across 5 modules:
  - networking: VPC, IGW, public/private subnets (2 AZs),
    route tables, 3 security groups (ALB→ECS→RDS chain)
  - database: RDS PostgreSQL 16, db.t3.micro, private subnets,
    publicly_accessible=false, 7-day backups
  - compute: ECR repository, ECS cluster (Fargate), task definition,
    ECS service, IAM execution role, CloudWatch logs
  - loadbalancer: ALB, target group, HTTP listener,
    health check on GET /api/config
  - secrets: AWS Secrets Manager for db credentials, jwt secret,
    db host — sensitive=true, never shown in output
- terraform init: AWS provider v5.100.0 installed
- terraform plan: 32 to add, 0 to change, 0 to destroy — clean
- terraform apply: all 32 resources created successfully
  - Fixed IAM permission gaps during apply (logs:TagResource,
    secretsmanager:CreateSecret) — added CloudWatchLogsFullAccess
    and SecretsManagerReadWrite to daybook-developers group
- Captured AWS Console screenshots of ECS, RDS, ALB, VPC
- terraform destroy: clean teardown, costs stopped
- Will re-apply before promotion panel conversation

### What this demonstrates (framework mapping)

**IaC — Mid**
*"Can describe and implement IaC using tools like Terraform
or CloudFormation. Can independently deploy a full environment."*
- Evidence: 32 AWS resources provisioned from code across 5
  modules. terraform plan/apply/destroy cycle completed. Full
  environment deployed independently. Screenshots saved showing
  real running infrastructure. Code committed to Git —
  infrastructure is repeatable.

**Cloud Security — Mid**
*"Understands shared responsibility model. Can implement
basic IAM policies and security group rules."*
- Evidence: Defence in depth via security groups (ALB→ECS→RDS
  chain). RDS private and not publicly accessible. Secrets in
  AWS Secrets Manager with sensitive=true. IAM least privilege
  — identified and fixed missing permissions during apply.
  Shared responsibility model applied: AWS manages RDS patching,
  I manage security group rules and IAM policies.

### Honest gaps to flag
- Docker image not yet built or pushed to ECR — ECS service
  running but container failing health checks (no image)
- Need to build Dockerfile and push image to ECR to get
  the app fully accessible at the ALB URL
- Will re-apply and complete deployment before panel

### Decisions still open
- Dockerfile for backend — next session
- Push image to ECR — next session
- Verify app accessible at ALB DNS name — next session
- AWS Cost Explorer analysis — next session
- AWS Cloud Practitioner exam — upcoming

----

## Day 17-18 — 2026-05-13 to 2026-05-14

### What I did
- Wrote Dockerfile for Spring Boot backend:
  - Multi-stage build: JDK 21 builder + JRE runner
  - eclipse-temurin:21 Alpine base (minimal image size)
  - Non-root daybook user for security
  - Layer caching: dependencies copied before source code
  - Built for linux/amd64 (required for ECS Fargate on Apple Silicon)
- Fixed application.properties: localhost → ${DB_HOST} for ECS
- Pushed Docker image to ECR
- Debugged and resolved multiple deployment issues:
  1. ARM64 vs AMD64 architecture mismatch — rebuilt with --platform linux/amd64
  2. Private subnets blocking ECR/Secrets Manager — moved ECS to public subnets
  3. Missing IAM permissions — added secretsmanager:GetSecretValue and ECR read
  4. Secrets Manager VPC connectivity — switched to environment variables
  5. Spring Boot 98s startup — added health_check_grace_period_seconds=120
  6. JDBC URL hardcoded localhost — fixed to use ${DB_HOST} environment variable
- Deployed Daybook to AWS successfully:
  - API responding at ALB DNS name
  - Login, accounts, transactions, reports all working
  - Seed data loaded (8 accounts, 5 transactions, 2 users)
  - Verified with curl and browser screenshots
- terraform destroy — clean teardown, costs stopped
- Added force_delete=true to ECR to prevent destroy failures

### What this demonstrates (framework mapping)

**IaC — Mid**
*"Can describe and implement IaC using tools like Terraform.
Can independently deploy a full environment."*
- Evidence: Full stack deployed to AWS via Terraform. 34 resources
  provisioned. Real debugging cycle — identified and fixed 6 separate
  deployment issues. Screenshots saved showing ECS running, RDS
  available, ALB active, app working in browser. Terraform
  plan/apply/destroy cycle completed cleanly.

**Cloud Security — Mid**
*"Understands shared responsibility model. Implements IAM
policies and security group rules."*
- Evidence: IAM execution role with least-privilege policies
  (secretsmanager:GetSecretValue, ECR read-only). Defence in depth:
  ALB→ECS→RDS security group chain. RDS not publicly accessible.
  Non-root Docker user. Identified missing IAM permissions during
  deployment and fixed independently.

**Problem Solving — Mid**
*"Independently debugs and resolves technical issues."*
- Evidence: 6 distinct deployment issues diagnosed and resolved
  independently using CloudWatch logs, AWS CLI, and Terraform state.
  Each error led to a concrete fix. No issues were left unresolved.

### Honest gaps to flag
- Frontend not deployed to AWS (still served locally via Vite proxy)
  — S3/CloudFront deployment planned as stretch goal
- ECS credentials passed as environment variables rather than
  Secrets Manager due to VPC connectivity issue. In production
  would use VPC endpoints for Secrets Manager. Trade-off documented.
- Spring Boot startup time (98s) is slow for Fargate — would
  investigate Spring AOT compilation or GraalVM native image
  for production to reduce cold start time
- Evidence log and README written with assistance

### Decisions still open
- AWS Cost Explorer analysis — next session
- AWS Cloud Practitioner exam prep — upcoming
- Concepts document — final session

---

## Day 19 — 2026-05-15

### What I did
- Set up AWS Cost Explorer — confirmed $0.00 total spend
- Created zero-spend budget alert via AWS Budgets —
  email notification triggers if any charge appears
- Added AWSBudgetsActionsWithAWSResourceControlPolicy and
  Billing policies to daybook-developers IAM group
- Wrote docs/cost-analysis.md documenting:
  - Estimated costs per service when running (~£40/month)
  - Deploy-on-demand strategy keeping actual spend at $0.00
  - Cost optimisation decisions (instance sizing, retention,
    single AZ, skip_final_snapshot)
  - Production cost considerations (NAT Gateway, multi-AZ,
    HTTPS, S3/CloudFront)
- Studied and consolidated all panel-ready concept answers
  from throughout the project

### What this demonstrates (framework mapping)

**Cloud Cost Management — Mid**
*"Understands cloud cost implications of architectural decisions."*
- Evidence: AWS Cost Explorer configured and monitored throughout
  project. Zero-spend budget alert active. Cost analysis document
  connects every architectural decision to its cost implication —
  public vs private subnets (NAT Gateway cost), single vs multi-AZ
  (availability vs cost), log retention period (observability vs
  storage). Screenshots saved showing $0.00 total spend.

**Communication — Mid**
*"Can clearly explain technical decisions to non-technical stakeholders."*
- Evidence: Cost analysis document written to be readable by
  a non-technical audience — explains trade-offs in business
  terms, not just technical ones.

### Honest gaps to flag
- Budget alert requires IAM permissions not in original setup —
  added during this session (AWSBudgetsActionsWithAWSResourceControlPolicy)
- Cost Explorer shows $0.00 because of deploy-on-demand strategy —
  not because costs are zero when running
- Evidence log and README written with assistance

---

## Day 20 — 2026-05-20

### What I did
- Built React production bundle: npm run build → dist/ (7 files, 425KB JS)
- Fixed vite.config.ts: import from vitest/config to fix TypeScript
  build errors with test configuration
- Created Terraform frontend module (S3 + CloudFront):
  - S3 bucket: daybook-frontend-production (private, no public access)
  - Origin Access Control: CloudFront reads S3 privately via OAC
  - CloudFront distribution with two origins:
    - S3: serves static files (HTML/JS/CSS/images)
    - ALB: forwards /api/* requests to Spring Boot backend
  - SPA routing: 403/404 return index.html for React Router
  - HTTPS enforced: redirect-to-https
  - PriceClass_100: Europe + North America edge locations
- Consolidated 10 IAM managed policies into one inline policy
  (daybook-full-access) to stay within AWS 10-policy-per-group limit
- Uploaded React build to S3: aws s3 sync dist/ s3://daybook-frontend-production
- Verified full application working at CloudFront URL:
  https://d2xjmpbr2m2035.cloudfront.net
  - Login page: paper-cut sky design, HTTPS ✅
  - Accounts page: 8 seeded accounts from RDS ✅
  - Transactions page: 5 transactions with correct entries ✅
  - Reports: Trial Balance £23,700 debits = £23,700 credits ✅
- Screenshots saved — complete frontend + backend on AWS
- terraform destroy: clean teardown, costs stopped

### What this demonstrates (framework mapping)

**IaC — Mid**
*"Can describe and implement IaC using tools like Terraform.
Can independently deploy a full environment."*
- Evidence: Full stack deployed — frontend (S3 + CloudFront) AND
  backend (ECS + RDS + ALB) — all provisioned via Terraform.
  39 AWS resources total. Real public URL serving real data.

**Architecture — Mid**
*"Can design simple architectures for basic projects."*
- Evidence: CloudFront acts as the single entry point for the
  entire application — routing static files to S3 and API calls
  to the ALB. This is the standard production pattern for
  decoupled frontend/backend applications.

**Cloud Security — Mid**
*"Implements basic security controls."*
- Evidence: S3 bucket has no public access. Files only accessible
  via CloudFront using Origin Access Control (OAC). HTTPS enforced
  via CloudFront redirect. Users can never access S3 directly.

### Honest gaps to flag
- CloudFront URL is auto-generated (d2xjmpbr2m2035.cloudfront.net)
  — a real production deployment would use a custom domain via
  Route 53 and AWS Certificate Manager
- No automated deployment pipeline for frontend — manual
  aws s3 sync after npm run build. A CD pipeline would automate this
- Evidence log and README written with assistance