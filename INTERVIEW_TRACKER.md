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
📖 *Guide:* [`docs/core-java/core-java-deep-dive.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/core-java/core-java-deep-dive.md) | 🧪 *Tests:* [`CoreJavaFundamentalsTest.java`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/src/test/java/com/example/interviewprep/practice/corejava/CoreJavaFundamentalsTest.java)

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **Pass-by-Value & Memory** | Primitives vs references, reference reassignment, stack vs heap | `INTERVIEW-READY` | 2026-10-23 | Verified in CoreJavaFundamentalsTest |
| **Object Contracts (equals/hashCode)** | Contract rules, hash bucket distribution, collision handling | `INTERVIEW-READY` | 2026-10-23 | Verified in CoreJavaFundamentalsTest |
| **String Pool & Immutability** | String Constant Pool, intern(), StringBuilder growth | `INTERVIEW-READY` | 2026-10-23 | Verified in CoreJavaFundamentalsTest |
| **Immutability & Defensive Copying** | Final fields, unmodifiable collections, Java 21 records | `INTERVIEW-READY` | 2026-10-23 | Demonstrated across DTO records |
| **Generics & Type Erasure** | Covariance `? extends T`, contravariance `? super T`, PECS | `INTERVIEW-READY` | 2026-10-23 | Verified in CoreJavaFundamentalsTest |
| **Exception Hierarchy** | Checked vs unchecked, try-with-resources, custom exceptions | `INTERVIEW-READY` | 2026-10-23 | Verified in GlobalExceptionHandler |

---

## 2. Collections Framework & Stream API
📖 *Guide:* [`docs/collections/hashmap-internals.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/collections/hashmap-internals.md) | 🧪 *Tests:* [`StreamApiPracticeTest.java`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/src/test/java/com/example/interviewprep/practice/streams/StreamApiPracticeTest.java) & [`CollectionsInternalsTest.java`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/src/test/java/com/example/interviewprep/practice/collections/CollectionsInternalsTest.java)

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **HashMap Internals** | Bitwise index `(n-1) & hash`, load factor 0.75, treeification threshold 8 | `INTERVIEW-READY` | 2026-10-23 | Verified in CollectionsInternalsTest |
| **ConcurrentHashMap Internals** | CAS + synchronized node heads, table resizing, thread safety | `INTERVIEW-READY` | 2026-10-23 | Verified in CollectionsInternalsTest |
| **ArrayList vs LinkedList** | CPU cache line locality, memory fragmentation, iterator performance | `INTERVIEW-READY` | 2026-10-23 | Documented in Hashmap internals |
| **Stream API Operations (50 Problems)** | Filtering, groupingBy, partitioningBy, flatMap, reducing, stats | `INTERVIEW-READY` | 2026-10-23 | 50 tests passing in StreamApiPracticeTest |

---

## 3. Concurrency, Multithreading & JVM
📖 *Guides:* [`docs/concurrency/concurrency-guide.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/concurrency/concurrency-guide.md) & [`docs/jvm/jvm-tuning-troubleshooting.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/jvm/jvm-tuning-troubleshooting.md) | 🧪 *Tests:* [`ConcurrencyPracticeTest.java`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/src/test/java/com/example/interviewprep/practice/concurrency/ConcurrencyPracticeTest.java)

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **Thread Lifecycle & States** | NEW, RUNNABLE, BLOCKED, WAITING, TIMED_WAITING, TERMINATED | `INTERVIEW-READY` | 2026-10-23 | Documented in Concurrency Guide |
| **Volatile & Happens-Before** | Visibility guarantee, instruction reordering, CPU cache coherency | `INTERVIEW-READY` | 2026-10-23 | Memory barrier mechanics covered |
| **ExecutorService & ThreadPools** | Goetz sizing formula, thread starvation, caller runs rejection | `INTERVIEW-READY` | 2026-10-23 | Production pool tuning documented |
| **CompletableFuture** | Async pipelines, exception handling, custom thread pool injection | `INTERVIEW-READY` | 2026-10-23 | Verified in ConcurrencyPracticeTest |
| **Java 21 Virtual Threads** | Continuation suspension, carrier thread unmounting, pinning traps | `INTERVIEW-READY` | 2026-10-23 | Verified with 1000 tasks test |
| **JVM Memory & GC Architecture** | Eden, Survivor, Tenured, Metaspace, G1GC/ZGC pause characteristics | `INTERVIEW-READY` | 2026-10-23 | Detailed diagnostic commands |

---

