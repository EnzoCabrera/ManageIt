# 📄 ManageIt — Database Schema Documentation

This document describes the database schema for **ManageIt**, your all-in-one management system for small businesses.

---

## 🗂️ Tables Overview

| Table  | Description |
|--------|-------------|
| TGVUSE | Users       |
| TGVSTO | Stock Items |
| TGVCUS | Customers   |
| TGVEXP | Expenses    |

---

## 📑 TGVUSE (Users)

| Column      | Type      | Description               | Possible Values           | Notes                                                                                    |
|-------------|-----------|---------------------------|---------------------------|------------------------------------------------------------------------------------------|
| ID          | BIGINT    | Unique user ID            | Auto-increment            | Primary Key                                                                              |
| EMAIL       | VARCHAR   | User's email address      | —                         | Unique, not null                                                                         |
| PASSWORD    | VARCHAR   | Encrypted user password   | —                         | Bcrypt hashed                                                                            |
| ROLE        | VARCHAR   | User role                 | 0 = USER, 1 = ADMIN       | Uses backend enum(src/main/java/com/example/estoque/entities/userEntities/UserRole.java) |
| CREATED_AT  | TIMESTAMP | When user was created     | auto                      |                                                                                          |

---

## 📑 TGVSTO (Stock)

| Column         | Type      | Description                        | Possible Values | Notes                  |
|----------------|-----------|------------------------------------|-----------------|------------------------|
| CODPROD        | BIGINT    | Unique product ID                  | Auto-increment  | Primary Key            |
| PRODUCT_NAME   | VARCHAR   | Product name                       | —               | Not null               |
| QUANTITY       | INT       | Current stock quantity             | —               |                        |
| CREATED_AT     | TIMESTAMP | When record was created            | auto            |                        |
| UPDATED_AT     | TIMESTAMP | Last update timestamp              | auto            |                        |
| CREATED_BY     | VARCHAR   | User who created the record        | (TGVUSE.EMAIL)  |                        |
| UPDATED_BY     | VARCHAR   | Last user who updated the record   | (TGVUSE.EMAIL)  |                        |
| IS_DELETED     | BOOLEAN   | Soft delete flag                   | true / false    | Default: false         |

---

## 📑 TGVCUS (Customers)

| Column     | Type      | Description                        | Possible Values  | Notes                                                                                            |
|------------|-----------|------------------------------------|------------------|--------------------------------------------------------------------------------------------------|
| CODCUS     | BIGINT    | Unique customer ID                 | Auto-increment   | Primary Key                                                                                      |
| CUSNAME    | VARCHAR   | Customer name                      | —                | Not null                                                                                         |
| CUSADDR    | VARCHAR   | Customer address                   | —                |                                                                                                  |
| CUSCITY    | VARCHAR   | Customer city                      | —                |                                                                                                  |
| CUSTATE    | VARCHAR   | Customer state (UF)                | e.g., MG, SP, RJ | Enum in backend(src/main/java/com/example/estoque/entities/customerEntities/BrazilianState.java) |
| CUSZIP     | VARCHAR   | Postal code                        | —                |                                                                                                  |
| CUSPHONE   | VARCHAR   | Customer phone number              | —                |                                                                                                  |
| CUSEMAIL   | VARCHAR   | Customer email                     | —                |                                                                                                  |
| CREATED_AT | TIMESTAMP | When customer was added            | auto             |                                                                                                  |
| UPDATED_AT | TIMESTAMP | Last update timestamp              | auto             |                                                                                                  |
| CREATED_BY | VARCHAR   | User who created the record        | (TGVUSE.EMAIL)   |                                                                                                  |
| UPDATED_BY | VARCHAR   | Last user who updated the record   | (TGVUSE.EMAIL)   |                                                                                                  |
| IS_DELETED | BOOLEAN   | Soft delete flag                   | true / false     | Default: false                                                                                   |

---

## 📑 TGVEXP (Expenses)

