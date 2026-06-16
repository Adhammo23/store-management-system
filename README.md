# 🏪 Store Management System

> A **production-ready** REST API for managing an online store with user authentication, inventory management, and order processing.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-green?style=flat-square&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-Security-purple?style=flat-square)
![Maven](https://img.shields.io/badge/Maven-Build-red?style=flat-square&logo=apache-maven)

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🛠️ Tech Stack](#️-tech-stack)
- [📋 Prerequisites](#-prerequisites)
- [🚀 Quick Start](#-quick-start)
- [⚙️ Configuration](#️-configuration)
- [🔐 Authentication & Authorization](#-authentication--authorization)
- [📡 API Endpoints](#-api-endpoints)
- [🏗️ Project Architecture](#-project-architecture)
- [🔒 Security Best Practices](#-security-best-practices)
- [📦 Database Schema](#-database-schema)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)

---

## ✨ Features

### 🔐 Authentication & User Management
- ✅ User registration with email validation
- ✅ Secure login with bcrypt password hashing
- ✅ JWT token-based authentication
- ✅ Role-based access control (USER / ADMIN)
- ✅ Automatic token expiration (1 hour default)

### 🏷️ Category Management
- ✅ Create, read, and update product categories
- ✅ List all categories with full details
- ✅ View products by category with pagination
- ✅ Nested product filtering by category

### 📦 Product Management
- ✅ Full CRUD operations on products
- ✅ Real-time inventory tracking (stock quantity)
- ✅ Dedicated restock endpoint for inventory updates
- ✅ Pagination and sorting support
- ✅ BigDecimal price precision for currency
- ✅ Unique product name constraint

### 🛒 Order Management
- ✅ Create orders with multiple items
- ✅ Automatic order total calculation
- ✅ Order-to-user relationship tracking
- ✅ Order item with price snapshot at sale time
- ✅ Inventory validation and reduction

### 🔍 Advanced Features
- ✅ Pagination with customizable page size
- ✅ Multi-column sorting support
- ✅ Global exception handling with meaningful error messages
- ✅ Request validation with Jakarta Bean Validation
- ✅ Lombok for reduced boilerplate code
- ✅ Spring Data JPA for efficient database operations
- ✅ Springdoc OpenAPI (Swagger) UI integration

---

## 🛠️ Tech Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| **Language** | Java | 17 |
| **Framework** | Spring Boot | 4.0.6 |
| **Web** | Spring MVC | Latest |
| **Security** | Spring Security | Latest |
| **Authentication** | JJWT | 0.12.3 |
| **Database** | MySQL | 8.0 |
| **ORM** | Hibernate / JPA | Latest |
| **Build Tool** | Maven | 3.9+ |
| **Code Generation** | Lombok | Latest |
| **API Docs** | Springdoc OpenAPI | 3.0.2 |
| **Containerization** | Docker & Docker Compose | Latest |

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **JDK 17** or higher ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- **Maven 3.9+** or use the included Maven Wrapper
- **Docker & Docker Compose** (optional, for MySQL container)
- **Git** (for cloning the repository)
- **Postman** or **cURL** (for API testing)

---

## 🚀 Quick Start

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/yourusername/store-management-system.git
cd store-management-system
```

### 2️⃣ Start MySQL with Docker Compose (Recommended)

The project includes a pre-configured `docker-compose.yml` for MySQL 8:

```bash
docker compose up -d
```

**Verify MySQL is running:**

```bash
docker ps | grep store-mysql
```

### 3️⃣ Run the Application

#### **Windows (PowerShell)**
```powershell
cd D:\Adham\spring-project\store-management-system
.\mvnw.cmd spring-boot:run
```

#### **Linux / macOS**
```bash
./mvnw spring-boot:run
```

#### **Alternative: Run the JAR directly**
```bash
# Build the project first
./mvnw clean package

# Run the JAR
java -jar target/store-management-system-0.0.1-SNAPSHOT.jar
```

### 4️⃣ Verify the Application

Once started, the API will be available at:

- **Base URL:** `http://localhost:8080`
- **Health Check:** `http://localhost:8080/actuator/health`
- **Request a token:** Use the `/api/auth/login` endpoint

---

## ⚙️ Configuration

### 📝 Application Properties

Main configuration file: `src/main/resources/application.properties`

```properties
# Application Name
spring.application.name=store-management-system

# Database Configuration
spring.datasource.url=${DB_URL}
spring.datasource.username=adham
spring.datasource.password=${DB_PASSWORD}

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
jwt.secret=${DB_SERCET}
jwt.expiration=${DB_EXPIRATION}
```

### 🔑 Environment Variables (Optional)

Override properties using environment variables:

#### **Windows (PowerShell)**
```powershell
$env:SPRING_DATASOURCE_URL=${DB_URL}
$env:SPRING_DATASOURCE_USERNAME="adham"
$env:SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
$env:JWT_SECRET="your-secret-key"
.\mvnw.cmd spring-boot:run
```

#### **Linux / macOS**
```bash
export SPRING_DATASOURCE_URL=${DB_URL}
export SPRING_DATASOURCE_USERNAME="adham"
export SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
export JWT_SECRET="your-secret-key"
./mvnw spring-boot:run
```

### 📊 Database Credentials (Docker Compose)

From `docker-compose.yml`:

| Property | Value |
|----------|-------|
| **Host** | `localhost` |
| **Port** | `3307` |
| **Database** | `store_db` |
| **Username** | `adham` |
| **Password** | `${DB_PASSWORD}` |
| **Root Password** | `root123` |

---

## 🔐 Authentication & Authorization

### 🔑 JWT Token Flow

1. **User registers** → System creates user account
2. **User logs in** → System validates credentials and returns JWT token
3. **User sends token** → Include token in `Authorization` header
4. **System validates token** → Grants access to protected endpoints
5. **Token expires** → User must login again (default: 1 hour)

### 📤 Getting a Token

#### Step 1: Register a New User

```bash
curl -X POST "http://localhost:8080/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Adham Ahmed",
    "email": "adham@example.com",
    "password": "SecurePassword123!"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Step 2: Login with Existing User

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "adham@example.com",
    "password": "SecurePassword123!"
  }'
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 🛡️ Using the Token

Include the token in the `Authorization` header for all protected endpoints:

```bash
curl -X GET "http://localhost:8080/products" \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 🔓 Public Endpoints

Only these endpoints do **NOT** require authentication:

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

**All other endpoints require a valid JWT token.**

---

## 📡 API Endpoints

### 🔐 Authentication (`/api/auth`)

| Method | Path | Body | Response | Auth |
|--------|------|------|----------|------|
| **POST** | `/api/auth/register` | RegisterRequest | AuthResponse | ❌ |
| **POST** | `/api/auth/login` | LoginRequest | AuthResponse | ❌ |

#### RegisterRequest Example
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

#### LoginRequest Example
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

#### AuthResponse Example
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwiaWF0IjoxNjc4OTAwMDAwfQ.signature"
}
```

---

### 🏷️ Categories (`/categories`)

| Method | Path | Query Params | Body | Response | Auth |
|--------|------|--------------|------|----------|------|
| **GET** | `/categories` | - | - | `List<CategoryResponseDto>` | ✅ |
| **GET** | `/categories/{categoryId}` | - | - | `CategoryResponseDto` | ✅ |
| **POST** | `/categories` | - | CategoryRequestDto | `CategoryResponseDto` | ✅ |
| **PUT** | `/categories/{categoryId}` | - | CategoryRequestDto | `CategoryResponseDto` | ✅ |
| **GET** | `/categories/{categoryId}/products` | `page`, `size`, `sortBy` | - | `Page<ProductResponseDto>` | ✅ |

#### Create Category Example

**Request:**
```bash
curl -X POST "http://localhost:8080/categories" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "name": "Electronics",
    "description": "All electronic devices and gadgets"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Electronics",
  "description": "All electronic devices and gadgets"
}
```

#### Get Products by Category

**Request:**
```bash
curl -X GET "http://localhost:8080/categories/1/products?page=0&size=10&sortBy=id" \
  -H "Authorization: Bearer <TOKEN>"
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Laptop",
      "price": 999.99,
      "stockQuantity": 50,
      "categoryId": 1
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "currentPage": 0,
  "pageSize": 10
}
```

---

### 📦 Products (`/products`)

| Method | Path | Query Params | Body | Response | Auth |
|--------|------|--------------|------|----------|------|
| **GET** | `/products` | `page`, `size`, `sortBy` | - | `Page<ProductResponseDto>` | ✅ |
| **GET** | `/products/{productId}` | - | - | `ProductResponseDto` | ✅ |
| **POST** | `/products` | - | ProductRequestDto | `ProductResponseDto` | ✅ |
| **PUT** | `/products/{productId}` | - | ProductRequestDto | `ProductResponseDto` | ✅ |
| **PUT** | `/products/{productId}/stock` | `quantity` | - | `ProductResponseDto` | ✅ |
| **DELETE** | `/products/{productId}` | - | - | `void` | ✅ |

#### Create Product Example

**Request:**
```bash
curl -X POST "http://localhost:8080/products" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "name": "iPhone 15 Pro",
    "price": 1299.99,
    "categoryId": 1,
    "stockQuantity": 100
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "iPhone 15 Pro",
  "price": 1299.99,
  "stockQuantity": 100,
  "categoryId": 1
}
```

#### Get All Products (Paginated)

**Request:**
```bash
curl -X GET "http://localhost:8080/products?page=0&size=20&sortBy=name" \
  -H "Authorization: Bearer <TOKEN>"
```

#### Restock Product

**Request:**
```bash
curl -X PUT "http://localhost:8080/products/1/stock?quantity=50" \
  -H "Authorization: Bearer <TOKEN>"
```

**Response:**
```json
{
  "id": 1,
  "name": "iPhone 15 Pro",
  "price": 1299.99,
  "stockQuantity": 150,
  "categoryId": 1
}
```

#### Update Product

**Request:**
```bash
curl -X PUT "http://localhost:8080/products/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "name": "iPhone 15 Pro Max",
    "price": 1399.99,
    "categoryId": 1,
    "stockQuantity": 100
  }'
```

#### Delete Product

**Request:**
```bash
curl -X DELETE "http://localhost:8080/products/1" \
  -H "Authorization: Bearer <TOKEN>"
```

---

### 🛒 Orders (`/orders`)

| Method | Path | Body | Response | Auth |
|--------|------|------|----------|------|
| **POST** | `/orders` | OrderRequestDto | `OrderResponse` | ✅ |

#### Create Order Example

**Request:**
```bash
curl -X POST "http://localhost:8080/orders" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{
    "items": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }'
```

**Response:**
```json
{
  "id": 1,
  "totalPrice": 2599.97,
  "orderDateTime": "2024-06-16T15:30:45",
  "userId": 1,
  "items": [
    {
      "id": 1,
      "productId": 1,
      "productName": "iPhone 15 Pro",
      "quantity": 2,
      "priceAtSale": 1299.99
    },
    {
      "id": 2,
      "productId": 2,
      "productName": "MacBook Pro",
      "quantity": 1,
      "priceAtSale": 999.99
    }
  ]
}
```

---

## 🏗️ Project Architecture

```
store-management-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/adham/store_management_system/
│   │   │       ├── auth/                 # Authentication logic
│   │   │       │   ├── AuthController.java
│   │   │       │   ├── AuthService.java
│   │   │       │   ├── AuthResponse.java
│   │   │       │   ├── LoginRequest.java
│   │   │       │   └── RegisterRequest.java
│   │   │       ├── controller/           # REST controllers
│   │   │       │   ├── CategoryController.java
│   │   │       │   ├── ProductController.java
│   │   │       │   └── OrderController.java
│   │   │       ├── dto/                  # Data Transfer Objects
│   │   │       │   ├── CategoryRequestDto.java
│   │   │       │   ├── CategoryResponseDto.java
│   │   │       │   ├── ProductRequestDto.java
│   │   │       │   ├── ProductResponseDto.java
│   │   │       │   ├── OrderRequestDto.java
│   │   │       │   ├── OrderResponse.java
│   │   │       │   ├── OrderItemRequestDto.java
│   │   │       │   ├── OrderItemResponse.java
│   │   │       │   └── ErrorResponse.java
│   │   │       ├── entity/               # JPA entities
│   │   │       │   ├── Category.java
│   │   │       │   ├── Product.java
│   │   │       │   ├── Order.java
│   │   │       │   └── OrderItem.java
│   │   │       ├── security/             # Security configuration
│   │   │       │   ├── JwtService.java
│   │   │       │   ├── JwtAuthFilter.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── service/              # Business logic
│   │   │       │   ├── CategoryService.java
│   │   │       │   ├── ProductService.java
│   │   │       │   └── OrderService.java
│   │   │       ├── repository/           # Data access layer
│   │   │       │   ├── CategoryRepository.java
│   │   │       │   ├── ProductRepository.java
│   │   │       │   └── OrderRepository.java
│   │   │       ├── mapper/               # DTO mappers
│   │   │       ├── exception/            # Custom exceptions
│   │   │       ├── user/                 # User entities & repos
│   │   │       │   ├── User.java
│   │   │       │   ├── Role.java
│   │   │       │   └── UserRepository.java
│   │   │       └── StoreManagementSystemApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/ & templates/
│   └── test/
│       └── java/...
├── docker-compose.yml         # MySQL container
├── pom.xml                    # Maven dependencies
├── mvnw & mvnw.cmd           # Maven wrapper
└── README.md                  # This file
```

### 🔄 Architecture Pattern

`Controller → Service → Repository → Database`

- **Controllers** handle HTTP requests and responses
- **Services** contain business logic and validations
- **Repositories** manage database operations using Spring Data JPA
- **DTOs** transfer data between layers
- **Entities** represent database tables

---

## 🔒 Security Best Practices

### 1️⃣ Password Security
- Passwords are **bcrypt hashed** before storage
- Never transmitted in plain text (HTTPS recommended in production)
- Minimum strength requirements enforced

### 2️⃣ JWT Token Security
- Tokens are **signed with HS256** algorithm
- **Expiration time**: 1 hour (configurable via `jwt.expiration`)
- **Stored in Authorization header** in Bearer format
- Invalid/expired tokens automatically rejected

### 3️⃣ CORS & CSRF
- Spring Security configured for stateless JWT authentication
- CSRF protection disabled (stateless API)
- CORS headers can be configured as needed

### 4️⃣ Input Validation
- All requests validated with **Jakarta Bean Validation**
- Email format validation
- Numeric constraints (price > 0, stock >= 0)
- Required field validation

### 5️⃣ Database Security
- Unique constraints on email and product names
- Foreign key constraints prevent orphaned records
- Cascade delete configured appropriately
- SQL injection prevented via parameterized queries (JPA)

### 6️⃣ Role-Based Access Control
- User roles: `USER` and `ADMIN`
- Extensible role system for future permissions
- Currently all authenticated users can access all endpoints

### ⚠️ Production Recommendations
- [ ] Use HTTPS/TLS for all communications
- [ ] Store `jwt.secret` in environment variables (not in code)
- [ ] Implement rate limiting
- [ ] Add request logging and monitoring
- [ ] Use a dedicated secrets management tool (e.g., Vault)
- [ ] Enable CORS selectively for trusted domains
- [ ] Implement audit logging for sensitive operations
- [ ] Add API versioning for backward compatibility

---

## 📦 Database Schema

```sql
-- Users Table
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('USER', 'ADMIN') DEFAULT 'USER'
);

-- Categories Table
CREATE TABLE categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) UNIQUE NOT NULL,
  description TEXT
);

-- Products Table
CREATE TABLE products (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) UNIQUE NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  stock_quantity INT DEFAULT 0,
  category_id BIGINT NOT NULL,
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Orders Table
CREATE TABLE orders (
  order_id BIGINT PRIMARY KEY AUTO_INCREMENT,
  local_date_time DATETIME NOT NULL,
  total_price DECIMAL(10, 2) NOT NULL,
  user_id BIGINT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Order Items Table
CREATE TABLE order_items (
  order_item BIGINT PRIMARY KEY AUTO_INCREMENT,
  quantity INT NOT NULL,
  price_at_sale DECIMAL(10, 2) NOT NULL,
  product_id BIGINT NOT NULL,
  order_id BIGINT NOT NULL,
  FOREIGN KEY (product_id) REFERENCES products(id),
  FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

### 🔑 Key Relationships
- **User ↔ Order**: One-to-Many (1:N)
- **Order ↔ OrderItem**: One-to-Many (1:N)
- **OrderItem ↔ Product**: Many-to-One (N:1)
- **Product ↔ Category**: Many-to-One (N:1)

---

## 🧪 Testing

### Test with Postman

1. **Import Collection**: Create a new Postman collection with endpoints from the [API Endpoints](#-api-endpoints) section
2. **Set Environment Variables**:
   - `base_url`: `http://localhost:8080`
   - `token`: (Set after successful login)
3. **Test Flow**:
   - Register → Login → Get Token
   - Create Category → Create Product
   - Create Order → Verify Order

### Test with cURL

All examples in this README use cURL and can be copied directly to your terminal.

### Run Unit Tests

```bash
./mvnw test
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/AmazingFeature`)
3. **Commit** your changes (`git commit -m 'Add AmazingFeature'`)
4. **Push** to the branch (`git push origin feature/AmazingFeature`)
5. **Open** a Pull Request

### Code Style
- Follow Java conventions
- Use meaningful variable names
- Add comments for complex logic
- Keep methods focused and single-responsibility

---

## 📄 License

This project is licensed under the **MIT License** - see the LICENSE file for details.

---

## 📞 Support & Contact

- **Issues**: Open an issue on GitHub for bugs and feature requests
- **Email**: adham231366@gmail.com
- **Documentation**: See the [API Endpoints](#-api-endpoints) section for detailed endpoint documentation

---

## 🎉 Changelog

### v0.0.1-SNAPSHOT (Current)
- ✅ User authentication with JWT
- ✅ Category management
- ✅ Product management with inventory
- ✅ Order creation with order items
- ✅ Pagination and sorting support
- ✅ Global exception handling
- ✅ Docker Compose MySQL setup

---

<div align="center">

**Built with ❤️ by Adham**

⭐ If you found this helpful, please consider giving it a star!

</div>

