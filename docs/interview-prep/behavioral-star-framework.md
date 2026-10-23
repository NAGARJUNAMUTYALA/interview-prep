# Senior Engineering Behavioral Interview Guide (STAR Framework)

**Candidate Experience Level:** ~5 Years Java Backend Development  
**Target Level:** Senior Software Engineer (L5 / SDE-3 / Tech Lead)  
**Methodology:** Situation ➔ Task ➔ Action ➔ Result (STAR)  

---

## 1. Scenario: Leading Resolution of a Critical Production Outage

### Question: *"Tell me about a time when a critical production system failed under your watch. How did you respond?"*

- **Situation:** Our e-commerce checkout API experienced a sudden latency spike from 45ms to over 8 seconds during a high-traffic promotional event, causing customer timeouts and lost revenue.
- **Task:** As the on-call senior backend engineer, my objective was to mitigate customer impact immediately, diagnose the root cause without guessing, and restore normal latency within our 15-minute SLA.
- **Action:**
  1. *Mitigation:* I checked the Grafana dashboard and HikariCP connection pool metrics, noticing 100% active connection saturation. I temporarily enabled our Redis read-through cache fallback and reduced client retry frequency via the API Gateway to relieve database pressure.
  2. *Triage:* Using `jcmd` and PostgreSQL `pg_stat_activity`, I identified multiple unindexed queries executing sequential table scans (`Seq Scan`) across millions of order history records.
  3. *Resolution:* I created a concurrent index (`CREATE INDEX CONCURRENTLY`) on the affected foreign key column, preventing table lockups, and recycled hung connections.
- **Result:** Service latency dropped back to 35ms within 11 minutes. Post-incident, I introduced automated query linting in our CI/CD pipeline to flag missing indexes before pull requests are merged, preventing recurrence.

---

## 2. Scenario: Resolving an Architectural Disagreement

### Question: *"Describe a time you strongly disagreed with a colleague or tech lead on an architectural design. How did you resolve it?"*

- **Situation:** When designing our new employee onboarding workflow, a teammate proposed an immediate microservice split with distributed two-phase commit (2PC) across three separate services.
- **Task:** I believed that premature decomposition and 2PC would introduce unneeded network latency, complex failure modes, and tight coupling across young service boundaries.
- **Action:**
  1. Rather than debating opinions, I scheduled a design review meeting and prepared an Architectural Decision Record (ADR) comparing the trade-offs of a Modular Monolith versus Microservices.
  2. I highlighted the operational overhead of distributed transactions and demonstrated how a **Modular Monolith** with clear package-level boundaries and in-process events would allow us to validate business requirements faster.
  3. I included a clear migration plan: once throughput exceeded single-database thresholds, we could cleanly extract the bounded context into microservices using the Transactional Outbox pattern.
- **Result:** The team aligned on the Modular Monolith strategy. We shipped the platform three weeks ahead of schedule with zero distributed transaction bugs and successfully decomposed into dedicated microservices six months later when traffic justified it.

---

## 3. Scenario: Managing Technical Debt Under Tight Deadlines

### Question: *"How do you balance delivering business features quickly against managing technical debt?"*

- **Situation:** Our business stakeholders requested a critical internationalization feature with a 3-week deadline, but our core service layer contained legacy tightly coupled JDBC code without proper transaction boundaries or automated test coverage.
- **Task:** Deliver the feature on time without compounding technical debt or introducing regression bugs into production.
- **Action:**
  1. I broke the project into two distinct milestones: first, wrapping existing critical paths with `@WebMvcTest` and integration regression tests to create a safety net.
  2. For the new feature, I applied clean architecture: isolating business rules behind service interfaces and utilizing immutable DTO records with declarative validation.
  3. I transparently negotiated a 20% technical debt allocation in the subsequent sprint with product management, showing them how automated test coverage would accelerate future feature delivery.
- **Result:** The internationalization feature shipped on schedule with zero production defect escapes. In the following sprint, we refactored the legacy JDBC code into Spring Data JPA, reducing overall codebase size by 35% and cutting subsequent feature lead time in half.
