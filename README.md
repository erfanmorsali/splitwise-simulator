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