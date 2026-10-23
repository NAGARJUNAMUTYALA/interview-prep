# AWS Cloud Architecture for Java Enterprise Applications

**Target Audience:** Senior Java Backend Engineers & Architects  
**Topic:** AWS Core Services, VPC Networking, Compute (ECS/EKS), RDS Multi-AZ, S3, and CloudWatch Observability  

---

## 1. Enterprise AWS Network & Topology (VPC)

```text
                        Internet
                           │
                           ▼
                 [AWS Internet Gateway]
                           │
    ┌──────────────────────┴──────────────────────┐
    │ Public Subnet (AZ-1)        Public Subnet (AZ-2)
    │  [NAT Gateway 1]             [NAT Gateway 2]
    │  [Application Load Balancer (ALB) - Public]
    └──────────────────────┬──────────────────────┘
                           │ (Forward to internal IP)
    ┌──────────────────────┴──────────────────────┐
    │ Private App Subnet (AZ-1)   Private App Subnet (AZ-2)
    │  [EKS / ECS Java Pods]       [EKS / ECS Java Pods]
    │  (No public IP addresses)    (Scales via HPA)
    └──────────────────────┬──────────────────────┘
                           │
    ┌──────────────────────┴──────────────────────┐
    │ Isolated DB Subnet (AZ-1)   Isolated DB Subnet (AZ-2)
    │  [RDS PostgreSQL Primary] ──▶ [RDS Read Replica / Standby]
    └─────────────────────────────────────────────┘
```

### Core Security & Networking Rules:
1. **Zero Public Exposure for Backend:** Spring Boot microservices run strictly inside **Private Subnets** without public IP addresses. Outbound internet access (e.g. third-party APIs) flows through a **NAT Gateway**.
2. **Database Isolation:** PostgreSQL RDS resides in isolated database subnets without internet access, reachable only via Security Group rules from the application subnet on port 5432.
3. **Secrets Management:** Credentials and database passwords are stored in **AWS Secrets Manager** or AWS Systems Manager Parameter Store and injected dynamically into container environment variables.

---

## 2. Compute: Amazon ECS vs. Amazon EKS for Java Microservices

| Dimension | AWS ECS (Fargate) | AWS EKS (Kubernetes) |
|---|---|---|
| **Complexity** | Simple, AWS-native task definitions | High, full Kubernetes control plane |
| **Maintenance** | Serverless via AWS Fargate (No EC2 patching) | Requires K8s upgrades, Helm charts, ingress controllers |
| **Portability** | AWS proprietary container orchestrator | 100% cloud-agnostic; manifests run on GCP/Azure/Local |
| **Adoption Strategy** | Ideal for startups & small-to-mid engineering teams | Standard for large enterprise multi-cloud microservices |

---

## 3. High-Frequency Cloud Interview Questions

### Q1: How does RDS Multi-AZ differ from RDS Read Replicas?
**Answer:**
- **Multi-AZ:** Synchronous physical replication to a standby instance in a different Availability Zone. The standby cannot serve read traffic. Its sole purpose is **automatic failover** within 60–120 seconds if the primary crashes.
- **Read Replicas:** Asynchronous logical replication used to scale **read throughput** (read-heavy workloads). Applications route `SELECT` queries to read replica endpoints and write operations (`INSERT`/`UPDATE`) to the primary.

### Q2: What is the difference between IAM Roles and IAM Users?
**Answer:**
- **IAM User:** Permanent credentials (username/password, access keys) associated with a human developer or legacy service. Risk of credential leakage.
- **IAM Role:** Temporary, short-lived security tokens assumed by compute entities (EC2 instances, ECS Tasks, EKS Pods via IRSA). Best practice: never store hardcoded AWS keys inside container images; use IAM Roles attached to the container task.
