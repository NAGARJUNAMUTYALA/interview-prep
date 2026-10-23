# System Design: High-Scale Employee Management & Enterprise Portal (100K RPS)

**Target Audience:** Senior Java & Distributed Systems Engineers  
**Non-Functional Requirements:** High Availability (99.99%), Low Latency (p99 < 50ms), Eventual Consistency  

---

## 1. High-Level Architecture Diagram

```text
Clients (Web / Mobile / Third-Party Integrations)
                     │
                     ▼
             [Cloudflare CDN / WAF]
                     │
                     ▼
            [API Gateway / Envoy]
  (Authentication, Rate Limiting via Token Bucket, TLS Termination)
                     │
                     ├──────────────────────────┐
                     ▼                          ▼
          [Employee Service Pods]    [Notification Service Pods]
          (Horizontal Pod Autoscaler)
                     │                          │
        ┌────────────┴────────────┐             │
        ▼                         ▼             │
[Redis Cluster (Cache)]   [PostgreSQL Primary]  │
(Read-through / Cache-Aside)      │             │
                                  ▼ (Replication)
                         [PostgreSQL Read Replicas]
                                  │
                                  ▼
                     [CDC / Debezium Connector]
                                  │
                                  ▼
                           [Apache Kafka] ──────┘
```

---

## 2. Deep Dive: Key Architectural Components

### A. Scalability & Read-Write Split
- **Read Path (95% of traffic):**
  - Clients query `GET /api/v1/employees`.
  - Service checks **Redis Cluster** (Cache-Aside pattern). If cache hit, return in < 5ms.
  - If cache miss, read from **PostgreSQL Read Replicas**; populate Redis with TTL (e.g., 10 minutes) and return.
- **Write Path (5% of traffic):**
  - Clients send `POST` / `PUT` / `DELETE`.
  - Handled by **PostgreSQL Primary**.
  - Evicts corresponding cache keys in Redis to prevent stale reads.

### B. Distributed Rate Limiter
Implemented at the API Gateway using the **Token Bucket Algorithm** backed by Redis:
```lua
-- Redis Lua script ensuring atomic token consumption
local key = KEYS[1]
local limit = tonumber(ARGV[1])
local current = tonumber(redis.call('get', key) or "0")
if current + 1 > limit then
    return 0 -- Rate limited (HTTP 429)
else
    redis.call("INCRBY", key, 1)
    if current == 0 then
        redis.call("EXPIRE", key, 1) -- 1-second window
    end
    return 1 -- Allowed
end
```

### C. Database Sharding Strategy
When employee data exceeds 10TB:
- **Shard Key:** `organization_id` or `tenant_id` for multi-tenant SaaS.
- **Consistent Hashing:** Distributes tenant records evenly across physical database shards while allowing shard addition without massive data reshuffling.
- **Cross-Shard Queries:** Avoided by enforcing tenant-scoped API routes (`/api/v1/organizations/{orgId}/employees`).
