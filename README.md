# Interview Prep Microservice

A lightweight Spring Boot microservice designed for interview preparation workflows.

## Features
- **Java 21 & Spring Boot 3.3.4**
- **Single Dedicated API Endpoint**: `prepStart` initializes and returns interview prep session modules and topics.
- **RESTful Design**: Clean JSON response model with session tracking.
- **Unit & Integration Tests**: Included with Spring MVC test support.

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

### Run Application
```bash
mvn spring-boot:run
```
The service will start on port `8081`.

---

## Author
- **Mutyala Chandra Sekhara Nagarjuna**
- **Email**: nagarjunamutyala460@gmail.com
