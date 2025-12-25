# Event-Driven Fraud Detection System

An event-driven fraud detection backend built with Spring Boot, Kafka, PostgreSQL, and AWS.
Transactions are processed asynchronously, evaluated by a rule engine, and fraud alerts
are delivered using AWS SNS, SQS, and Lambda. All AWS infrastructure is provisioned using Terraform.

---

## Architecture
![Architecture](fraud.png)

---

## System Flow
1. Client submits transaction via REST API
2. Transaction is persisted axnd published to Kafka
3. Fraud Engine consumes and evaluates rules
4. Fraud result is published to `fraud-results`
5. Alert Service publishes fraud alerts to SNS
6. SNS fans out to SQS
7. Lambda processes alerts with retries and DLQ support

---

## Tech Stack
- Java, Spring Boot
- Apache Kafka
- PostgreSQL
- AWS SNS, SQS, Lambda
- Terraform (Infrastructure as Code)
- Docker

---

## Fraud Detection
- Amount threshold rule (> $5000)
- Extensible rule-based engine design

---

## Infrastructure
- SNS, SQS, DLQ, Lambda, IAM provisioned via Terraform
- Application configuration via environment variables
- Local development with Kafka (Docker Compose)

---

## Future Enhancements
- Deploy services to ECS Fargate
- Replace Kafka with AWS MSK
- Velocity-based fraud detection
- Terraform modules for ECS and networking