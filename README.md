# Axis Banking Platform

Enterprise-grade microservices banking platform with AWS cloud integration, inspired by Axis Bank.

## Architecture

```
                                 ┌─────────────┐
                                 │   Route 53   │
                                 │     DNS      │
                                 └──────┬───────┘
                                        │
                         ┌──────────────┼──────────────┐
                         │              │              │
                    ┌────▼────┐   ┌────▼────┐   ┌────▼────┐
                    │CloudFront│   │   ALB   │   │   WAF   │
                    │  (CDN)  │   │         │   │         │
                    └────┬────┘   └────┬────┘   └─────────┘
                         │              │
                    ┌────▼────┐   ┌────▼────────────────────────┐
                    │ Angular │   │    API Gateway (8072)        │
                    │   UI    │   │  JWT Auth + Rate Limiter     │
                    │  (S3)   │   │  Circuit Breaker + Retry     │
                    └─────────┘   └────┬────────────────────────┘
                                       │
                            ┌──────────┼──────────────────────────┐
                            │     Eureka Server (8070)             │
                            │     Service Discovery                │
                            └──┬───┬───┬───┬───┬───┬───┬───┬──────┘
                               │   │   │   │   │   │   │   │
        ┌──────────────────────┼───┼───┼───┼───┼───┼───┼───┤
        │                      │   │   │   │   │   │   │   │
   ┌────▼────┐  ┌────▼───┐ ┌──▼──┐ ┌─▼──┐ ┌▼───┐ ┌▼───┐ ┌▼────┐ ┌▼────┐
   │  Auth   │  │Accounts│ │Cards│ │Loan│ │Dep │ │Inv │ │Ins │ │Pay  │
   │ (8073)  │  │ (8080) │ │8082│ │8083│ │8081│ │8084│ │8085│ │8086│
   └────┬────┘  └────┬───┘ └──┬──┘ └─┬──┘ └┬───┘ └┬───┘ └┬───┘ └┬───┘
        │             │        │      │     │      │      │      │
   ┌────▼─────────────▼────────▼──────▼─────▼──────▼──────▼──────▼────┐
   │                    Redis (ElastiCache)                             │
   │              Caching + Rate Limiting + Token Blacklist             │
   └───────────────────────────────────────────────────────────────────┘
   ┌───────────────────────────────────────────────────────────────────┐
   │                    Kafka (Amazon MSK)                             │
   │              Event Streaming Between Services                     │
   └───────────────────────────────────────────────────────────────────┘
   ┌───────────────────────────────────────────────────────────────────┐
   │                    Oracle Database (RDS)                          │
   │              Persistent Data Storage                              │
   └───────────────────────────────────────────────────────────────────┘
```

## Services

| Service | Port | Description |
|---------|------|-------------|
| Config Server | 8071 | Centralized configuration management |
| Eureka Server | 8070 | Service discovery and registration |
| API Gateway | 8072 | Routing, rate limiting, JWT auth, circuit breaker |
| Auth Service | 8073 | JWT OAuth2 authentication with user management |
| Accounts | 8080 | Customer accounts, savings/current/salary |
| Deposits | 8081 | FD, RD, tax-saving deposits |
| Cards | 8082 | Credit/debit card management |
| Loans | 8083 | Home, personal, auto, education, business loans |
| Investments | 8084 | Mutual funds, SIP, equity, bonds |
| Insurance | 8085 | Life, health, motor, travel insurance |
| Payments | 8086 | UPI, NEFT, RTGS, IMPS, bill payments |

## Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5, Spring Cloud 2023.0.1
- **Database**: Oracle (via JPA/Hibernate)
- **Cache**: Redis (Amazon ElastiCache)
- **Messaging**: Apache Kafka (Amazon MSK)
- **Security**: JWT OAuth2 (JJWT 0.12.5), Spring Security
- **Frontend**: Angular 18 (standalone components)
- **Cloud**: AWS (S3, CloudFront, Route 53, ALB, ECS, SQS, Secrets Manager, CloudWatch)

## Enterprise Features

### JWT Authentication
```
POST /api/auth/register   → Register new user
POST /api/auth/login      → Login, get access + refresh tokens
POST /api/auth/refresh    → Refresh expired access token
POST /api/auth/logout     → Invalidate token (Redis blacklist)
GET  /api/auth/validate   → Validate token
```

Roles: `CUSTOMER`, `ADMIN`, `MANAGER`, `TELLER`

### Redis Caching
- Account lookups cached by ID, number, and customer ID
- Card, loan, deposit, investment, insurance queries cached
- Cache eviction on create/update/delete operations
- TTL: 10 minutes (configurable)

