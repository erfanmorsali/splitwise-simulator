# 💸 Splitwise Simulator – Group Expense Management System

Splitwise Simulator is a modular Spring Boot–based application inspired by Splitwise, designed to manage shared expenses, balances, and settlements among group members.  
It demonstrates production-level architecture choices such as modular design, event-driven communication, distributed locking, and secure authentication.

## 🧱 Overview

The system allows users to:
- Create and manage groups
- Add and share expenses among members
- Track balances between users
- Invite new members to groups (with accept/reject flow)
- Send notifications when key events occur (e.g., expense added, invite accepted)
- Authenticate securely using JWT and OTP-based login

The project is composed of multiple modules/services:
- Application Service – Handles groups, costs, balances, and authentication
- Notification Service – Consumes domain events and sends user notifications
- Shared Module – Contains shared DTOs, event models, and common configurations

## ⚙️ Tech Stack

| Component | Technology |
|------------|-------------|
| Language | Java 17 |
| Framework | Spring Boot |
| Database | PostgreSQL |
| Cache / Rate Limit | Redis |
| Message Broker | Apache Kafka |
| Scheduler Locking | ShedLock |
| Containerization | Docker Compose |
| API Docs | Swagger / OpenAPI |
| Authentication | JWT + OTP (via Redis, rate-limited by IP) |

## 🔐 Security

- JWT-based authentication ensures stateless and scalable sessions.
- OTP login improves security and user experience.
- IP-based rate limiting is implemented using Redis to prevent OTP abuse.
- The design allows easy extraction of the authentication module into a standalone Auth Service in future scaling phases.
- For test otp code returns from request-otp APi but for production it will send with SMS/EMAIL

### 📤 Event Flow & Outbox Pattern

1. Whenever an important action occurs (e.g., invite accepted, expense added),  
   an event is saved into an events table in the same transaction.

2. A scheduler runs every 20 seconds and fetches up to 5000 unsent events.

3. The scheduler publishes those events to Kafka topics and delete them if succeed and flag them as failed if failed.

4. ShedLock is used to ensure that only one instance of the scheduler executes the job at a time (prevents double sending).

> 📝 Design Note:  
> In a real production setup, Debezium or Change Data Capture (CDC) could be used instead of a scheduler to provide a more scalable and near-real-time event streaming mechanism.  
> However, a scheduler was chosen here for simplicity and clarity, making it easier to demonstrate the outbox pattern within a limited project timeframe.


## 🧠 Design Decisions

- Outbox Pattern → Guarantees reliable event delivery and transactional consistency.
- ShedLock → Prevents concurrency issues in distributed environments.
- Modular Design → Shared module separates reusable logic and models.
- Redis for OTP & Rate Limit → Ensures fast lookups and easy expiration handling.
- Kafka for Event Communication → Enables decoupling and scalability between core and notification modules.
- Docker Compose → Simplifies local orchestration of PostgreSQL, Redis, Kafka, and all app services.

## 🐳 Docker Setup

The entire system can be run locally using Docker Compose:


```console
docker-compose up --build
```

## API Usage

After running the application, you can access the API documentation (Swagger UI) here:

[Swagger UI](http://localhost:7000/api/v1/swagger-ui/index.html)