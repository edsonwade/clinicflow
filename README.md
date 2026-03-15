# clinicflow

```markdown
<div align="center">

<img src="docs/assets/clinicflow-logo.svg" alt="ClinicFlow Logo" width="120" />

# ClinicFlow

**The all-in-one SaaS platform for modern medical clinics**

[![CI/CD](https://github.com/clinicflow/clinicflow/actions/workflows/ci-cd.yml/badge.svg)](https://github.com/clinicflow/clinicflow/actions/workflows/ci-cd.yml)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=clinicflow-backend&metric=coverage)](https://sonarcloud.io/summary/new_code?id=clinicflow-backend)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=clinicflow-backend&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=clinicflow-backend)
[![License](https://img.shields.io/badge/license-Proprietary-red.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://react.dev)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.4-blue.svg)](https://www.typescriptlang.org)

[🌐 Live Demo](https://demo.clinicflow.io) · [📖 Documentation](https://docs.clinicflow.io) · [🐛 Report Bug](https://github.com/clinicflow/clinicflow/issues) · [💡 Request Feature](https://github.com/clinicflow/clinicflow/discussions)

</div>

---

## 📋 Table of Contents

- [About the Project](#-about-the-project)
- [Key Features](#-key-features)
- [Architecture](#-architecture)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Environment Variables](#-environment-variables)
- [Database](#-database)
- [API Documentation](#-api-documentation)
- [Testing](#-testing)
- [Deployment](#-deployment)
- [Security & Compliance](#-security--compliance)
- [Monitoring & Observability](#-monitoring--observability)
- [Contributing](#-contributing)
- [Changelog](#-changelog)
- [Team](#-team)
- [License](#-license)

---

## 🏥 About the Project

**ClinicFlow** is a modern, cloud-native SaaS platform built to eliminate the administrative burden that prevents medical professionals from focusing on what truly matters — patient care.

### 🩺 The Problem

Small and medium medical clinics (3–20 doctors) operate with fragmented tools:

| Problem | Impact |
|---------|--------|
| Paper schedules and manual booking | 2–3 hours/day lost per doctor |
| No automated reminders | 15–20% no-show rate |
| Disconnected billing systems | 5–8% revenue lost to errors |
| Paper clinical records | GDPR non-compliance risk |
| No patient self-service | High reception workload |

### ✅ The Solution

ClinicFlow provides an integrated platform that manages the **complete patient journey**:

```
Patient books online → Receptionist confirms → Doctor consults →
Clinical notes recorded → Prescription issued → Invoice generated →
Patient reminded → Follow-up scheduled
```

### 📊 Business Impact

- ✅ **No-show rate reduced from 15% → 9%** (saving ~€7,000/month per clinic)
- ✅ **Admin time reduced by 60%** per doctor per day
- ✅ **100% GDPR compliant** for health data (Article 9)
- ✅ **Setup in under 1 day** — no IT team required

---

## ✨ Key Features

### 👥 For Receptionists
- 📅 **Real-time agenda** — check-ins appear instantly (Server-Sent Events)
- 🔍 **Fuzzy patient search** — find patients by name, NIF, or process number in < 200ms
- 📞 **Automated reminders** — email (48h) and SMS (24h) before appointments
- 🚶 **One-click check-in** — instant doctor notification
- 💳 **Integrated billing** — invoices generated automatically after consultation

### 🩺 For Doctors
- 📝 **Structured SOAP notes** — with auto-save every 30 seconds
- 🏥 **ICD-10 coding** — searchable in Portuguese and English
- 💊 **Electronic prescriptions** — with allergy and drug interaction checking
- 📎 **File attachments** — lab results, X-rays, PDFs
- 📜 **Immutable records** — finalised records are locked (GDPR compliant)

### 🏢 For Clinic Administrators
- 📈 **Real-time dashboard** — occupancy rate, revenue, no-shows
- 👤 **Staff management** — role-based access control
- ⚙️ **Full customisation** — appointment types, working hours, templates
- 📊 **Monthly reports** — downloadable PDF and Excel

### 📱 For Patients
- 🌐 **Self-service booking portal** — no account required
- 📧 **Appointment confirmations** — email and SMS
- 📋 **Access to own records** — GDPR right of access
- 🔔 **Smart reminders** — never miss an appointment

---

## 🏗️ Architecture

ClinicFlow is built as a **Modular Monolith** following **Clean Architecture** and **Domain-Driven Design** principles, deployed on **Kubernetes** in **AWS eu-west-1** (Dublin) for GDPR data residency compliance.

```
┌─────────────────────────────────────────────────────────────────┐
│                         Internet                                │
└───────────────────────────┬─────────────────────────────────────┘
                            │ HTTPS
                    ┌───────▼────────┐
                    │  CloudFront    │  CDN + WAF
                    │  + Route 53    │
                    └───────┬────────┘
                            │
                    ┌───────▼────────┐
                    │  AWS ALB       │  Load Balancer
                    └───────┬────────┘
                            │
              ┌─────────────┼─────────────┐
              │             │             │
    ┌─────────▼──┐  ┌───────▼────┐  ┌────▼───────────┐
    │  Frontend  │  │  Backend   │  │ Patient Portal │
    │  (Nginx +  │  │ (Spring    │  │ (Nginx +       │
    │   React)   │  │  Boot 21)  │  │  React)        │
    └────────────┘  └───────┬────┘  └────────────────┘
                            │
        ┌───────────────────┼────────────────────┐
        │                   │                    │
