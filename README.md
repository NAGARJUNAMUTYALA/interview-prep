# Interview Prep: Production-Grade Java Platform & Interview Mastery

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Build Tool](https://img.shields.io/badge/Maven-3.9.9-blue.svg)](https://maven.apache.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED.svg)](https://www.docker.com/)

A practical, production-style Java backend platform and interview preparation curriculum tailored for **mid-to-senior Java developers (~5 years experience)**.

This repository combines a working enterprise **Employee Management Platform** with comprehensive, runnable practice modules covering **Core Java, Collections internals, Concurrency, JVM internals, Spring Boot, JPA/Hibernate, SQL, Distributed Microservices, and System Design**.

---

## 📌 Master Documentation & Trackers

- 📋 **[Repository Audit](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/REPOSITORY_AUDIT.md)**: Baseline inventory, tooling verification, and gap analysis.
- 🗺️ **[12-Week Roadmap](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/ROADMAP.md)**: Structured step-by-step learning and implementation phases.
- 📈 **[Progress Dashboard](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/PROGRESS.md)**: Current phase status, verification evidence, and completed milestones.
- 🎯 **[Interview Topic Tracker](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/INTERVIEW_TRACKER.md)**: Topic-by-topic confidence tracking for technical and coding rounds.
- 🏛️ **[Architecture Decisions (ADR)](file:///Users/nagarjuna_m/.gemini/antigravity-ide/scratch/interview-prep/DECISIONS.md)**: Key design choices, trade-offs, and rationale.

---

## 🏗️ Architecture Overview

The application follows clean architecture and SOLID design principles:

```
src/main/java/com/example/interviewprep/
├── InterviewPrepApplication.java
├── common/
│   └── exception/                  # Global exception handling (@RestControllerAdvice, RFC 7807)
├── employee/
│   ├── controller/                 # REST controllers (Employee CRUD, pagination, filtering)
│   ├── dto/                        # Immutable Java 21 Record DTOs with Jakarta Bean Validation
│   ├── entity/                     # JPA entities with audit timestamps (@PrePersist, @PreUpdate)
│   ├── repository/                 # Spring Data JPA repositories with derived & JPQL queries
│   └── service/                    # Business interface & ServiceImpl with detailed Javadoc
├── prep/                           # Interview session prep APIs & module generator
└── practice/                       # Runnable Core Java, Collections, Concurrency & DSA modules
```

---

## 🚀 Quick Start

### Prerequisites
- **Java 21+** (`java -version`)
- **Maven 3.9+** (`mvn -version`)
- **Docker & Docker Compose** (Optional for local container run)

### Build & Run Tests
```bash
mvn clean test
```

### Run Locally (Port 8081)
```bash
mvn spring-boot:run
```

### Run with Docker Compose
```bash
docker compose up -d --build
```

---

## 📡 Core API Endpoints

### 1. Interview Prep Session Starter
- **Endpoint:** `GET /api/v1/prepStart` *(or `GET /prepStart`)*
- **Query Parameter:** `topic` *(optional, string)*
- **Sample Request:**
  ```bash
  curl http://localhost:8081/api/v1/prepStart
  ```

### 2. Employee Management Platform (Phase 1)
- `POST /api/v1/employees`: Create new employee with validation.
- `GET /api/v1/employees/{id}`: Retrieve employee by ID.
- `GET /api/v1/employees`: List employees with pagination, sorting, and department filtering.
- `PUT /api/v1/employees/{id}`: Update employee details.
- `DELETE /api/v1/employees/{id}`: Delete employee (204 No Content).

---

## 👨‍💻 Author & Maintainer
- **Engineer:** Mutyala Chandra Sekhara Nagarjuna
- **Email:** nagarjunamutyala460@gmail.com
- **GitHub:** [@NAGARJUNAMUTYALA](https://github.com/NAGARJUNAMUTYALA)
