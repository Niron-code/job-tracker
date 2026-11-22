# Job Tracker

A job application tracker built to practise a production-style Java stack: Spring Boot, microservices, Spring Cloud, Spring Security, React and Azure. Each stage leaves something that runs and covers a topic that comes up in interviews.

> **Status:** Stage 1, Step 1 (project setup)

---

## Goals

- Track job applications from Applied → Interview → Offer/Rejected
- Store job descriptions and note which CV version was sent with each application
- Practise each layer of the stack in order, without skipping the fundamentals
- Ship it: Dockerised, with CI/CD, deployed to Azure

## Scope rules

- One stage at a time. Don't start a stage until the previous one runs.
- No payments, no notifications, no extra features until Stage 6 is done.
- AI features (Spring AI) come last, as an optional extra.

---

## Architecture (target)

```
                 ┌──────────────┐
  React (TS) ──► │ API Gateway  │ ◄── JWT auth (Spring Security)
                 └──────┬───────┘
                        │  service discovery (Eureka)
          ┌─────────────┴──────────────┐
          ▼                            ▼
┌──────────────────────┐    ┌──────────────────────────┐
│ application-service  │◄──►│ job-description-service  │
│ applications, status │    │ pasted JDs, parsed info, │
│ dates, notes, CV ver │    │ CV versions              │
└──────────┬───────────┘    └────────────┬─────────────┘
           ▼                             ▼
      PostgreSQL                    PostgreSQL
              Config Server (shared config)
```

## MVC architecture (inside each service)

Each backend service follows Spring MVC with a layered design. The **View** is the JSON response. In Stage 5, the React app becomes the visual view.

```
HTTP request
     │
     ▼
┌────────────┐   DTO (record)   ┌───────────┐   Entity   ┌──────────────┐
│ Controller │ ───────────────► │  Service  │ ─────────► │  Repository  │ ──► Database
│  (C)       │ ◄─────────────── │ (business │ ◄───────── │ (Spring Data │
└────────────┘   DTO → JSON (V) │   logic)  │            │     JPA)     │
                                └───────────┘            └──────────────┘
                     Model (M) = Entity + DTOs
```

| MVC role | Layer | Responsibility | Spring annotations |
|---|---|---|---|
| **Model** | `model/` (entities) + `dto/` | Data and its structure. Entities map to tables, DTOs define what the API sends and receives | `@Entity`, `@Id`, Java `record` |
| **View** | JSON response (later React) | How data is presented to the client | `@RestController` serialises to JSON via Jackson |
| **Controller** | `controller/` | Takes HTTP requests, validates input, calls the service, returns a response with the right status code | `@RestController`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@Valid` |
| (supporting) | `service/` | Business rules (e.g. valid status transitions), entity ↔ DTO mapping, transactions | `@Service`, `@Transactional` |
| (supporting) | `repository/` | Database access | `JpaRepository` |
| (supporting) | `exception/` | Central error handling | `@RestControllerAdvice`, `@ExceptionHandler` |

**Rules:**
- Controllers never call repositories directly. They always go through the service.
- Controllers never return entities. They return DTOs.
- Business logic lives in the service, not in the controller or the entity.
- Depend on interfaces where it helps testing, and inject through the constructor (no field `@Autowired`).

### Package structure (`application-service`)

```
src/main/java/com/philipniron/applicationservice/
├── ApplicationServiceApplication.java
├── controller/
│   └── JobApplicationController.java
├── service/
│   ├── JobApplicationService.java
│   └── JobApplicationServiceImpl.java
├── repository/
│   └── JobApplicationRepository.java
├── model/
│   ├── JobApplication.java          # @Entity
│   └── ApplicationStatus.java       # enum: APPLIED, INTERVIEW, OFFER, REJECTED, WITHDRAWN
├── dto/
│   ├── JobApplicationRequest.java   # record, with validation annotations
│   └── JobApplicationResponse.java  # record
├── mapper/
│   └── JobApplicationMapper.java    # entity ↔ DTO
└── exception/
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java  # @RestControllerAdvice

