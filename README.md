
````markdown
# 🛒 CGM E-commerce Backend

This is a **Spring Boot** backend for an e-commerce application with partial multi-tenancy support, implemented as part of a technical assignment for **Lead Software Architect (SAP Commerce Cloud)** interview at CGM.
https://cgm.wd3.myworkdayjobs.com/de-DE/cgm/job/Lead-Software-Architect--m-f-d-_JR107374

---

## ⚙️ Features

- Core **Product**, **Order**, **Customer**, and **Frequently Bought Together** services
- **DTO & Domain mapping**
- Partial **multi-tenancy** support
- Comprehensive **unit tests** (`mvn test` ✅)
- Designed for **PostgreSQL** database

---

## 📐 Architecture Overview

```text
   ┌──────────────┐         ┌───────────────┐
   │   Controller │────────>│   Service      │
   └──────────────┘         └───────────────┘
                                 │
                                 ▼
                        ┌────────────────┐
                        │ Repository /   │
                        │ JPA / Hibernate│
                        └────────────────┘
                                 │
                                 ▼
                        ┌────────────────┐
                        │   PostgreSQL    │
                        └────────────────┘
````

* Controllers handle **HTTP requests**.
* Services contain **business logic**, including DTO mapping.
* Repositories interact with **JPA/Hibernate**.
* Multi-tenancy partially implemented at **Service/Repository** level.

---

## 🏃 Running the Application

> **Important:** The application currently **requires a PostgreSQL database**. `mvn spring-boot:run` **will fail** without it.

1. Configure PostgreSQL locally.
2. Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=your_user
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
```

3. Run with Maven:

```
mvn spring-boot:run
```

**Without database:**

* Application startup will fail (`Could not obtain connection to query metadata`)
* Unit tests and `mvn verify` **still work**.

---

## 🧪 Build & Test

```
mvn clean install   # Compile & package
mvn test            # Run unit tests (all pass ✅)
mvn verify          # Run full build verification (all pass ✅)
```

---

## ⚠️ Known Limitations

* **Multi-tenancy**: partially implemented, not fully tested with multiple schemas
* **Docker support**: Not included
* **Database connection**: Required for full application startup

---

## 🎯 Summary

* ✅ Fully functional **unit-tested backend**
* ⚠️ Database connection required for running
* ⚠️ Multi-tenancy & Docker: work in progress

---





**Technical Assignment**

I. Context
   You are tasked with designing and implementing a simplified e-commerce platform backend using the Spring Framework stack. The solution should demonstrate clean
   architecture, extensibility, and best practices.
   We are especially interested in how you approach the design, so please document your choices clearly.

II. Requirements
   1. System Design & Architecture
      ● Design a modular, extensible architecture that could grow into a larger system.
      ● Provide short documentation ( around 1 page) describing your architecture, design choices, and tradeoffs.
      ● Apply best practices: layered architecture, SOLID principles, clean code, etc.
   
   2. Domain: E-commerce
      Implement a backend for a simplified e-commerce application that supports the
      following core features:
      
      a. Products
         ● Support CRUD operations for products.
      
      b. Customers & Orders
         ● Business rule:
            ○ When creating an order, ensure enough stock exists for all items.
            ○ Deduct stock from products when the order is placed.
      
      c. “Frequently Bought Together” functionality
         Implement a feature that suggests a frequently bought together set of products for a given product.
         ● Input: a product ID + optional number of related products (N), default - 3.
         ● Output: top N products most frequently bought together with it.
   
   3. REST API
      Expose all relevant CRUD endpoints:
      ● Product - create, read, update, delete
      ● Customer - create, read, update, delete
      ● Order - create, read
      ● Frequently Bought Together
      Endpoints should follow REST best practices.
   
   4. Persistence & Data Model
      ● Use a relational database of your choice.
   
   5. Testing
      ● Write tests that cover the functionalities of your solution.
   
   6. Multi-Tenancy (Optional for bonus points)
      ● Extend your solution to support multiple tenants (e.g., multiple shops using the same system).
      ● Each tenant should have isolated data (products, customers, and orders).

III. Deliverables
   ● Source code (GitHub/GitLab repository).
   ● Short architecture/design documentation.
   ● Instructions to run the application (preferably via Docker or ./mvn spring-boot:run).

