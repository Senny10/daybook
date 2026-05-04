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