| Column           | Type      | Description                       | Possible Values        | Notes                                                                                           |
|------------------|-----------|-----------------------------------|------------------------|-------------------------------------------------------------------------------------------------|
| CODEXP           | BIGINT    | Unique expense ID                 | Auto-increment         | Primary Key                                                                                     |
| EXPDESC          | VARCHAR   | Expense description               | —                      |                                                                                                 |
| EXPCOST_IN_CENTS | INT       | Expense cost in cents             | —                      | Integer for precision                                                                           |
| EXPDATE          | DATE      | Date the expense occurred         | YYYY-MM-DD             |                                                                                                 |
| EXPDATEPAY       | DATE      | Payment date                      | YYYY-MM-DD             | Must be >= EXPDATE                                                                              |
| EXPTYPE          | VARCHAR   | Expense type                      | e.g., SALARY, SUPPLIES | Enum in backend(src/main/java/com/example/estoque/entities/expenseEntities/ExpenseType.java)    |
| EXPSTS           | VARCHAR   | Expense status                    | e.g., PENDING, PAID    | Enum in backend(src/main/java/com/example/estoque/entities/expenseEntities/ExpenseStatus.java)  |
| CREATED_AT       | TIMESTAMP | When expense was added            | auto                   |                                                                                                 |
| UPDATED_AT       | TIMESTAMP | Last update timestamp             | auto                   |                                                                                                 |
| CREATED_BY       | VARCHAR   | User who created the expense      | (TGVUSE.EMAIL)         |                                                                                                 |
| UPDATED_BY       | VARCHAR   | Last user who updated the expense | (TGVUSE.EMAIL)         |                                                                                                 |
| IS_DELETED       | BOOLEAN   | Soft delete flag                  | true / false           | Default: false                                                                                  |

---

## 📢 Notes

- Dates follow ISO format: `YYYY-MM-DD`
- Soft delete: Records with `IS_DELETED = true` should be excluded in most queries.
- `ROLE`, `EXPTYPE` and `EXPSTS` values are enums in the backend — see the respective `Enum` classes.
- Keep this doc updated **every time** you create new migrations!


---

## Appendix: Enum Classes

### UserRole Enum
| Value | Meaning          |
|-------|------------------|
| 0     | USER             |
| 1     | ADMIN            |

- Source: src/main/java/com/example/estoque/entities/userEntities/UserRole.java

### BrazilianState Enum
| Value | Meaning             |
|-------|---------------------|
| AC    | Acre                |
| AL    | Alagoas             |
| AM    | Amazonas            |
| AP    | Amapá               |
| BA    | Bahia               |
| CE    | Ceará               |
| DF    | Distrito Federal    |
| ES    | Espírito Santo      |
| GO    | Goiás               |
| MA    | Maranhão            |
| MG    | Minas Gerais        |
| MS    | Mato Grosso do Sul  |
| MT    | Mato Grosso         |
| PA    | Pará                |
| PB    | Paraíba             |
| PE    | Pernambuco          |
| PI    | Piauí               |
| PR    | Paraná              |
| RJ    | Rio de Janeiro      |
| RN    | Rio Grande do Norte |
| RO    | Rondônia            |
| RR    | Roraima             |
| RS    | Rio Grande do Sul   |
| SC    | Santa Catarina      |
| SE    | Sergipe             |
| SP    | São Paulo           |
| TO    | Tocantins           |
- Source: src/main/java/com/example/estoque/entities/customerEntities/BrazilianState.java


### ExpenseType Enum

| Value       | Meaning                             |
|-------------|-------------------------------------|
| SALARY      | Employee salaries                   |
| SUPPLIES    | Office supplies                     |
| RENT        | Office rent                         |
| TAXES       | Government taxes                    |
| UTILITIES   | Electricity, water                  |
| SOFTWARE    | Software subscriptions and licenses |
| MARKETING   | Marketing and advertising expenses  |
| TRAVEL      | Business travel expenses            |
| INSURANCE   | Insurance payments                  |
| MAINTENANCE | Equipment or property maintenance   |
| OTHER       | Any other expense not listed above  |

- Source: src/main/java/com/example/estoque/entities/expenseEntities/ExpenseType.java


### ExpenseStatus Enum

| Value          | Meaning                                       |
|----------------|-----------------------------------------------|
| PENDING        | Not paid yet                                  |
| PAID           | It has already been paid off                  |
| CANCELLED      | It was cancelled before being paid            |
| OVERDUE        | Payment deadline has passed                   |
| PARTIALLY_PAID | Part has already been paid, but not the total |


- Source: src/main/java/com/example/estoque/entities/expenseEntities/ExpenseStatus.java






