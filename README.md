# 🛒 OmniGrocer

> **OmniGrocer** is a modern, production-grade fresh grocery and perishables e-commerce web platform built on **Java 25**, **Spring Boot 4**, **Thymeleaf**, **Spring Data JPA**, **PostgreSQL**, **Spring Security**, and **Docker**.

---

## 🌟 Highlights

- **Modern Java 25 & Spring Boot 4.1.2** core architecture.
- **Thymeleaf HTML5 Server-Side Rendering**: Responsive user storefront and comprehensive admin dashboard.
- **Spring Security Authentication & RBAC**: Dedicated security filter chains for customers (`ROLE_USER`) and administrators (`ROLE_ADMIN`).
- **Spring Data JPA & PostgreSQL**: Robust persistence layer with schema migrations, relational mapping, and ACID compliance.
- **Testcontainers & Complete Test Suite**: Over 100 automated unit, repository, service, controller, and containerized PostgreSQL integration tests.
- **Containerization Ready**: Multi-stage `Dockerfile` (Alpine JRE) and Docker Compose specification (`compose.yaml`).

---

## 🏗️ Architecture & Modules

```
io.github.deepakjvm.ecommerce
├── configuration/       # Spring Security, PasswordEncoder (BCrypt)
├── controller/          # Storefront, Admin management, Error controllers
├── models/              # User, Product, Category, Cart, CartProduct entities
├── repository/          # Spring Data JPA repositories
└── services/            # Transactional business logic & password migration
```

### Storefront Features
- Browse fresh produce, perishables, and categorized items.
- Dynamic shopping cart and instant mock checkout.
- Customer account registration, login, and profile management.

### Admin Dashboard Features
- Inventory & product catalog management (add, update, delete with category tagging).
- Real-time category administration.
- Customer management and profile controls.

---

## 🚀 Getting Started

### Prerequisites
- **JDK 25** (e.g. OpenJDK 25)
- **Maven 3.9+**
- **Docker & Docker Compose** (optional, for containerized run and Testcontainers)

### Running Locally with PostgreSQL
1. Start the database using Docker Compose:
   ```bash
   docker compose up postgres -d
   ```
2. Build and run the application:
   ```bash
   mvn clean spring-boot:run
   ```
3. Access the storefront at [http://localhost:8080](http://localhost:8080)
   - **Admin portal**: [http://localhost:8080/admin/login](http://localhost:8080/admin/login)
   - Default admin credentials: `username: admin`, `password: 123`
   - Default user credentials: `username: lisa`, `password: 765`

### Running Fully Containerized
```bash
docker compose up --build
```

---

## 🧪 Running Tests

The test suite runs against in-memory H2 as well as real PostgreSQL containers via **Testcontainers**:

```bash
mvn clean test
```

---

## 📄 License
This project is licensed under the MIT License.