┌───────▼──────┐  ┌─────────▼──────┐  ┌──────────▼─────┐
│  PostgreSQL  │  │   MongoDB      │  │    Redis        │
│  (RDS        │  │   (Clinical    │  │  (ElastiCache   │
│   Multi-AZ)  │  │    Records)    │  │   Sessions +    │
│  + Replica   │  │                │  │   Cache)        │
└──────────────┘  └────────────────┘  └─────────────────┘
        │
┌───────▼──────┐
│    Kafka     │  Event Streaming
│   (MSK       │
│   KRaft)     │
└──────────────┘
```

### 🧩 Bounded Contexts (DDD)

| Module | Responsibility | Storage |
|--------|---------------|---------|
| `identity` | Authentication, 2FA, RBAC | PostgreSQL |
| `patient` | Patient profiles, allergies | PostgreSQL |
| `scheduling` | Appointments, slots, waitlist | PostgreSQL |
| `clinical` | SOAP notes, prescriptions, ICD-10 | MongoDB |
| `billing` | Invoices, payments | PostgreSQL |
| `notification` | Email, SMS, reminders | PostgreSQL + Redis |

### 📐 Clean Architecture per Module

```
module/
├── domain/          ← Pure Java, zero dependencies
│   ├── model/       ← Aggregates, Entities
│   ├── valueobject/ ← Immutable value types
│   ├── event/       ← Domain events
│   ├── exception/   ← Domain exceptions
│   ├── service/     ← Domain services
│   └── repository/  ← Repository interfaces (ports)
│
├── application/     ← Use Cases, Ports
│   ├── port/in/     ← Use case interfaces
│   ├── port/out/    ← Infrastructure interfaces
│   ├── service/     ← Use case implementations
│   └── dto/         ← Commands and results
│
└── adapter/         ← Framework code
    ├── in/web/      ← REST controllers
    ├── out/persistence/ ← JPA/MongoDB adapters
    ├── out/cache/   ← Redis adapters
    └── out/messaging/   ← Kafka adapters
