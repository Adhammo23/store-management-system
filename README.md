# Store Management System (Spring Boot)

A simple **Store Management System** REST API built with Spring Boot. It provides:

- JWT-based authentication (register / login)
- Category management
- Product management
- Pagination + sorting for product listings

> This repository contains a backend API only (no frontend).

---

## Table of contents

- [Features](#features)
- [Tech stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick start (recommended)](#quick-start-recommended)
- [Configuration](#configuration)
- [Authentication (JWT)](#authentication-jwt)
- [API endpoints](#api-endpoints)
- [Error response format](#error-response-format)
- [Notes / limitations](#notes--limitations)

---

## Features

### Auth

- Register a new user
- Login
- Receive a JWT token to access protected endpoints

### Categories

- Create category
- List all categories
- Get category by id
- List products by category (paged)

### Products

- Create product
- List products (paged + sorting)
- Get product by id
- Update product (PATCH)
- Delete product

---

## Tech stack

- Java 17
- Spring Boot 4.0.6
- Spring WebMVC
- Spring Data JPA
- Spring Security (stateless)
- JWT (jjwt 0.12.3)
- MySQL 8
- Lombok
- Springdoc OpenAPI UI (Swagger) 3.0.2

---

## Prerequisites

- JDK 17
- Maven (or use the included Maven Wrapper)
- (Recommended) Docker + Docker Compose to run MySQL

---

## Quick start (recommended)

### 1) Start MySQL using Docker Compose

The project includes a `docker-compose.yml` that runs MySQL 8 on **port 3307**:

```bash
docker compose up -d
```

Database values (from `docker-compose.yml` and `application.properties`):

- Host: `localhost`
- Port: `3307`
- Database: `store_db`
- Username: `adham`
- Password: `adham123`

### 2) Run the application

Windows (PowerShell):

```powershell
cd D:\Adham\spring-project\store-management-system
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Default base URL:

- `http://localhost:8080`

---

## Configuration

Main configuration is in `src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:mysql://localhost:3307/store_db`
- `spring.datasource.username=adham`
- `spring.datasource.password=adham123`
- `spring.jpa.hibernate.ddl-auto=update`
- `jwt.secret=...`
- `jwt.expiration=3600000` (milliseconds = 1 hour)

You can override values using environment variables. Example (PowerShell):

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:mysql://localhost:3307/store_db"
$env:SPRING_DATASOURCE_USERNAME="adham"
$env:SPRING_DATASOURCE_PASSWORD="adham123"
.\mvnw.cmd spring-boot:run
```

Important note:

- In `JwtService`, `jwt.secret` is decoded as **Base64**.

---

## Authentication (JWT)

All endpoints are protected and require a JWT token **except**:

- `POST /api/auth/register`
- `POST /api/auth/login`

After register/login, you receive:

```json
{ "token": "<JWT_TOKEN>" }
```

Send it in requests using:

```http
Authorization: Bearer <JWT_TOKEN>
```

### cURL examples

Register:

```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{"name":"Adham","email":"adham@example.com","password":"123456"}'
```

Login:

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"adham@example.com","password":"123456"}'
```

Call a protected endpoint (example: list products):

```bash
curl "http://localhost:8080/products?page=0&size=20&sortBy=id" \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

---

## API endpoints

Base URL: `http://localhost:8080`

### Auth (`/api/auth`)

| Method | Path | Body | Auth |
|---|---|---|---|
| POST | `/api/auth/register` | `{ name, email, password }` | Public |
| POST | `/api/auth/login` | `{ email, password }` | Public |

Request body examples:

`POST /api/auth/register`

```json
{ "name": "Adham", "email": "adham@example.com", "password": "123456" }
```

`POST /api/auth/login`

```json
{ "email": "adham@example.com", "password": "123456" }
```

### Categories (`/categories`)

| Method | Path | Query Params | Body | Auth |
|---|---|---|---|---|
| GET | `/categories` | - | - | JWT |
| GET | `/categories/{categoryId}` | - | - | JWT |
| POST | `/categories` | - | `{ name, description }` | JWT |
| GET | `/categories/{categoryId}/products` | `page,size,sortBy` | - | JWT |

`POST /categories` example:

```json
{ "name": "Drinks", "description": "All drinks" }
```

### Products (`/products`)

| Method | Path | Query Params | Body | Auth |
|---|---|---|---|---|
| GET | `/products` | `page,size,sortBy` | - | JWT |
| GET | `/products/{productID}` | - | - | JWT |
| POST | `/products` | - | `{ name, price, categoryId }` | JWT |
| PATCH | `/products/{productId}` | - | `{ name, price, categoryId }` | JWT |
| DELETE | `/products/{productId}` | - | - | JWT |

`POST /products` example:

```json
{ "name": "Pepsi", "price": 10.5, "categoryId": 1 }
```

Pagination note:

- `GET /products` and `GET /categories/{id}/products` return `Page<...>` (fields like `content`, `totalElements`, `totalPages`, etc.).

---

## Error response format

For errors (e.g., 404 or validation errors), the API returns:

```json
{
  "status": 404,
  "message": "Product not found",
  "timestamp": 1710000000000
}
```

---

## Notes / limitations

- A Swagger/OpenAPI dependency is included, but Swagger endpoints are **not excluded** from security in `SecurityConfig`.
  - You may get `401 Unauthorized` when opening Swagger UI unless you adjust security configuration.
  - This README does not change any code.
- `spring.jpa.hibernate.ddl-auto=update` is convenient for development, but not recommended for production.
- Roles `USER` and `ADMIN` exist, but endpoint-level role restrictions are not enforced in the current code.