src/test/java/com/philipniron/applicationservice/
├── controller/   # @WebMvcTest
├── service/      # Mockito unit tests
└── repository/   # @DataJpaTest
```

### API endpoints (Stage 1)

| Method | Path | Description | Success code |
|---|---|---|---|
| `POST` | `/api/applications` | Create an application | 201 |
| `GET` | `/api/applications` | List all (optional `?status=`) | 200 |
| `GET` | `/api/applications/{id}` | Get one | 200 / 404 |
| `PUT` | `/api/applications/{id}` | Update | 200 / 404 |
| `PATCH` | `/api/applications/{id}/status` | Change status only | 200 |
| `DELETE` | `/api/applications/{id}` | Delete | 204 |

---

## Repository layout (target)

```
job-tracker/
├── application-service/
├── job-description-service/
├── api-gateway/
├── discovery-server/
├── config-server/
├── frontend/
├── docker-compose.yml
├── .github/workflows/
└── README.md
```

---

## Tech stack

| Layer | Tools |
|---|---|
| Language | Java 21 |
| Backend | Spring Boot, Spring Web, Spring Data JPA, Bean Validation |
| Database | H2 (dev, Stage 1) → PostgreSQL |
| Microservices | OpenFeign |
| Spring Cloud | Gateway, Eureka, Config Server |
| Security | Spring Security, JWT |
| Frontend | React + TypeScript |
| Testing | JUnit 5, Mockito, MockMvc |
| DevOps | Docker, Docker Compose, GitHub Actions |
| Cloud | Azure Container Apps |
| AI (later) | Spring AI 2.x (Anthropic or OpenAI-compatible, including Azure OpenAI) |

---

## Roadmap

### Stage 1: Spring Boot REST API (`application-service`)
- [ ] **Step 1: Project setup.** Generate from start.spring.io (Maven, Java 21; Web, Data JPA, Validation, H2, PostgreSQL Driver, DevTools), run it, push to GitHub
- [ ] **Step 2: Entity + repository.** `JobApplication` entity (company, role, link, status enum, appliedDate, notes, cvVersion), `JpaRepository`
- [ ] **Step 3: REST controller.** CRUD endpoints, DTOs as Java records, service layer
- [ ] **Step 4: Validation + error handling.** `@Valid`, `@ControllerAdvice`, consistent error responses, correct status codes
- [ ] **Step 5: PostgreSQL in Docker.** Switch from H2, use profiles (`dev`, `prod`)
- [ ] **Step 6: Tests.** Unit tests (Mockito) for the service, `@WebMvcTest` for the controller, `@DataJpaTest` for the repository

### Stage 2: Microservices
- [ ] Create `job-description-service` (store pasted JD, stack, seniority, closing date, CV versions)
- [ ] Service-to-service calls with OpenFeign
- [ ] Separate database per service

### Stage 3: Spring Cloud
- [ ] Eureka discovery server
- [ ] API Gateway with routing
- [ ] Config Server for shared configuration

### Stage 4: Spring Security
- [ ] Register/login, JWT issued and validated
- [ ] Auth enforced at the gateway, with roles (user/admin)

### Stage 5: React + TypeScript
- [ ] Board view with status columns
- [ ] Add/edit application form
- [ ] Login page, API calls with the token

### Stage 6: Cloud & DevOps
- [ ] Dockerfile per service, `docker-compose.yml` for running everything locally
- [ ] GitHub Actions: build + test on every push
- [ ] Deploy to Azure Container Apps

### Stage 7 (optional): AI with Spring AI
- [ ] Paste a JD and get back a structured `JobDescription` (structured output to a POJO)
- [ ] Score a JD against each CV version and recommend which CV to send

---

## Running locally (Stage 1)

```bash
cd application-service
./mvnw spring-boot:run        # macOS/Linux
mvnw.cmd spring-boot:run      # Windows
```

App runs at `http://localhost:8080`.

---

## Interview notes

Record the answer to each step's question in your own words.

| Step | Question | My answer |
|---|---|---|
| 1.1 | How did the app start a web server without any server code? | |
| 1.2 | What is the difference between `JpaRepository` and `CrudRepository`? | |
| 1.3 | Why use DTOs instead of returning entities from controllers? | |
| 1.4 | How does `@ControllerAdvice` work, and which status codes fit which errors? | |
| 1.5 | What are Spring profiles and how do they load config? | |
| 1.6 | `@WebMvcTest` vs `@SpringBootTest`: when do you use each? | |
| MVC | In a REST API, what plays the "View" role? Why add a service layer on top of MVC? | |
