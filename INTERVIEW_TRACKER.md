# Master Interview Preparation Tracker

**Candidate Experience:** ~5 Years Java Backend Development  
**Target Level:** Mid-to-Senior Java Engineer (L4 / L5 / SDE-2 & SDE-3)  
**Status Scale:**
- `NOT STARTED`
- `LEARNING`
- `STUDIED BUT CANNOT RECALL`
- `UNDERSTANDS BUT CANNOT EXPLAIN`
- `CAN EXPLAIN BUT CANNOT APPLY`
- `CAN APPLY WITH HINTS`
- `CAN EXPLAIN AND APPLY INDEPENDENTLY`
- `INTERVIEW-READY` (Criteria: Clear explanation, internals knowledge, live coding confidence, edge cases, follow-up answers, real-world context)

---

## 1. Core Java, OOP & Memory Model

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **Pass-by-Value & Memory** | Primitives vs references, reference reassignment, stack vs heap | `LEARNING` | 2026-10-23 | Mastered in scratch, moving to project tests |
| **Object Contracts (equals/hashCode)** | Contract rules, hash bucket distribution, collision handling | `NOT STARTED` | - | Must explain `HashMap` bucket lookup |
| **String Pool & Immutability** | String Constant Pool, intern(), StringBuilder growth | `NOT STARTED` | - | Memory overhead & performance |
| **Immutability & Defensive Copying** | Final fields, unmodifiable collections, Java 21 records | `NOT STARTED` | - | Thread safety without synchronization |
| **Generics & Type Erasure** | Covariance `? extends T`, contravariance `? super T`, PECS | `NOT STARTED` | - | Production API design |
| **Exception Hierarchy** | Checked vs unchecked, try-with-resources, custom exceptions | `NOT STARTED` | - | Effective Java Item 70 |

---

## 2. Collections Framework & Stream API

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **HashMap Internals** | Bitwise index `(n-1) & hash`, load factor 0.75, treeification threshold 8 | `NOT STARTED` | - | High-frequency senior interview question |
| **ConcurrentHashMap Internals** | CAS + synchronized node heads, table resizing, thread safety | `NOT STARTED` | - | Compare with `Collections.synchronizedMap` |
| **ArrayList vs LinkedList** | CPU cache line locality, memory fragmentation, iterator performance | `NOT STARTED` | - | Big-O vs mechanical sympathy |
| **Comparable vs Comparator** | Natural ordering vs custom strategies, lambda comparator chaining | `NOT STARTED` | - | Clean sorting algorithms |
| **Stream API Operations** | Filtering, groupingBy, partitioningBy, flatMap, reducing | `NOT STARTED` | - | 50 practical problem target |

---

## 3. Concurrency, Multithreading & JVM

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **Thread Lifecycle & States** | NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED | `NOT STARTED` | - | Thread dump state analysis |
| **Volatile & Happens-Before** | Visibility guarantee, instruction reordering, CPU cache coherency | `NOT STARTED` | - | JMM specifications |
| **ExecutorService & ThreadPools** | Pool sizing formulas, thread starvation, caller runs rejection | `NOT STARTED` | - | Production thread tuning |
| **CompletableFuture** | Async pipelines, exception handling, custom thread pool injection | `NOT STARTED` | - | Microservice fan-out patterns |
| **Java 21 Virtual Threads** | Continuation suspension, carrier thread unmounting, pinning traps | `NOT STARTED` | - | Modern Java scalability |
| **JVM Memory & GC Architecture** | Eden, Survivor, Tenured, Metaspace, G1GC/ZGC pause characteristics | `NOT STARTED` | - | Heap dump & memory leak diagnosis |

---

## 4. Spring Boot, JPA & Database Design

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **Spring IoC & Bean Lifecycle** | BeanPostProcessor, BeanFactory vs ApplicationContext, Scopes | `NOT STARTED` | - | Core framework architecture |
| **Spring AOP & Proxying** | JDK Dynamic Proxy vs CGLIB, @Transactional self-invocation trap | `NOT STARTED` | - | Why private @Transactional fails |
| **JPA Entity Lifecycle** | Transient, Persistent, Detached, Removed, First-level cache | `NOT STARTED` | - | Dirty checking mechanics |
| **N+1 Query Problem** | Hibernate query explosion, JOIN FETCH, EntityGraphs | `NOT STARTED` | - | Production DB optimization |
| **Transaction Isolation & Locking**| Read Committed, Repeatable Read, Optimistic vs Pessimistic locks | `NOT STARTED` | - | Preventing lost updates |
| **SQL Practice (75 Questions)** | Window functions (DENSE_RANK), CTEs, self-joins, aggregates | `NOT STARTED` | - | Live coding interviews |

---

## 5. Microservices, DevOps & System Design

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **REST API Best Practices** | Idempotency, HTTP verbs, RFC 7807 error formats, versioning | `LEARNING` | 2026-10-23 | Building in Phase 1 |
| **Resilience & Fault Tolerance** | Circuit Breaker, Retry, Rate Limiter, Bulkhead (Resilience4j) | `NOT STARTED` | - | Preventing cascading failures |
| **Kafka Event-Driven Architecture**| Partitions, consumer groups, exactly-once vs at-least-once, Outbox | `NOT STARTED` | - | Distributed transaction handling |
| **Docker & Kubernetes Deployment** | Multi-stage Docker, K8s Deployments, probes, zero-downtime rolls | `LEARNING` | 2026-10-23 | Dockerfile & compose verified |
| **System Design: 7 Core Systems** | Scalability, caching, database sharding, consistency models | `NOT STARTED` | - | High-level architecture rounds |
