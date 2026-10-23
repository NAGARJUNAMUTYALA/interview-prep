# Apache Kafka & Event-Driven Architecture Guide

**Target Audience:** Senior Java Backend Engineers (~5 Years Experience)  
**Topic:** Topics, Partitions, Consumer Groups, Idempotent Producers, Rebalancing, and Dead-Letter Topics (DLT)  

---

## 1. Apache Kafka Architecture & Storage Internals

```text
               Topic: "employee-events" (Replication Factor: 3)
┌─────────────────────────────────────────────────────────────────┐
│ Partition 0: [msg0 | msg1 | msg2 | msg3 | msg4] (Leader: Broker 1)│
│ Partition 1: [msg0 | msg1 | msg2]              (Leader: Broker 2)│
│ Partition 2: [msg0 | msg1 | msg2 | msg3]        (Leader: Broker 3)│
└─────────────────────────────────────────────────────────────────┘
          ▲                                           │
          │                                           ▼
   [Kafka Producer]                         [Consumer Group "analytics"]
   Message Key: "emp_101"                   - Consumer A (reads P0, P1)
   hash(key) % 3 ➔ Partition 2              - Consumer B (reads P2)
```

### Core Architecture Rules:
1. **Partitioning & Ordering Guarantee:** Kafka guarantees strict total ordering **only within a single partition**, never across multiple partitions. Messages sharing the same key (`key = "employee_id"`) are always routed to the same partition using Murmur2 hashing (`hash(key) % num_partitions`), guaranteeing per-employee sequential ordering.
2. **Consumer Groups & Parallelism:** The number of active consumers in a consumer group cannot exceed the number of partitions. Excess consumers remain idle. To scale consumption, increase the number of partitions.
3. **Log Storage & Commit Log:** Kafka writes messages sequentially to append-only disk segments (`.log` files) using zero-copy OS page cache transfers (`sendfile` system call), bypassing JVM heap memory.

---

## 2. Preventing Data Loss & Duplication

### A. Idempotent Producer (`enable.idempotence = true`)
- Prevents duplicate writes when network retries occur.
- Broker assigns a unique Producer ID (`PID`) and monotonically increasing Sequence Number to each batch. If a broker receives a batch with an already committed sequence number, it returns an ACK without duplicating the record.

### B. Producer Acks:
- `acks = 0`: Fire-and-forget (Highest throughput, risk of data loss).
- `acks = 1`: Leader broker writes to local log before ACKing (Loss if leader crashes before replica sync).
- `acks = all` (`-1`) + `min.insync.replicas = 2`: Strong durability. Leader and at least one in-sync replica must persist before ACKing.

### C. Consumer Error Handling & Dead Letter Topic (DLT)
When a consumer fails to process a poison pill message (deserialization error, database failure):
1. Configure `DefaultErrorHandler` with a fixed backoff (e.g., 3 retries at 1-second intervals).
2. If retries are exhausted, forward the failing record to a dedicated Dead-Letter Topic: `employee-events.DLT`.
3. The consumer commits the offset and continues processing, preventing a single malformed message from stalling the partition consumer loop.
