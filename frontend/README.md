# TransitOps Frontend

Simple React + Vite frontend for the TransitOps Spring Boot backend.

## Run

```bash
npm install
npm run dev
```

Open:

```text
http://localhost:5173
```

The dev server proxies `/api` requests to:

```text
http://localhost:8080
```

## Backend

Start the Spring Boot backend first:

```bash
../mvnw.cmd spring-boot:run
```

Register an admin user from the frontend, then login with the same email/password.
