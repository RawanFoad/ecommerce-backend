# E-Commerce Backend – Technical Documentation

## 1. Project Overview

This is a Spring Boot 3.3.1 backend for an e-commerce application. It provides:

* Product CRUD operations
* Frequently Bought Together recommendation logic
* Multi-tenancy scaffolding (partially implemented)
* DTO/Domain mapping
* Unit testing of services and controllers

---

## 2. Architecture Overview

```
Client (JSP/REST API)
        |
        v
  [Controller Layer]
        |
        v
  [Service Layer]
        |
        v
  [Repository Layer / Mock Data]
```

* **Controller Layer:** Handles HTTP requests, receives DTOs, calls service methods.
* **Service Layer:** Contains business logic (e.g., Product management, Frequently Bought Together computation).
* **Repository Layer:** Currently mocked; repository interfaces defined for database access (PostgreSQL support can be added).
* **DTO / Domain Mapping:** DTOs are used for API inputs/outputs; domain entities are used internally.

**Note:** Multi-tenancy is partially implemented using `CurrentTenantIdentifierResolver`, but database isolation has not been fully applied.

---

## 3. Key Components

| Layer         | Component                                                                                            | Responsibility                                             |
| ------------- | ---------------------------------------------------------------------------------------------------- | ---------------------------------------------------------- |
| Controller    | `ProductController`, `OrderController`, `CustomerController`, `FrequentlyBoughtTogetherController`   | Expose REST endpoints, handle requests/responses           |
| Service       | `ProductService`, , `OrderService`, `CustomerService`, `FrequentlyBoughtTogetherService`             | Business logic, validation, mapping between DTO and entity |
| DTO           | `ProductDto`, `OrderDto`, `CustomerDto`, `FrequentlyBoughtTogetherDto`, `OrderItemDto`               | Data transfer objects for API responses/requests           |
| Domain/Entity | `Product`, `Order`, `Customer`, `OrderItem`                                                          | JPA entities representing database tables                  |
| Repository    | `ProductRepository`, `OrderRepository`, `CustomerRepository`, `OrderItemRepository`                  | Interfaces for DB access (currently not connected)         |
| Mapper        | `ProductMapper`, `OrderMapper`, `CustomerMapper`                                                     | Converts between Entities and DTOs                         |

---

## 4. Multi-Tenancy (Partial)

* `MultiTenancyConfig` and `CurrentTenantIdentifierResolver` are included.
* Multi-tenant logic exists at the service level (tenant-aware filtering).
* Database-level multi-tenancy is not fully configured due to the absence of a live PostgreSQL instance.

---

## 5. Technology Stack

| Component         | Technology                          |
| ----------------- | ----------------------------------- |
| Backend Framework | Spring Boot 3.3.1                   |
| ORM / JPA         | Hibernate 6.5.2                     |
| Database          | PostgreSQL (planned, not connected) |
| Testing Framework | JUnit 5, Mockito                    |
| Build Tool        | Maven                               |
| IDE               | IntelliJ IDEA 2023.3.8              |

---

## 6. Running the Application

### Prerequisites

* Java 17
* Maven 4.0

### Steps

1. Clone the repository:

```
git clone https://github.com/RawanFoad/ecommerce-backend.git
cd ecommerce-backend
```

2. Build the project:

```
mvn clean install
```

3. Run the application (without database connection):

```
mvn spring-boot:run
```

**Note:**

* The application will build successfully, but database-dependent endpoints will fail because no PostgreSQL instance is connected.
* All unit tests pass successfully, verifying service-layer logic.

---

## 7. Testing

* Run unit tests:

```
mvn test
```

* Run full verification:

```
mvn verify
```

**Status:** All tests pass, confirming business logic correctness without requiring a live database.

---

## 8. Deliverable Scope

* Complete service-layer logic for products and frequently bought together recommendations.
* DTO/Domain mapping implemented.
* Repository interfaces defined; database connection is optional and not required for the assignment.
* Multi-tenancy scaffold included.
* Unit tests and integration tests validate core functionality.

