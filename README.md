# 📦 ManageIt - Inventory and Order Management System

**ManageIt** is a complete application for managing inventory, orders, and customers, developed in **Java + Spring Boot**, focused on precise control, detailed auditing, and role-based security.  
The system is designed to be modular, scalable, and easily integrated into dashboards and reports.

---

## 🚀 Main Features

- **User Management**
  - User registration and authentication with JWT
  - Role-based access control (**ADMIN, MANAGER, OPERATOR, AUDITOR, VIEWER**)
  - Endpoint access restrictions by role

- **Inventory Management**
  - Create, update, and delete products
  - Minimum quantity tracking
  - Automatic email notifications when stock is below the minimum
  - Pre-built queries for dashboards

- **Order Management**
  - Create, edit, and cancel orders
  - Automatic stock updates when orders are placed
  - Reports and KPI queries (e.g., top customers, highest spenders)

- **Customer Management**
  - Register and manage customers
  - Queries for performance and sales indicators

- **Full Audit Logging**
  - Logs all changes in the system (`TGVLOG`)
  - Stores **entity**, **entity ID**, **action**, **field changed**, **old value**, **new value**, **responsible user**
  - Filter logs by entity

---

## 🛠 Tech Stack

- **Backend:** Java 21, Spring Boot 3.x
- **Security:** Spring Security + JWT
- **Database:** PostgreSQL (Docker)
- **ORM:** Spring Data JPA / Hibernate
- **Documentation:** Springdoc OpenAPI (Swagger)
- **Others:** Lombok, Jakarta Validation, Email Service

---

## 🔒 Role-Based Access Control

| Role       | Main Permissions |
|------------|------------------|
| **ADMIN**  | Full access to all endpoints |
| **MANAGER**| Manage users, inventory, and orders |
| **OPERATOR** | Create and manage orders and inventory |
| **AUDITOR** | View logs and audit data |
| **VIEWER**  | Read-only access to data and reports |

---

## 📊 Implemented Queries & Reports

- Top 5 customers with the highest daily spending
- Products below the minimum stock level
- Order statistics by period and status
- Full change history by entity

---

## 📧 Automatic Notifications

- Sends email alerts to responsible users when a product is below its defined minimum quantity.

---

## ⚙️ How to Run Locally

### Prerequisites
- **Java 21+**
- **Maven 3.9+**
- **Docker** and **Docker Compose**

### Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/EnzoCabrera/ManageIt.git
   cd ManageIt

2. Start the application using Docker:
   ```bash
   docker-compose up -d

3. Run the application:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

4. Download the Insomnia API Requests Collection
   [Click here to download](docs/Insomnia-requests.yaml)

