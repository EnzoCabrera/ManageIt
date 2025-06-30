# 📦 ManageIT

**ManageIT** is a modern, containerized management system built to help businesses **organize their inventory, manage clients, and control expenses** — all in one place and with secure access.

---

## 🚀 Tech Stack

This project is powered by:
- **Java 21** — Main programming language
- **Spring Boot 3.x** — For building the RESTful API
- **Spring Security** — For authentication and JWT-based authorization
- **Spring Data JPA** — Data persistence with Hibernate
- **Flyway** — Database version control with SQL migrations
- **PostgreSQL 15** — Relational database
- **Docker & Docker Compose** — For containerizing the app and the database
- **IntelliJ IDEA** - Recommended IDE

---

## 📚 What You Can Do with ManageIT

Right now, **ManageIT** lets you:

✅ **Manage Inventory**  
- Register products with price, quantity, and active/inactive status  
- Full CRUD operations for stock management

✅ **Manage Clients**  
- Register clients with complete contact and address info  
- Create, update, or soft-delete client records

✅ **Track Expenses**  
- Log company expenses with description, cost, type, issue date, and payment date  
- Filter expenses by month and year to stay organized

✅ **Secure Auth**  
- Register and login users with encrypted passwords (BCrypt)  
- Generate and validate JWT tokens for secure access

---

## ⚙️ Getting Started with Docker

Want to run everything locally in an isolated, production-like environment? Here’s how!

### 1️⃣ Prerequisites

    Docker & Docker Compose installed
    Make sure ports **5432** (PostgreSQL) and **8080** (API) are free

### 2️⃣ Set Up Your Environment

Create a **`.env`** file in the root of the project:

Copy this variables to .env and fill in your secrets:
  
    POSTGRES_USER=
    POSTGRES_PASSWORD=
    POSTGRES_DB=  
    JWT_SECRET=

### 3️⃣ Run It All

Spin up the containers with one command: 

    docker-compose up --build

### 4️⃣ Access It

- The API is live at: http://localhost:8080

- PostgreSQL is available at localhost:5432 — connect with your favorite SQL client

