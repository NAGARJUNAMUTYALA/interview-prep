# Microservices Architecture: Resilience, Outbox & Saga Patterns

**Target Audience:** Senior Java Backend Engineers (~5 Years Experience)  
**Topic:** Distributed transactions, fault tolerance, and eventual consistency  

---

## 1. The Distributed Transaction Problem: Why 2PC Fails in Cloud

In microservices, each service owns its private database. A business operation (e.g. Employee Onboarding ➔ Provision Email ➔ Assign Hardware) spans multiple services:
- Traditional **Two-Phase Commit (2PC)** requires distributed locking across all participating databases.
- Under high throughput or network partitions, 2PC holds database locks across network boundaries, causing thread pool exhaustion and catastrophic cascading failures.
- Microservices adopt **Eventual Consistency** via the **Saga Pattern**.

---

## 2. The Saga Pattern: Orchestration vs. Choreography

```text
Choreography (Event-Driven):
[Employee Service] ──(EmployeeCreated)──▶ [Notification Service]
                                       ──▶ [IT Hardware Service]
(No central controller; services listen and react to domain events)

Orchestration (Command-Driven):
[Employee Service] ──▶ [Saga Orchestrator]
                            │
                            ├──▶ Command: Create Email Account
                            ├──▶ Command: Assign Laptop
                            └──▶ If step fails ➔ Trigger Compensating Transactions
```

### Compensating Transactions:
If step 3 fails, the orchestrator issues semantic undo actions (e.g. cancel order, revoke email). A compensating transaction is an application-level rollback that restores the system to a consistent state.

---

## 3. The Transactional Outbox Pattern

### The Problem (Dual-Write Anti-Pattern):
A service needs to:
1. Save record in database (`employeeRepository.save(employee)`).
2. Publish message to Kafka (`kafkaTemplate.send("employee-events", event)`).

If the database commits but Kafka is down, the message is lost. If Kafka sends but the database transaction rolls back, external services act on phantom data!

### The Solution:
```text
Service Transaction Boundary:
┌───────────────────────────────────────────────┐
│ 1. INSERT INTO employees (...)                │  (Atomic local DB transaction)
│ 2. INSERT INTO outbox_table (event_payload)   │
└───────────────────────────────────────────────┘
                     │
                     ▼
  CDC (Debezium / Kafka Connect) or Polling Publisher
                     │
                     ▼
              Apache Kafka
```
Guarantees **At-Least-Once Delivery** without distributed transactions.

---

## 4. Resilience4j Fault Tolerance

1. **Circuit Breaker:** Prevents repeatedly invoking a failing downstream service.
   - States: `CLOSED` (Normal) ➔ `OPEN` (Failures exceed threshold, calls fail fast) ➔ `HALF-OPEN` (Test calls allowed).
2. **Retry with Exponential Backoff & Jitter:** Retries transient network glitches while randomizing delay to prevent the "thundering herd" problem.
3. **Rate Limiter:** Protects upstream APIs from being overwhelmed (e.g. max 100 requests per second).
4. **Bulkhead:** Isolates thread pools and semaphores so slow third-party integrations do not exhaust the main application container threads.
