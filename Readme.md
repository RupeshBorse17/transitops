# TransitOps

Smart Transport Operations Platform for vehicle, driver, trip, maintenance, fuel, expense, dashboard, and analytics workflows.

## Tech Stack

- Java 17
- Spring Boot 3.5.x
- Maven
- Spring Data JPA
- MySQL
- Spring Security
- Lombok
- Springdoc OpenAPI

## Setup

1. Create or allow the app to create the MySQL database `transitops`.
2. Update `src/main/resources/application.properties` with your MySQL username and password.
3. Start the backend:

```bash
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Authentication

Register a user with `/api/auth/register`, then call protected APIs using HTTP Basic authentication with the registered email and password.

Roles supported by the existing enum:

- `ADMIN`
- `FLEET_MANAGER`
- `DRIVER`
- `SAFETY_OFFICER`
- `FINANCIAL_ANALYST`

## Main APIs

- `POST /api/auth/register`
- `POST /api/auth/login`
- `CRUD /api/vehicles`
- `CRUD /api/drivers`
- `CRUD /api/trips`
- `PUT /api/trips/dispatch/{id}`
- `PUT /api/trips/complete/{id}`
- `PUT /api/trips/cancel/{id}`
- `CRUD /api/maintenance`
- `PUT /api/maintenance/approve/{id}`
- `PUT /api/maintenance/reject/{id}`
- `PUT /api/maintenance/resolve/{id}`
- `CRUD /api/fuel`
- `CRUD /api/expenses`
- `GET /api/dashboard`
- `GET /api/analytics`

## Business Rules Implemented

- Vehicle registration number is unique.
- Retired, in-shop, and on-trip vehicles cannot be dispatched.
- Suspended, unavailable, on-trip, or expired-license drivers cannot be dispatched.
- Cargo weight cannot exceed vehicle capacity.
- Dispatching a trip changes vehicle and driver status to `ON_TRIP`.
- Completing a dispatched trip restores vehicle and driver status to `AVAILABLE`.
- Cancelling a non-completed trip restores vehicle and driver status to `AVAILABLE`.
- Approving maintenance changes vehicle status to `IN_SHOP`.
- Resolving maintenance restores vehicle status to `AVAILABLE` unless retired.

## Architecture

The project follows the existing package structure:

- `controller`: REST API layer
- `dto`: Request and response DTOs with validation
- `entity`: JPA entities
- `enums`: Domain status and role enums
- `exception`: Global JSON error handling
- `repository`: Spring Data JPA repositories and JPQL aggregates
- `service`: Service contracts
- `service.impl`: Business logic and transactional workflows
- `config`: Security and OpenAPI configuration

## Database

Core entities:

- `users`
- `roles`
- `vehicles`
- `drivers`
- `trips`
- `maintenance_logs`
- `fuel_logs`
- `expenses`

Useful indexes and unique constraints are applied on registration numbers, license numbers, statuses, dates, and foreign keys.

