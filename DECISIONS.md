# Architecture Decision Records (ADR)

This document tracks significant architectural and technical decisions made in the `interview-prep` platform, documenting context, choices, trade-offs, and rationale.

---

## ADR 001: Selection of Java 21 LTS and Spring Boot 3.3.x

### Status
Accepted

### Context
The candidate has ~5 years of experience preparing for senior backend roles. Modern Java interviews require proficiency in both traditional enterprise patterns and current language features (Records, Pattern Matching, Virtual Threads, Sequenced Collections).

### Decision
Adopt **Java 21 LTS** with **Spring Boot 3.3.4** and **Maven 3.9.9**.

### Consequences & Rationale
- **Pros:**
  - Modern Java features eliminate boilerplate (e.g. Records for DTOs).
  - Spring Boot 3.3 introduces native support for virtual threads, improved Docker/OCI image tooling, and Jakarta EE 10.
  - Matches modern production standards expected by tech companies in 2026.
- **Cons:** Requires Java 21 runtime baseline (already verified on host and in Docker container).

---

## ADR 002: Dual-Profile Database Strategy (H2 for Tests, PostgreSQL for Production/Docker)

### Status
Accepted

### Context
Testing must be completely reliable, self-contained, and run quickly (`mvn clean test`) without requiring external database instances running. At the same time, interview prep demands realistic SQL dialect experience (PostgreSQL 16) with window functions, JSONB, and transaction locks.

### Decision
Implement a dual-profile configuration:
1. **`default` / `test` Profile:** H2 in-memory database with Spring Boot auto-configuration for fast, reproducible tests.
2. **`docker` / `prod` Profile:** PostgreSQL 16 containerized via Docker Compose for production runs and complex SQL practice.

### Consequences & Rationale
- Zero-friction developer onboarding (`mvn test` runs anywhere).
- Production fidelity when running containers via `docker compose up`.

---

## ADR 003: Layered Package Architecture with Service Interface / Implementation Separation

### Status
Accepted

### Context
Enterprise Spring Boot applications require clean separation of concerns, testability, and adherence to SOLID principles (especially the Dependency Inversion Principle).

### Decision
Organize code into clear domain layers:
- `controller/`: HTTP handling, serialization, request routing.
- `service/`: Business contracts (Interfaces).
- `service/impl/`: Business logic, transactional boundaries (`@Transactional`), domain validations, and in-depth Javadoc.
- `repository/`: Spring Data JPA persistence interfaces.
- `entity/`: JPA entities with audit metadata.
- `dto/`: Java 21 Records for immutable request/response payloads.
- `exception/`: Domain-specific exceptions and `@RestControllerAdvice`.

### Consequences & Rationale
- Enables seamless unit testing of service logic with Mockito without starting Spring context.
- Provides a clean framework for explaining Spring AOP proxying and transaction boundaries during interviews.

---

## ADR 004: Preservation of Existing `prepStart` Microservice Endpoint

### Status
Accepted

### Context
The repository was initialized with a working `/api/v1/prepStart` endpoint and tests. Existing features must not be broken or discarded.

### Decision
Retain `PrepController`, `PrepService`, and `PrepStartResponse` in the codebase under the `prep` module/package while introducing the `Employee` platform domain alongside it.

### Consequences & Rationale
- Preserves all earlier deliverables and keeps existing tests green.
- Provides a multi-domain microservice architecture reflecting real enterprise codebases.