```

### 📋 Architecture Decision Records

All major architectural decisions are documented in [`docs/adr/`](docs/adr/):

| ADR | Decision | Status |
|-----|----------|--------|
| [ADR-001](docs/adr/ADR-001-modular-monolith.md) | Modular Monolith for v1 | ✅ Accepted |
| [ADR-002](docs/adr/ADR-002-postgresql-primary-database.md) | PostgreSQL as primary database | ✅ Accepted |
| [ADR-003](docs/adr/ADR-003-mongodb-clinical-records.md) | MongoDB for clinical records | ✅ Accepted |
| [ADR-004](docs/adr/ADR-004-kafka-event-streaming.md) | Kafka for event streaming | ✅ Accepted |
| [ADR-005](docs/adr/ADR-005-jwt-rs256-authentication.md) | JWT RS256 authentication | ✅ Accepted |
| [ADR-006](docs/adr/ADR-006-clean-architecture.md) | Clean Architecture + DDD | ✅ Accepted |

---

## 🛠️ Tech Stack

### Backend
| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Runtime (Virtual Threads) |
| Spring Boot | 3.3 | Application framework |
| Spring Security | 6.3 | Authentication & authorisation |
| Spring Data JPA | 3.3 | PostgreSQL ORM |
| Spring Data MongoDB | 4.3 | MongoDB ODM |
| Spring Data Redis | 3.3 | Cache & sessions |
| Spring Kafka | 3.2 | Event streaming |
| Flyway | 10.x | Database migrations |
| Resilience4j | 2.2 | Circuit breaker, retry, bulkhead |
| Micrometer | 1.13 | Metrics & tracing |
| OpenTelemetry | 1.38 | Distributed tracing |
| MapStruct | 1.5 | Object mapping |
| Lombok | 1.18 | Boilerplate reduction |
| SpringDoc OpenAPI | 2.3 | API documentation |

### Frontend
| Technology | Version | Purpose |
|-----------|---------|---------|
| React | 18.3 | UI framework |
| TypeScript | 5.4 | Type safety |
| Vite | 5.3 | Build tool |
| Tailwind CSS | 4.0 | Styling |
| TanStack Query | 5.40 | Server state management |
| Zustand | 4.5 | Client state management |
| React Hook Form | 7.51 | Form management |
| Zod | 3.23 | Schema validation |
| Axios | 1.7 | HTTP client |
| React Router | 6.23 | Client-side routing |
| Recharts | 2.12 | Data visualisation |
| Lucide React | 0.395 | Icon library |
| Radix UI | 2.x | Headless UI primitives |

### Infrastructure
| Technology | Version | Purpose |
|-----------|---------|---------|
| Docker | 26.x | Containerisation |
| Kubernetes | 1.29 | Container orchestration |
| AWS EKS | 1.29 | Managed Kubernetes |
| AWS RDS | PostgreSQL 16 | Managed database |
| AWS ElastiCache | Redis 7.2 | Managed cache |
| AWS MSK | Kafka 3.7 | Managed event streaming |
| AWS S3 | — | File storage |
| AWS CloudFront | — | CDN |
| Terraform | 1.8 | Infrastructure as Code |
| Nginx | 1.25 | Reverse proxy & static serving |
| GitHub Actions | — | CI/CD pipeline |

### Observability
| Technology | Purpose |
|-----------|---------|
| Prometheus | Metrics collection |
| Grafana | Dashboards & alerting |
| Grafana Loki | Log aggregation |
| Grafana Tempo | Distributed tracing |
| OpenTelemetry Collector | Telemetry pipeline |
| SonarCloud | Code quality |

---

## 📁 Project Structure

```
clinicflow/
├── apps/
│   ├── backend/          ← Spring Boot application
│   ├── frontend/         ← Clinic staff interface (React)
│   └── patient-portal/   ← Patient self-service (React)
├── infrastructure/
│   ├── terraform/        ← AWS infrastructure as code
│   ├── kubernetes/       ← K8s manifests (Kustomize)
│   └── docker/           ← Docker init scripts
├── docs/
│   ├── adr/              ← Architecture Decision Records
│   ├── api/              ← OpenAPI specification
│   └── runbooks/         ← Operational runbooks
├── scripts/              ← Developer utilities
├── .github/              ← CI/CD workflows, templates
├── docker-compose.yml    ← Local development stack
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

