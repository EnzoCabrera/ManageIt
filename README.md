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

✅ Manage Orders
- Create orders linked to customers with payment type, due date, and notes
- Add multiple items per order with automatic stock validation and discount support
- Update or soft-delete orders, with items and stock adjusted automatically

✅ **Secure Auth**  
- Register and login users with encrypted passwords (BCrypt)  
- Generate and validate JWT tokens for secure access

---

## 📧 Email Notifications

ManageIt automatically sends email alerts to the user when:

- An expense is **one day away** from its payment date (`expdatepay`) and its status is still `PENDING`.
- An expense has become `OVERDUE`.

This helps users avoid any penalties or disruptions.
  
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

---

## 📌 API Endpoints — ManageIt

Here’s a quick reference for all available API routes in **ManageIt**, so you can test everything easily in Postman or any other API client.

---

### 🧑‍💼 Users(TGVUSE)

| Method | Endpoint              | Description                       |
|--------|-----------------------|-----------------------------------|
| POST   | `/api/users/register` | Register a new user               |
| POST   | `/api/auth/login`     | Authenticate user (JWT token)     |

---

### 📦 Stock(TGVSTO)

| Method | Endpoint                            | Description                                  |
|--------|-------------------------------------|----------------------------------------------|
| POST   | `/api/stock/product/registry`       | Register new stock item                      |
| GET    | `/api/stock/see?codprod=`           | Get stock items by product code(codprod)     |
| GET    | `/api/stock/see?productName=`       | Get stock items by product name(productName) |
| GET    | `/api/stock/see`                    | Get all stock items                          |
| PUT    | `/api/stock/product/update/{id}`    | Update stock item by ID                      |
| DELETE | `/api/stock/product/delete/{id}`    | Soft delete stock item by ID                 |

✅ **Example filter:**  

| Method | Endpoint                            | 
|--------|-------------------------------------|
| GET    | `/api/stock/see?productName=Widget` |
| GET    | `/api/stock/see?codprod=1`          |

---

### 💸 Expenses(TGVEXP)

| Method | Endpoint                                    | Description                                  |
|--------|---------------------------------------------|----------------------------------------------|
| POST   | `/api/expense/register`                     | Register new expense                         |
| GET    | `/api/expense/see?type=`                    | Get expenses by type                         |
| GET    | `api/expense/see?startDate=&endDate=`       | Get expenses by date range                   |
| GET    | `api/expense/see?startDatePay=&endDatePay=` | Get expenses by payment date range           |
| GET    | `/api/expense/see?expSts=`                  | Get expenses by status                       |
| GET    | `/api/expense/see/monthly-summary`          | Get current month expenses                   |
| GET    | `/api/expense/see/type-summary`             | Get current month expenses by type           |
| GET    | `/api/expense/see/top5-summary`             | Get current month top 5 expenses             |
| GET    | `/api/expense/see/status-summary`           | Get current month expenses by status         |
| GET    | `/api/expense/see/unpaid-summary`           | Get current month expenses unpaid            |
| GET    | `/api/expense/see`                          | Get all expenses                             |
| PUT    | `/api/expense/update/{id}`                  | Update expense by ID                         |
| DELETE | `/api/expense/delete/{id}`                  | Soft delete expense by ID                    |

✅ **Example filter:**  

| Method | Endpoint                                                         | 
|--------|------------------------------------------------------------------|
| GET    | `/api/expense/see?type=SALARY`                                   |
| GET    | `/api/expense/see?startDate=2025-06-01&endDate=2025-06-30`       |
| GET    | `/api/expense/see?startDatePay=2025-06-01&endDatePay=2025-06-30` |

---

### 🧑‍🤝‍🧑 Customers(TGVCUS)

| Method | Endpoint                       | Description                  |
|--------|--------------------------------|------------------------------|
| POST   | `/api/customers/register`      | Register new customer        |
| GET    | `/api/customer/see?name=`      | Get  customers by name       |
| GET    | `/api/customer/see?address=`   | Get customers by address     |
| GET    | `/api/customer/see?city=`      | Get customers by city        |
| GET    | `/api/customer/see?state=`     | Get customers by state       |
| GET    | `/api/customer/see?zipCode=`   | Get customers by zip code    |
| GET    | `/api/customer/see?phone=`     | Get customers by phone       |
| GET    | `/api/customer/see?email=`     | Get customers by email       |
| GET    | `/api/customer/see`            | Get all customers            |
| PUT    | `/api/customers/update/{id}`   | Update customer by ID        |
| DELETE | `/api/customers/delete/{id}`   | Soft delete customer by ID   |

✅ **Example filter:**  

| Method | Endpoint                                   | 
|--------|--------------------------------------------|
| GET    | `/api/customers/see?phone=34999999999`     |
| GET    | `/api/customers/see?email=teste@gmail.com` |

---

### 🧾 Orders(TGVORD)

| Method | Endpoint                             | Description                  |
|--------|--------------------------------------|------------------------------|
| POST   | `/api/customers/register`            | Register new order           |
| GET    | `/api/order/see?codord=`             | Get  orders by code          |
| GET    | `/api/order/see?codcus=`             | Get orders by customers code |
| GET    | `/api/order/see?startDate=&endDate=` | Get order by date range      |
| GET    | `/api/order/see?ordsts=`             | Get order by status          |
| GET    | `/api/order/see?ordpaytype=`         | Get order by payment type    |
| GET    | `/api/order/see`                     | Get all orders               |
| PUT    | `/api/order/update/{id}`             | Update orders by ID          |
| DELETE | `/api/order/delete/{id}`             | Soft delete orders by ID     |

✅ **Example filter:**  

| Method | Endpoint                                                 | 
|--------|----------------------------------------------------------|
| GET    | `/api/order/see?startDate=2025-07-01&endDate=2025-07-31` |
| GET    | `/api/order/see?ordpaytype=CREDIT`                       | 

---

## ✅ How to use

1. **Base URL:** `http://localhost:8080`
2. Use **Bearer Token** for endpoints that require authentication.
3. For **filters**, pass query params as shown above.

