# Interview Prep Microservice

A lightweight Spring Boot microservice designed for interview preparation workflows.

## Architecture & Design
- **Java 21 & Spring Boot 3.3.4**
- **Layered Architecture**:
  - `PrepController`: REST layer exposing the `prepStart` endpoint.
  - `PrepService`: Interface defining the interview prep contracts.
  - `PrepServiceImpl`: Implementation handling business logic & module curation.
  - `PrepStartResponse`: Immutable Java Record DTO.
- **Single Dedicated API Endpoint**: `prepStart` initializes and returns interview prep session modules and topics.
- **Unit & Integration Tests**: Comprehensive tests for controller layer (MockMvc) and service layer.

---

## API Specification

### 1. Start Interview Preparation Session
- **Endpoint**: `GET /api/v1/prepStart` *(or `GET /prepStart`)*
- **Query Parameters**:
  - `topic` *(optional, string)*: Topic for prep. Default: `"Java & Spring Boot Microservices"`

#### Example Request
```bash
curl http://localhost:8081/api/v1/prepStart
```

#### Custom Topic Request
```bash
curl "http://localhost:8081/api/v1/prepStart?topic=Distributed%20Systems"
```

#### Example Response (`200 OK`)
```json
{
  "status": "SUCCESS",
  "message": "Interview preparation session initialized successfully",
  "sessionId": "4bb48530-5895-46aa-b001-eb37827e69d7",
  "topic": "Java & Spring Boot Microservices",
  "startedAt": "2026-10-23T15:25:00.000Z",
  "modules": [
    "Core Java Fundamentals & Memory Model",
    "Spring Boot & Microservices Architecture",
    "Concurrency & Multithreading",
    "Database Optimization & Distributed Caching",
    "System Design & REST API Best Practices"
  ]
}
```

---

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+

### Build and Run Tests
```bash
mvn clean test
```

### Run Locally with Maven
```bash
mvn spring-boot:run
```
The service will start on port `8081`.

---

## Running with Docker & Docker Compose

### Using Docker Compose (Recommended)
```bash
# Build and start container in detached mode
docker compose up -d --build

# View container logs
docker compose logs -f

# Stop container
docker compose down
```

### Using Docker CLI
```bash
# Build Docker image
docker build -t interview-prep-app:latest .

# Run container
docker run -d -p 8081:8081 --name interview-prep-app interview-prep-app:latest
```

Once running, test the endpoint:
```bash
curl http://localhost:8081/api/v1/prepStart
```

---

## Author
- **Mutyala Chandra Sekhara Nagarjuna**
- **Email**: nagarjunamutyala460@gmail.com
