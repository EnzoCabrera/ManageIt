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
| TGVORD | Orders      |
| TGVITE | Items       |
| TGVLOG | Logs        |


---

## 📑 TGVUSE (Users)

| Column     | Type      | Description                      | Possible Values      | Notes                                                                                    |
|------------|-----------|----------------------------------|----------------------|------------------------------------------------------------------------------------------|
| ID         | BIGINT    | Unique user ID                   | Auto-increment       | Primary Key                                                                              |
| EMAIL      | VARCHAR   | User's email address             | —                    | Unique, not null                                                                         |
| PASSWORD   | VARCHAR   | Encrypted user password          | —                    | Bcrypt hashed                                                                            |
| ROLE       | VARCHAR   | User role                        | 0 = USER, 1 = ADMIN  | Uses backend enum(src/main/java/com/example/estoque/entities/userEntities/UserRole.java) |
| CREATED_AT | TIMESTAMP | When user was created            | auto                 |                                                                                          |
| IS_DELETED | BOOLEAN   | Soft delete flag                 | true / false         | Default: false                                                                           |
| IS_ACTIVE  | BOOLEAN   | Active flag                      | true / false         | Default: true                                                                            |
| CREATED_BY | VARCHAR   | User who created the record      | (TGVUSE.EMAIL)       |                                                                                          |
| UPDATED_BY | VARCHAR   | Last user who updated the record | (TGVUSE.EMAIL)       |                                                                                          |
| UPDATED_AT | TIMESTAMP | When user was created            | auto                 |                                                                                          |

---

## 📑 TGVSTO (Stock)

| Column          | Type      | Description                                           | Possible Values | Notes          |
|-----------------|-----------|-------------------------------------------------------|-----------------|----------------|
| CODPROD         | BIGINT    | Unique product ID                                     | Auto-increment  | Primary Key    |
| PRODUCT_NAME    | VARCHAR   | Product name                                          | —               | Not null       |
| QUANTITY        | INT       | Current stock quantity                                | —               |                |
| UNPRIC_IN_CENTS | INT       | Current unit price of each product                    | —               | Default: 0     |
| UNTYPE          | VARCHAR   | Product unit type                                     | e.g., UNIT, BOX | Default: UNIT  |
| UNQTT           | INT       | Product default quantity based on unit type           | —               | Default: 1     |
| MINIMUM_QTD     | INT       | Minimun quantity of stock that the product could have | —               | Default: 0     |
| CREATED_AT      | TIMESTAMP | When record was created                               | auto            |                |
| UPDATED_AT      | TIMESTAMP | Last update timestamp                                 | auto            |                |
| CREATED_BY      | VARCHAR   | User who created the record                           | (TGVUSE.EMAIL)  |                |
| UPDATED_BY      | VARCHAR   | Last user who updated the record                      | (TGVUSE.EMAIL)  |                |
| IS_DELETED      | BOOLEAN   | Soft delete flag                                      | true / false    | Default: false |
| IS_ACTIVE       | BOOLEAN   | Active flag                                           | true / false    | Default: true  |

---

## 📑 TGVCUS (Customers)

| Column     | Type      | Description                      | Possible Values  | Notes                                                                                            |
|------------|-----------|----------------------------------|------------------|--------------------------------------------------------------------------------------------------|
| CODCUS     | BIGINT    | Unique customer ID               | Auto-increment   | Primary Key                                                                                      |
| CUSNAME    | VARCHAR   | Customer name                    | —                | Not null                                                                                         |
| CUSADDR    | VARCHAR   | Customer address                 | —                |                                                                                                  |
| CUSCITY    | VARCHAR   | Customer city                    | —                |                                                                                                  |
| CUSTATE    | VARCHAR   | Customer state (UF)              | e.g., MG, SP, RJ | Enum in backend(src/main/java/com/example/estoque/entities/customerEntities/BrazilianState.java) |
| CUSZIP     | VARCHAR   | Postal code                      | —                |                                                                                                  |
| CUSPHONE   | VARCHAR   | Customer phone number            | —                |                                                                                                  |
| CUSEMAIL   | VARCHAR   | Customer email                   | —                |                                                                                                  |
| CREATED_AT | TIMESTAMP | When customer was added          | auto             |                                                                                                  |
| UPDATED_AT | TIMESTAMP | Last update timestamp            | auto             |                                                                                                  |
| CREATED_BY | VARCHAR   | User who created the record      | (TGVUSE.EMAIL)   |                                                                                                  |
| UPDATED_BY | VARCHAR   | Last user who updated the record | (TGVUSE.EMAIL)   |                                                                                                  |
| IS_DELETED | BOOLEAN   | Soft delete flag                 | true / false     | Default: false                                                                                   |
| IS_ACTIVE  | BOOLEAN   | Active flag                      | true / false     | Default: true                                                                                    |                              

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

