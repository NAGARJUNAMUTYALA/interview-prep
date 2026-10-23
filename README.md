# Interview Prep: Production-Grade Java Platform & Interview Mastery

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build Tool](https://img.shields.io/badge/Maven-3.9.9-blue.svg)](https://maven.apache.org/)
[![Tests](https://img.shields.io/badge/Tests-92%2F92%20Passing-brightgreen.svg)](https://github.com/NAGARJUNAMUTYALA/interview-prep)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)

A practical, production-style Java backend platform and interview preparation curriculum tailored for **mid-to-senior Java developers (~5 years experience)**.

This repository combines a working enterprise **Employee Management Platform** with comprehensive, runnable practice modules covering **Core Java, Collections internals, 50 Stream API exercises, Concurrency, Virtual Threads, JVM internals, Spring Boot, JPA/Hibernate, 75 SQL queries, Distributed Microservices, and System Design**.

---

## 📌 Master Documentation & Study Trackers

- 📋 **[Repository Audit](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/REPOSITORY_AUDIT.md)**: Baseline inventory, tooling verification, and gap analysis.
- 🗺️ **[12-Week Roadmap](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/ROADMAP.md)**: Structured step-by-step learning and implementation phases.
- 📈 **[Progress Dashboard](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/PROGRESS.md)**: Current phase status, 92 verified tests evidence, and completed milestones.
- 🎯 **[Interview Topic Tracker](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/INTERVIEW_TRACKER.md)**: Topic-by-topic confidence tracking for technical and coding rounds.
- 🏛️ **[Architecture Decisions (ADR)](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/DECISIONS.md)**: Key design choices, trade-offs, and rationale.
- 🚀 **[End-to-End Request Trace & Runbooks](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/end-to-end-execution.md)**: Full 6-stage lifecycle trace and production troubleshooting.
- 🗄️ **[75 SQL Interview Queries & Solutions](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/sql/75-sql-interview-queries.md)**: Window functions, DENSE_RANK, CTEs, running totals, and performance indexing.
- 📦 **[HashMap Internals Deep Dive](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/collections/hashmap-internals.md)**: Bitwise index calculation, bucket collision, treeification, and ConcurrentHashMap.
- ⚡ **[Concurrency & Virtual Threads Guide](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/concurrency/concurrency-guide.md)**: JMM, Happens-Before, ExecutorService sizing formula, and Java 21 Loom mechanics.
- ☕ **[JVM Internals & GC Troubleshooting](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/jvm/jvm-tuning-troubleshooting.md)**: Heap regions, G1GC/ZGC, diagnostic commands (jcmd/jstack), and OOM diagnosis.
- 🌐 **[Microservices, Outbox & Saga Patterns](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/microservices/resilience-and-saga.md)**: Distributed transactions, Eventual Consistency, Outbox Pattern, and Resilience4j.
- 📐 **[System Design: 100K RPS Platform](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/docs/system-design/employee-platform-design.md)**: High-scale architecture, Redis caching, DB sharding, and Token Bucket rate limiting.

---

## 🏗️ Architecture & Directory Layout

```
interview-prep/
├── pom.xml                                              # Java 21, Spring Boot 3.3.4, JPA, OpenAPI
├── README.md                                            # Master project guide
├── REPOSITORY_AUDIT.md                                  # Baseline audit
├── ROADMAP.md                                           # 12-week curriculum
├── PROGRESS.md                                          # 92/92 test metrics
├── INTERVIEW_TRACKER.md                                 # Confidence tracker
├── DECISIONS.md                                         # Architectural Decision Records (ADRs)
├── Dockerfile                                           # Multi-stage container build
├── docker-compose.yml                                   # Local container orchestration
├── .github/workflows/ci.yml                             # Automated GitHub Actions CI workflow
├── docs/                                                # Deep dive study guides & runbooks
├── infrastructure/                                      # K8s manifests, Docker & Prometheus configs
├── scripts/                                             # ./scripts/run-local.sh, ./scripts/test-all.sh
├── src/main/java/com/example/interviewprep/
│   ├── InterviewPrepApplication.java                    # Spring Boot entry point + VIA_DTO
│   ├── common/exception/                                # GlobalExceptionHandler (@RestControllerAdvice)
│   ├── employee/                                        # Employee Management Domain
│   │   ├── controller/EmployeeController.java           # REST CRUD + OpenAPI docs
│   │   ├── service/impl/EmployeeServiceImpl.java        # Business logic + Detailed Javadoc
│   │   ├── repository/EmployeeRepository.java           # Spring Data JPA + Specifications
│   │   ├── dto/EmployeeRequest.java                     # Record DTO with Bean Validation
│   │   └── entity/Employee.java                         # JPA Entity with @PrePersist/@PreUpdate
│   └── service/PrepService.java                         # Preserved prepStart API service
└── src/test/java/com/example/interviewprep/
    ├── employee/                                        # Controller, Service, Repository tests
    ├── practice/                                        # 66 Runnable Practice Tests
    │   ├── corejava/CoreJavaFundamentalsTest.java       # Pass-by-value, String Pool, equals/hashCode
    │   ├── collections/CollectionsInternalsTest.java    # HashMap bitwise index & collisions
    │   ├── streams/StreamApiPracticeTest.java           # 50 Practical Stream API exercises
    │   ├── concurrency/ConcurrencyPracticeTest.java     # CompletableFuture, Virtual Threads
    │   └── dsa/DsaPracticeTest.java                     # Two Pointers, Sliding Window, Monotonic Stack
    └── InterviewPrepApplicationTests.java               # Context load test
```

---

## 🚀 Quick Start & Verification

### Run the Complete Test Suite (92 Tests):
```bash
./scripts/test-all.sh
# or: mvn clean test
```

### Start the Microservice Locally (Port 8081):
```bash
./scripts/run-local.sh
# or: mvn spring-boot:run
```

### Interactive OpenAPI Swagger Documentation:
Once started, explore and test the interactive API in your browser:
🔗 **[http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)**

### Run with Docker Compose:
```bash
docker compose up -d --build
```

---

## 👨‍💻 Author & Maintainer
- **Engineer:** Mutyala Chandra Sekhara Nagarjuna
- **Email:** nagarjunamutyala460@gmail.com
- **GitHub:** [@NAGARJUNAMUTYALA](https://github.com/NAGARJUNAMUTYALA)