| Tool | Version | Installation |
|------|---------|-------------|
| Java | 21+ | [adoptium.net](https://adoptium.net) |
| Maven | 3.9+ | [maven.apache.org](https://maven.apache.org) |
| Node.js | 20+ | [nodejs.org](https://nodejs.org) |
| Docker | 26+ | [docker.com](https://docker.com) |
| Docker Compose | 2.x | Included with Docker Desktop |

### ⚡ Quick Start (Docker — recommended)

```bash
# 1. Clone the repository
git clone https://github.com/clinicflow/clinicflow.git
cd clinicflow

# 2. Run the setup script (generates keys, installs deps)
chmod +x scripts/setup-dev.sh
./scripts/setup-dev.sh

# 3. Start the full stack
docker compose up --build

# 4. Open the application
open http://localhost:80          # Clinic staff interface
open http://localhost:8081        # Patient portal
open http://localhost:8080/swagger-ui.html  # API docs
```

### 🔧 Manual Setup (without Docker)

**1. Start infrastructure services:**
```bash
# Start only the databases and messaging
docker compose up -d postgres mongodb redis kafka
```

**2. Start the backend:**
```bash
cd apps/backend

# Run database migrations
mvn flyway:migrate \
  -DDB_URL=jdbc:postgresql://localhost:5432/clinicflow \
  -DDB_USERNAME=clinicflow \
  -DDB_PASSWORD=localdev

# Start the application
mvn spring-boot:run \
  -Dspring-boot.run.profiles=local
```

**3. Start the frontend:**
```bash
cd apps/frontend
npm install
npm run dev
# Available at http://localhost:5173
```

### 🛠️ Dev Tools (optional)

```bash
# Start with dev tools (Kafka UI, pgAdmin, Mongo Express)
docker compose --profile dev-tools up -d

# Access:
# Kafka UI:       http://localhost:9000
# pgAdmin:        http://localhost:8083  (dev@clinicflow.io / localdev)
# Mongo Express:  http://localhost:8082  (admin / localdev)
```

### 🧪 Default Credentials (local only)

| Role | Email | Password |
|------|-------|----------|
| Admin | `admin@demo-clinic.com` | `Admin@123456` |
| Doctor | `dr.silva@demo-clinic.com` | `Doctor@123456` |
| Receptionist | `recepcao@demo-clinic.com` | `Recep@123456` |
| Patient | `patient@example.com` | `Patient@123456` |

> ⚠️ These credentials only work in local/staging environments.
> Production accounts are provisioned during onboarding.

---

## 🔐 Environment Variables

### Backend (`apps/backend`)

```bash
# ── Application ────────────────────────────────────────
APP_ENVIRONMENT=local                    # local | staging | production
APP_VERSION=1.0.0

# ── PostgreSQL ─────────────────────────────────────────
DB_URL=jdbc:postgresql://localhost:5432/clinicflow
DB_USERNAME=clinicflow
DB_PASSWORD=your-secure-password

# ── MongoDB ────────────────────────────────────────────
MONGODB_URI=mongodb://localhost:27017/clinicflow

# ── Redis ──────────────────────────────────────────────
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=your-redis-password

# ── Kafka ──────────────────────────────────────────────
KAFKA_BOOTSTRAP_SERVERS=localhost:9092

# ── JWT ────────────────────────────────────────────────
JWT_PRIVATE_KEY_PATH=/app/keys/private.pem
JWT_PUBLIC_KEY_PATH=/app/keys/public.pem

# ── External Services ──────────────────────────────────
SENDGRID_API_KEY=SG.xxxx              # Email delivery
TWILIO_ACCOUNT_SID=ACxxxx            # SMS delivery
TWILIO_AUTH_TOKEN=xxxx
TWILIO_FROM_NUMBER=+351xxxxxxxxx

# ── AWS (production) ───────────────────────────────────
AWS_REGION=eu-west-1
AWS_S3_BUCKET=clinicflow-prod-documents
```

### Frontend (`apps/frontend`)

```bash
# Vite build-time variables (prefix: VITE_)
VITE_APP_NAME=ClinicFlow
VITE_APP_VERSION=1.0.0
VITE_API_BASE_URL=/api        # Nginx proxy in Docker
                               # http://localhost:8080 in dev
```

> 🔒 **Never commit `.env` files.**
> See `.env.example` in each app directory for the full list.
> In production, secrets are managed by **AWS Secrets Manager**.

---

## 🗃️ Database

### Schema Overview

```
PostgreSQL (clinicflow)
├── identity schema
│   ├── users
│   ├── refresh_tokens
│   └── totp_setups
├── patient schema
│   ├── patients
│   └── allergies
├── scheduling schema
│   ├── appointments
│   ├── schedules
│   └── waitlist_entries
├── billing schema
│   ├── invoices
│   ├── invoice_items
│   └── payments
├── notification schema
│   ├── notifications
│   └── notification_templates
└── shared schema
    └── outbox_events

MongoDB (clinicflow)
├── clinical_records
├── audit_logs
└── clinic_configurations
```

### Running Migrations

```bash
# Run all pending migrations
cd apps/backend
mvn flyway:migrate

# Check migration status
mvn flyway:info

# Repair failed migration (if needed)
mvn flyway:repair
```

### Migration Conventions

```
✅ All migrations: db/migration/V{N}__{description}.sql
✅ Index creation: always CONCURRENTLY (no table locks)
✅ Large table changes: Expand-Contract pattern
✅ Each migration: tested against 1M+ row dataset in staging
✅ Rollback procedure: documented in migration file header
```

---

## 📖 API Documentation

### Interactive Documentation

When the application is running:

```
Swagger UI:  http://localhost:8080/swagger-ui.html
OpenAPI JSON: http://localhost:8080/api-docs
OpenAPI YAML: http://localhost:8080/api-docs.yaml
```

### Base URL

```
Local:      http://localhost:8080/api/v1
Staging:    https://staging-api.clinicflow.io/api/v1
Production: https://api.clinicflow.io/api/v1
```

### Authentication

All endpoints require a Bearer token (except `/auth/*`):

```bash
# Login
curl -X POST https://api.clinicflow.io/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@clinic.com","password":"password"}'

# Use the access token
curl https://api.clinicflow.io/api/v1/patients \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiJ9..."
```

### Quick Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/auth/login` | Authenticate user |
| `POST` | `/auth/2fa/verify` | Complete 2FA |
| `POST` | `/auth/refresh` | Refresh access token |
| `GET` | `/patients` | List/search patients |
| `POST` | `/patients` | Register patient |
| `GET` | `/patients/{id}` | Get patient details |
| `GET` | `/appointments/slots` | Get available slots |
| `POST` | `/appointments` | Book appointment |
| `POST` | `/appointments/{id}/check-in` | Check in patient |
| `GET` | `/agenda/daily` | Get doctor's daily agenda |
| `GET` | `/agenda/stream` | Real-time agenda (SSE) |
| `GET` | `/clinical-records/{appointmentId}` | Get clinical record |
| `POST` | `/clinical-records` | Create clinical record |
| `POST` | `/clinical-records/{id}/finalise` | Finalise record |
| `POST` | `/prescriptions` | Issue prescription |
| `POST` | `/invoices` | Generate invoice |
| `POST` | `/invoices/{id}/payments` | Record payment |

Full API documentation: [`docs/api/openapi.yml`](docs/api/openapi.yml)

---

## 🧪 Testing

### Running Tests

```bash
cd apps/backend

# All tests
mvn test

# Unit tests only (fast — no Docker required)
mvn test -Dgroups="unit"

# Integration tests (requires Docker)
mvn test -Dgroups="integration"

# Architecture tests (ArchUnit)
mvn test -Dtest="*ArchitectureTest"

# With coverage report
mvn verify
open target/site/jacoco/index.html
```

### Coverage Requirements

| Layer | Minimum Coverage |
|-------|-----------------|
| `domain` | 80% line, 70% branch |
| `application` | 80% line, 70% branch |
| `adapter` | 60% line |

### Test Pyramid

```
        ▲
       /E2E\           10%  — Full booking flow
      /──────\
     /  Integ  \       20%  — Testcontainers (real DBs)
    /────────────\
   /  Unit Tests  \    70%  — Domain + Application layer
  /────────────────\
```

### Architecture Tests

ArchUnit enforces architectural rules on every build:

```
✅ Domain layer has zero Spring/JPA imports
✅ Application layer has zero JPA imports
✅ Adapters depend on Application, never on other Adapters
✅ No cross-module internal access (only public APIs)
✅ All index migrations use CONCURRENTLY
```

---

## 🚢 Deployment

### CI/CD Pipeline

Every push triggers the pipeline:

```
push → Validate → Test → Security Scan → Build Docker → Deploy Staging → E2E Tests → [manual] → Deploy Production
```

| Stage | Duration | Blocks merge |
|-------|----------|-------------|
| Compile + Checkstyle | ~1 min | ✅ |
| Unit Tests + Coverage | ~3 min | ✅ |
| Integration Tests | ~5 min | ✅ |
| OWASP Dependency Check | ~3 min | ✅ |
| SonarCloud Quality Gate | ~2 min | ✅ |
| Docker Build + Trivy Scan | ~3 min | ✅ |
| Deploy Staging (auto) | ~5 min | — |
| E2E Tests on Staging | ~5 min | — |
| Deploy Production (manual) | ~10 min | — |

### Deployment Strategy

- **Staging:** Rolling update on every merge to `main`
- **Production:** Blue-Green deployment on every Git tag (`v*.*.*`)
  - Zero downtime
  - Instant rollback (switch back to old slot)
  - Smoke tests before traffic switch

### Creating a Production Release

```bash
# 1. Create and push a version tag
git tag v1.2.0 -m "feat: add patient portal booking"
git push origin v1.2.0

# 2. CI builds and pushes the Docker image
# 3. Pipeline pauses for manual approval
# 4. Approve in GitHub Actions
# 5. Blue-Green deployment executes automatically
```

### Infrastructure (Terraform)

```bash
cd infrastructure/terraform/environments/staging

# Preview changes
terraform plan

# Apply changes (requires approval in CI)
terraform apply
```

---

## 🔒 Security & Compliance

### GDPR Compliance

ClinicFlow handles **Special Category Data** (health data) under GDPR Article 9.

| Requirement | Implementation |
|------------|---------------|
| Data residency | AWS eu-west-1 (Dublin, Ireland) only |
| Encryption at rest | AES-256 via AWS KMS |
| Encryption in transit | TLS 1.2+ enforced |
| Access logging | Every access to clinical data is logged immutably |
| Right of access | Patient data export within 72 hours |
| Data minimisation | Role-based field visibility |
| Retention | Clinical records: 10 years minimum |
| Audit log retention | 3 years (TTL enforced in MongoDB) |

### Security Features

- 🔐 **JWT RS256** — asymmetric signing, 15-minute access tokens
- 📱 **TOTP 2FA** — mandatory for medical staff
- 🛡️ **Brute force protection** — 5 attempts → 15-minute lockout
- 🔑 **Refresh token rotation** — detects token theft
- 🏷️ **RBAC** — role-based access control per clinic
- 🔒 **ABAC** — attribute-based for clinical data access
- 🚦 **Rate limiting** — per user and per IP (Redis sliding window)
- 🧪 **OWASP scanning** — on every build (CVSS ≥ 7 blocks release)
- 🔍 **Secret scanning** — Gitleaks on every push
- 🐋 **Container scanning** — Trivy on every Docker build

### Reporting a Vulnerability

Please **do not** create a public GitHub issue for security vulnerabilities.

Email: [security@clinicflow.io](mailto:security@clinicflow.io)

We follow responsible disclosure and will respond within 48 hours.

---

## 📊 Monitoring & Observability

### The Three Pillars

| Pillar | Tool | Access |
|--------|------|--------|
| Metrics | Prometheus + Grafana | Internal only |
| Logs | Grafana Loki | Internal only |
| Traces | Grafana Tempo | Internal only |

### Key Dashboards

```
Grafana (internal):
├── 🏥 ClinicFlow — Business Metrics
│   ├── Appointments today (total, completed, no-shows)
│   ├── No-show rate trend (30 days)
│   ├── Revenue today and this month
│   └── Active clinics and users
│
├── 🔧 ClinicFlow — Service Health
│   ├── P50/P95/P99 latency by endpoint
│   ├── Error rate by endpoint
│   ├── JVM memory and GC
│   └── Thread pool utilisation
│
├── 🗃️ ClinicFlow — Database
│   ├── Query latency (p95)
│   ├── Connection pool utilisation
│   ├── Slow queries (> 100ms)
│   ├── Replication lag
│   └── Cache hit rate (L1/L2)
│
└── 🔄 ClinicFlow — Kafka
    ├── Consumer lag per group
    ├── Messages per second
    └── Failed messages (DLQ)
```

### SLOs (Service Level Objectives)

| Service | SLO | Measurement |
|---------|-----|-------------|
| Appointment booking | 99.9% availability | 8h–20h business days |
| Patient search | p95 < 200ms | All requests |
| Booking API | p95 < 500ms | All requests |
| Patient portal | 99.5% availability | 24/7 |

### Health Checks

```bash
# Liveness (is the process running?)
GET /actuator/health/liveness

# Readiness (is the service ready for traffic?)
GET /actuator/health/readiness

# Full health (internal use only)
GET /actuator/health
```

---

## 🤝 Contributing

We welcome contributions from the team! Please read the full guide first.

### Quick Contribution Guide

```bash
# 1. Create a branch from main
git checkout main && git pull
git checkout -b feature/CLI-234-appointment-checkin

# 2. Make your changes following our conventions
# See CONTRIBUTING.md for full guidelines

# 3. Ensure everything passes
cd apps/backend && mvn verify
cd apps/frontend && npm run type-check && npm test

# 4. Commit using Conventional Commits
git commit -m "feat(scheduling): add patient check-in endpoint"

# 5. Open a Pull Request
# Use the PR template — fill in all sections
```

### Commit Convention

We use [Conventional Commits](https://www.conventionalcommits.org/):

```
feat(module): short description        → bumps MINOR version
fix(module): short description         → bumps PATCH version
feat(module)!: breaking change         → bumps MAJOR version
docs: update README
test(module): add missing tests
refactor(module): extract service
chore: update dependencies
```

**Valid scopes:** `scheduling`, `patients`, `clinical`, `prescriptions`,
`billing`, `notifications`, `auth`, `api`, `db`, `config`, `ci`, `docs`, `deps`, `security`

### Code Review Process

1. **Self-review** — read your own diff before requesting review
2. **Automated checks** — all CI checks must be green
3. **Peer review** — minimum 1 approval required
   - Critical paths (clinical, billing, auth): 2 approvals
4. **CODEOWNERS** — auto-assigned reviewers per area

### Definition of Done

- [ ] Acceptance criteria from the ticket met
- [ ] Unit tests written (coverage maintained)
- [ ] Integration tests for new endpoints
- [ ] ArchUnit tests pass
- [ ] No new SonarCloud issues (Blocker/Critical)
- [ ] No new OWASP vulnerabilities (CVSS ≥ 7)
- [ ] OpenAPI spec updated (if endpoint added/changed)
- [ ] ADR written (if architectural decision made)
- [ ] PR template fully completed
- [ ] Reviewed and approved

---

## 📝 Changelog

See [CHANGELOG.md](CHANGELOG.md) for the full history.

### Latest Release — v1.0.0 (2024-07-15)

#### ✨ Features
- 🔐 JWT authentication with TOTP 2FA
- 👥 Patient registration and fuzzy search
- 📅 Real-time appointment scheduling with SSE
- 🚶 Patient check-in with instant doctor notification
- 📝 SOAP clinical notes with ICD-10 coding
- 💊 Electronic prescriptions with allergy checking
- 💳 Invoice generation and payment recording
- 📧 Automated email and SMS reminders
- 📊 Clinic dashboard with real-time metrics
- 🌐 Patient self-service booking portal

#### 🏗️ Infrastructure
- ☸️ Kubernetes deployment on AWS EKS
- 🔵🟢 Blue-Green deployment with zero downtime
- 📈 Full observability (Prometheus, Loki, Tempo)
- 🔒 GDPR-compliant data handling

---

## 👥 Team

| Name | Role | GitHub |
|------|------|--------|
| Tech Lead | Architecture & Backend | [@tech-lead](https://github.com/tech-lead) |
| Senior Backend | Backend Development | [@senior-backend](https://github.com/senior-backend) |
| Frontend Lead | Frontend Development | [@frontend-lead](https://github.com/frontend-lead) |
| DevOps Engineer | Infrastructure & CI/CD | [@devops](https://github.com/devops) |
| QA Engineer | Quality Assurance | [@qa-engineer](https://github.com/qa-engineer) |
| DPO | GDPR & Compliance | [@dpo](https://github.com/dpo) |

---

## 📞 Support

| Channel | Purpose | Response Time |
|---------|---------|--------------|
| [GitHub Issues](https://github.com/clinicflow/clinicflow/issues) | Bug reports | 48h |
| [GitHub Discussions](https://github.com/clinicflow/clinicflow/discussions) | Feature requests | 72h |
| [Slack #engineering](slack://channel) | Internal team | Same day |
| [security@clinicflow.io](mailto:security@clinicflow.io) | Security vulnerabilities | 48h |
| [support@clinicflow.io](mailto:support@clinicflow.io) | Customer support | 24h (SLA) |

---

## 📄 License

Copyright © 2024 ClinicFlow Lda. All rights reserved.

This software is proprietary and confidential.
Unauthorised copying, modification, or distribution is strictly prohibited.

See [LICENSE](LICENSE) for full terms.

---

<div align="center">

Built with ❤️ for healthcare professionals across Europe

[🌐 clinicflow.io](https://clinicflow.io) · [📖 docs.clinicflow.io](https://docs.clinicflow.io) · [📧 hello@clinicflow.io](mailto:hello@clinicflow.io)

</div>
```