## 📑 TGVORD (Orders)
| Column           | Type      | Description                     | Possible Values     | Notes                                                                                           |
|------------------|-----------|---------------------------------|---------------------|-------------------------------------------------------------------------------------------------|
| CODORD           | BIGINT    | Unique order ID                 | Auto-increment      | Primary Key                                                                                     |
| ORDCOST_IN_CENTS | INT       | Order cost in cents             | —                   | Integer for precision                                                                           |
| CODCUS           | BIGINT    | Order customer code             | —                   |                                                                                                 |
| ORDSTS           | VARCHAR   | Order status                    | e.g., PENDING, PAID | Enum in backend(src/main/java/com/example/estoque/entities/orderEntities/OrderStatus.java)      |
| ORDPAYTYPE       | VARCHAR   | Order Payment type              | e.g., PIX, DEBIT    | Enum in backend(src/main/java/com/example/estoque/entities/orderEntities/OrderPaymentType.java) |
| ORDPAYDUE        | DATE      | Order payment date              | YYYY-MM-DD          |                                                                                                 |
| ORDNOTE          | VARCHAR   | Order note                      | -                   |                                                                                                 |
| CREATED_AT       | TIMESTAMP | When order was added            | auto                |                                                                                                 |
| UPDATED_AT       | TIMESTAMP | Last update timestamp           | auto                |                                                                                                 |
| CREATED_BY       | VARCHAR   | User who created the order      | (TGVUSE.EMAIL)      |                                                                                                 |
| UPDATED_BY       | VARCHAR   | Last user who updated the order | (TGVUSE.EMAIL)      |                                                                                                 |
| IS_DELETED       | BOOLEAN   | Soft delete flag                | true / false        | Default: false                                                                                  |


---

## 📑 TGVITE (Items in Orders)
| Column          | Type      | Description                                          | Possible Values | Notes                                |
|-----------------|-----------|------------------------------------------------------|-----------------|--------------------------------------|
| CODITE          | BIGINT    | Unique order ID                                      | Auto-increment  | Primary Key                          |
| CODORD          | BIGINT    | Order ID                                             | —               | Foreign Key → TGVORD (CODORD)        |
| CODPROD         | BIGINT    | Product ID                                           | —               | Foreign Key → TGVSTO (CODPROD)       |
| QUANTITY        | INT       | Quantity of the product in the order                 | —               |                                      |
| PRICEINCENTS    | INT       | Unit price at the time of order (cents)              | —               | Snapshot of unit price at order time |
| DISCOUNTPERCENT | REAL      | Discount percentage for this item                    | —               | Example: 10.0 for 10%                |
| TOTALINCENTS    | INT       | Final item total (with discount applied)             | —               | (unit price × quantity) - discount   |
| CREATED_AT      | TIMESTAMP | When item was added                                  | auto            |                                      |
| UPDATED_AT      | TIMESTAMP | Last update timestamp                                | auto            |                                      |
| CREATED_BY      | VARCHAR   | User who created the order that have this items      | (TGVUSE.EMAIL)  |                                      |
| UPDATED_BY      | VARCHAR   | Last user who updated the order that have this items | (TGVUSE.EMAIL)  |                                      |
| IS_DELETED      | BOOLEAN   | Soft delete flag                                     | true / false    | Default: false                       |


---

## 📑 TGVLOG (Logs)
| Column     | Type      | Description                                 | Possible Values | Notes       |
|------------|-----------|---------------------------------------------|-----------------|-------------|
| CODLOG     | BIGINT    | Unique log ID                               | Auto-increment  | Primary Key |
| ENTITY     | VARCHAR   | Entity that was created, changed or deleted | —               |             |
| ENTITY_ID  | BIGINT    | Entity ID                                   | —               |             |
| ACTION     | VARCHAR   | Which action was made: CREATED, UPDATED...  | —               |             |
| FIELD      | VARCHAR   | Which field suffered the action             | —               |             |
| OLD_VALUE  | VARCHAR   | Value before the action                     | —               |             |
| NEW_VALUE  | VARCHAR   | Value after the action                      | —               |             |
| CHANGED_BY | VARCHAR   | Who made the action                         | (TGVUSE.EMAIL)  |             |
| CREATED_BY | TIMESTAMP | When the action was made                    | auto            |             |

## 📢 Notes

- Dates follow ISO format: `YYYY-MM-DD`
- Soft delete: Records with `IS_DELETED = true` should be excluded in most queries.
- `ROLE`, `EXPTYPE` and `EXPSTS` values are enums in the backend — see the respective `Enum` classes.
- Keep this doc updated **every time** you create new migrations!


---

## Appendix: Enum Classes

### UserRole Enum
| Value    | Meaning  |
|----------|----------|
| ADMIN    | Admin    |
| MANAGER  | Manager  |
| OPERATOR | Operator |
| AUDITOR  | Auditor  |
| Viewer   | Viewer   |

- Source: src/main/java/com/example/estoque/entities/userEntities/UserRole.java

---

### StockUnitType Enum
| Value   | Meaning |
|---------|---------|
| UNIT    | Unit    |
| BOX     | Box     |
| PACKAGE | Package |

- Source: src/main/java/com/example/estoque/entities/stockEntities/StockUnitType.java

---

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


### OrderStatus Enum

| Value     | Meaning                                      |
|-----------|----------------------------------------------|
| PENDING   | Order has been created but not yet processed |
| PAID      | Order has been paid                          |
| OVERDUE   | Payment deadline has passed                  |
| CANCELLED | Order has been cancelled                     |

- Source: src/main/java/com/example/estoque/entities/orderEntities/OrderStatus.java


### OrderPaymentType Enum

| Value  | Meaning                 |
|--------|-------------------------|
| PIX    | Payment via PIX         |
| DEBIT  | Payment via debit card  |
| CREDIT | Payment via credit card |
| BOLETO | Payment in BOLETO       |

- Source: src/main/java/com/example/estoque/entities/orderEntities/OrderPaymentType.java


