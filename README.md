# Axis Banking Platform

A comprehensive banking microservices platform inspired by Axis Bank, built with **Java Spring Boot**, **Spring Cloud**, **Oracle Database**, and an **Angular** frontend.

## Architecture

```
                         ┌──────────────────┐
                         │   Angular UI      │
                         │   (Port 4200)     │
                         └────────┬─────────┘
                                  │
                         ┌────────▼─────────┐
                         │   API Gateway     │
                         │   (Port 8072)     │
                         └────────┬─────────┘
                                  │
              ┌───────────────────┼───────────────────┐
              │                   │                    │
    ┌─────────▼──────┐  ┌────────▼───────┐  ┌────────▼───────┐
    │ Config Server   │  │ Eureka Server  │  │  Business      │
    │ (Port 8071)     │  │ (Port 8070)    │  │  Microservices │
    └────────────────┘  └────────────────┘  └────────────────┘
```

### Business Microservices

| Service | Port | Description |
|---------|------|-------------|
| **Accounts** | 8080 | Customer & account management (Savings, Current, Salary, NRI) |
| **Deposits** | 8081 | Fixed deposits, recurring deposits, tax-saving FDs |
| **Cards** | 8082 | Credit, debit, prepaid & forex card management |
| **Loans** | 8083 | Home, personal, auto, education, gold & business loans |
| **Investments** | 8084 | Mutual funds, SIPs, stocks, bonds, PPF, NPS |
| **Insurance** | 8085 | Life, health, vehicle, home & travel insurance |
| **Payments** | 8086 | UPI, NEFT, RTGS, IMPS, fund transfers & bill payments |

### Infrastructure Services

| Service | Port | Description |
|---------|------|-------------|
| **Config Server** | 8071 | Centralized configuration management |
| **Eureka Server** | 8070 | Service discovery & registration |
| **API Gateway** | 8072 | Single entry point with intelligent routing |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5, Spring Cloud 2023.0.1
- **Database**: Oracle Database (ojdbc11)
- **Frontend**: Angular 18, TypeScript, SCSS
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **ORM**: Spring Data JPA / Hibernate

## Prerequisites

- Java 17+
- Maven 3.8+
- Oracle Database (or configure for H2 in test profile)
- Node.js 18+ and npm
- Angular CLI 18

## Getting Started

### Backend

1. **Start Config Server**
   ```bash
   cd config-server && mvn spring-boot:run
   ```

2. **Start Eureka Server**
   ```bash
   cd eureka-server && mvn spring-boot:run
   ```

3. **Start API Gateway**
   ```bash
   cd api-gateway && mvn spring-boot:run
   ```

4. **Start Business Services** (each in a separate terminal)
   ```bash
   cd accounts-service && mvn spring-boot:run
   cd deposits-service && mvn spring-boot:run
   cd cards-service && mvn spring-boot:run
   cd loans-service && mvn spring-boot:run
   cd investments-service && mvn spring-boot:run
   cd insurance-service && mvn spring-boot:run
   cd payments-service && mvn spring-boot:run
   ```

### Frontend

```bash
cd banking-ui
npm install
ng serve
```

Open `http://localhost:4200` in your browser.

### Oracle Database Setup

Create separate schemas for each service:

```sql
CREATE USER accounts_user IDENTIFIED BY password;
CREATE USER deposits_user IDENTIFIED BY password;
CREATE USER cards_user IDENTIFIED BY password;
CREATE USER loans_user IDENTIFIED BY password;
CREATE USER investments_user IDENTIFIED BY password;
CREATE USER insurance_user IDENTIFIED BY password;
CREATE USER payments_user IDENTIFIED BY password;

-- Grant privileges to each user
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO accounts_user;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO deposits_user;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO cards_user;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO loans_user;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO investments_user;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO insurance_user;
GRANT CONNECT, RESOURCE, CREATE TABLE, CREATE SEQUENCE TO payments_user;
```

## API Endpoints

### Accounts Service (`/api/accounts`)
- `POST /api/accounts/customers` - Create customer
- `GET /api/accounts/customers` - List customers
- `POST /api/accounts` - Open account
- `GET /api/accounts` - List accounts
- `GET /api/accounts/customer/{customerId}` - Accounts by customer

### Deposits Service (`/api/deposits`)
- `POST /api/deposits` - Create deposit
- `GET /api/deposits` - List deposits
- `PUT /api/deposits/{id}/close` - Close deposit

### Cards Service (`/api/cards`)
- `POST /api/cards` - Issue card
- `GET /api/cards` - List cards
- `PUT /api/cards/{id}/block` - Block card
- `PUT /api/cards/{id}/activate` - Activate card

### Loans Service (`/api/loans`)
- `POST /api/loans` - Apply for loan
- `GET /api/loans` - List loans
- `PUT /api/loans/{id}/approve` - Approve loan
- `PUT /api/loans/{id}/close` - Close loan

### Investments Service (`/api/investments`)
- `POST /api/investments` - Create investment
- `GET /api/investments` - List investments
- `PUT /api/investments/{id}/redeem` - Redeem investment

### Insurance Service (`/api/insurance`)
- `POST /api/insurance` - Create policy
- `GET /api/insurance` - List policies
- `PUT /api/insurance/{id}/cancel` - Cancel policy

### Payments Service (`/api/payments`)
- `POST /api/payments` - Initiate payment
- `GET /api/payments` - List payments
- `GET /api/payments/customer/{customerId}` - Payments by customer

## Project Structure

```
axis-banking/
├── pom.xml                    # Parent POM
├── config-server/             # Centralized configuration
├── eureka-server/             # Service discovery
├── api-gateway/               # API Gateway
├── accounts-service/          # Accounts microservice
├── deposits-service/          # Deposits microservice
├── cards-service/             # Cards microservice
├── loans-service/             # Loans microservice
├── investments-service/       # Investments microservice
├── insurance-service/         # Insurance microservice
├── payments-service/          # Payments microservice
└── banking-ui/                # Angular frontend
```

Each microservice follows the pattern:
```
*-service/
└── src/main/java/com/axisbanking/*/
    ├── model/          # JPA entities & enums
    ├── dto/            # Data Transfer Objects
    ├── repository/     # Spring Data JPA repositories
    ├── service/        # Business logic (interface + impl)
    ├── controller/     # REST endpoints
    └── exception/      # Custom exceptions & handlers
```