### Rate Limiting
- Global: 10 req/sec per user, burst capacity 20
- Auth endpoints: 5 req/sec per IP (brute-force protection)
- Backed by Redis via Spring Cloud Gateway RequestRateLimiter

### Kafka Event Streaming
| Topic | Events |
|-------|--------|
| `axis.accounts.events` | ACCOUNT_CREATED, ACCOUNT_UPDATED, ACCOUNT_CLOSED |
| `axis.payments.events` | PAYMENT_INITIATED, PAYMENT_COMPLETED, PAYMENT_FAILED |
| `axis.loans.events` | LOAN_APPLIED, LOAN_APPROVED, LOAN_DISBURSED, LOAN_CLOSED |
| `axis.deposits.events` | DEPOSIT_CREATED, DEPOSIT_MATURED, DEPOSIT_CLOSED |
| `axis.cards.events` | CARD_ISSUED, CARD_BLOCKED, CARD_ACTIVATED |
| `axis.investments.events` | INVESTMENT_CREATED, INVESTMENT_REDEEMED |
| `axis.insurance.events` | POLICY_CREATED, POLICY_CANCELLED, CLAIM_SUBMITTED |
| `axis.dlq.events` | Dead letter queue for failed events |

### Circuit Breaker
- Resilience4j circuit breaker on critical services (accounts, payments, loans, cards, deposits)
- Sliding window: 10 calls, 50% failure threshold
- Fallback responses when services are unavailable

### AWS Integration
- **S3**: Angular static assets, document storage
- **CloudFront**: CDN for frontend with HTTPS
- **Route 53**: DNS management with health checks
- **ALB**: Application load balancer with WAF
- **ECS/Fargate**: Container deployment
- **SQS**: Dead letter queues for failed events
- **Secrets Manager**: Credential management with caching
- **CloudWatch**: Metrics (API latency, error counts)

## Quick Start

### Prerequisites
- Java 17+
- Node.js 18+
- Oracle Database or Docker
- Redis
- Apache Kafka

### Local Development
```bash
# Start infrastructure
cd infrastructure/docker
docker-compose up -d redis kafka zookeeper

# Build all services
mvn clean package -DskipTests

# Start services (in order)
java -jar config-server/target/*.jar
java -jar eureka-server/target/*.jar
java -jar api-gateway/target/*.jar
java -jar auth-service/target/*.jar
java -jar accounts-service/target/*.jar
# ... start remaining services

# Build Angular UI
cd banking-ui
npm install
ng serve
```

### Docker Deployment
```bash
# Build all images
mvn clean package -DskipTests
cd infrastructure/docker
docker-compose up -d
```

### AWS Deployment
Reference configs in `infrastructure/aws/`:
- `alb-config.json` - ALB with WAF rules
- `ecs-task-definition.json` - ECS Fargate task
- `cloudfront-distribution.json` - CDN setup
- `route53-config.json` - DNS configuration

## API Examples

### Register & Login
```bash
# Register
curl -X POST http://localhost:8072/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"secure123","email":"john@axis.com","fullName":"John Doe"}'

# Login
curl -X POST http://localhost:8072/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"secure123"}'
# Response: { "accessToken": "eyJ...", "refreshToken": "eyJ...", "role": "CUSTOMER" }
```

### Use Authenticated Endpoints
```bash
TOKEN="eyJ..."

# Create account
curl -X POST http://localhost:8072/api/accounts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"accountType":"SAVINGS","branchCode":"001"}'

# Initiate payment
curl -X POST http://localhost:8072/api/payments \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"fromAccount":"AXIS9123456789","toAccount":"HDFC987654","amount":5000,"paymentType":"NEFT"}'
```

## Project Structure
```
axis-banking/
├── common-lib/              # Shared library (Kafka, security, AWS clients)
├── config-server/           # Spring Cloud Config Server
├── eureka-server/           # Netflix Eureka Service Discovery
├── api-gateway/             # Spring Cloud Gateway (rate limiting, JWT, circuit breaker)
├── auth-service/            # JWT OAuth2 Authentication Service
├── accounts-service/        # Account & Customer management
├── deposits-service/        # Fixed/Recurring deposits
├── cards-service/           # Credit/Debit card management
├── loans-service/           # Loan application & management
├── investments-service/     # Mutual funds, SIP, equity
├── insurance-service/       # Insurance policy management
├── payments-service/        # Payment processing (UPI, NEFT, RTGS)
├── banking-ui/              # Angular 18 frontend
└── infrastructure/          # Docker & AWS configs
    ├── docker/              # docker-compose.yml
    └── aws/                 # CloudFront, Route53, ECS, ALB configs
```