## 4. Spring Boot, JPA & Database Design
📖 *Guides:* [`docs/spring-boot/spring-boot-internals-guide.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/spring-boot/spring-boot-internals-guide.md), [`docs/hibernate-jpa/jpa-n-plus-1-and-caching.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/hibernate-jpa/jpa-n-plus-1-and-caching.md) & [`docs/sql/75-sql-interview-queries.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/sql/75-sql-interview-queries.md)

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **Spring IoC & Bean Lifecycle** | BeanPostProcessor, BeanFactory vs ApplicationContext, Scopes | `INTERVIEW-READY` | 2026-10-23 | Full lifecycle diagram documented |
| **Spring AOP & Proxying** | JDK Dynamic Proxy vs CGLIB, @Transactional self-invocation trap | `INTERVIEW-READY` | 2026-10-23 | Self-invocation pitfall explained |
| **JPA Entity Lifecycle** | Transient, Persistent, Detached, Removed, First-level cache | `INTERVIEW-READY` | 2026-10-23 | Dirty checking snapshot mechanics |
| **N+1 Query Problem** | Hibernate query explosion, JOIN FETCH, EntityGraphs | `INTERVIEW-READY` | 2026-10-23 | Detailed solution comparisons |
| **Transaction Isolation & Locking**| Read Committed, Repeatable Read, Optimistic vs Pessimistic locks | `INTERVIEW-READY` | 2026-10-23 | SKIP LOCKED & FOR UPDATE queries |
| **SQL Practice (75 Questions)** | Window functions (DENSE_RANK), CTEs, self-joins, aggregates | `INTERVIEW-READY` | 2026-10-23 | 75 production queries solved |

---

## 5. Microservices, DevOps & System Design
📖 *Guides:* [`docs/microservices/resilience-and-saga.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/microservices/resilience-and-saga.md), [`docs/kafka/kafka-event-driven-guide.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/kafka/kafka-event-driven-guide.md), [`docs/system-design/employee-platform-design.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/system-design/employee-platform-design.md), [`docs/cloud/aws-cloud-guide.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/cloud/aws-cloud-guide.md) & [`docs/troubleshooting/linux-networking-debugging.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/troubleshooting/linux-networking-debugging.md)

| Topic | Key Concepts | Status | Last Reviewed | Confidence / Notes |
|---|---|---|---|---|
| **REST API Best Practices** | Idempotency, HTTP verbs, RFC 7807 error formats, versioning | `INTERVIEW-READY` | 2026-10-23 | Implemented in EmployeeController |
| **Resilience & Fault Tolerance** | Circuit Breaker, Retry, Rate Limiter, Bulkhead (Resilience4j) | `INTERVIEW-READY` | 2026-10-23 | Covered in Microservices guide |
| **Kafka Event-Driven Architecture**| Partitions, consumer groups, exactly-once vs at-least-once, Outbox | `INTERVIEW-READY` | 2026-10-23 | Detailed in Kafka Guide |
| **System Design: 100K RPS Platform**| Scalability, caching, database sharding, consistency models | `INTERVIEW-READY` | 2026-10-23 | Complete architectural diagram |
| **AWS Cloud Architecture** | VPC, private subnets, ECS vs EKS, RDS Multi-AZ vs Replicas | `INTERVIEW-READY` | 2026-10-23 | Documented in AWS Cloud Guide |
| **Linux & Incident Debugging** | High-CPU thread dump mapping (`top -H ➔ hex ➔ jcmd`) | `INTERVIEW-READY` | 2026-10-23 | Step-by-step incident runbook |
| **Behavioral STAR Framework** | High-impact stories on outages, technical debt, and mentoring | `INTERVIEW-READY` | 2026-10-23 | 3 complete senior interview stories |

---

## 6. DSA Curriculum Tracker (150 Problems)
📖 *Curriculum:* [`docs/dsa/dsa-master-curriculum.md`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/dsa/dsa-master-curriculum.md) | 🧪 *Tests:* [`DsaPracticeTest.java`](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/src/test/java/com/example/interviewprep/practice/dsa/DsaPracticeTest.java)

- **Pattern 1: Arrays, Strings & Hashing (15 Problems):** 1/15 Completed
- **Pattern 2: Two Pointers (10 Problems):** 1/10 Completed
- **Pattern 3: Sliding Window (10 Problems):** 1/10 Completed
- **Pattern 4: Monotonic Stack (10 Problems):** 1/10 Completed
- **Pattern 5: Binary Search (10 Problems):** 1/10 Completed
- **Pattern 6: Linked Lists (10 Problems):** 0/10 Completed
- **Pattern 7: Trees & BST (15 Problems):** 1/15 Completed
- **Pattern 8: Heaps & Priority Queues (10 Problems):** 0/10 Completed
- **Pattern 9: Tries (5 Problems):** 0/5 Completed
- **Pattern 10: Graphs (BFS, DFS, Topo) (15 Problems):** 0/15 Completed
- **Pattern 11: Dynamic Programming (25 Problems):** 1/25 Completed
- **Pattern 12: Intervals & Greedy (15 Problems):** 0/15 Completed
- *Total Curriculum:* 150 Curated Problems tracked.
