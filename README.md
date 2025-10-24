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