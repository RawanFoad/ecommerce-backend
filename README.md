**Outline:**
-Technical Assignment
-Architecture / Design Documentation
-Instructions to Run the Application





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





**Architecture / Design Documentation**
1. Overall Architecture
This project is a Spring Boot application implementing an e-commerce backend. It follows a layered architecture with multi-tenancy support.

[DONE]            -Controller layer: Handles incoming REST API requests, validates input, and delegates work to services.
[DONE]            -Service layer: Contains the business logic. Services orchestrate between repositories, mappers, and external systems. DTO and Domain mapping usually happens here.
[DONE]            -Repository layer (DAO): Interfaces extending Spring Data JPA’s JpaRepository to interact with the database.
[DONE]            -Domain layer: Contains JPA entities that map directly to database tables.
[DONE]            -DTO layer: Data Transfer Objects exposed to the outside world (used in controllers and API responses).
[DONE]            -Mapper layer: Converts between Domain objects (Entities) and DTOs.
[STARTED/NOT DONE]-Configuration: MultiTenancyConfig: Configures tenant-based schema separation.
[STARTED/NOT DONE]-Flyway: Manages database schema migrations via src/main/resources/db/migration.

2. Technology Stack
                    Java 17+
                    Spring Boot 3.3.1
                    Spring Data JPA (Hibernate 6.x)
                    Flyway for database migrations
[STARTED/NOT DONE]  H2 / PostgreSQL / MySQL (depending on profile)
                    Maven for build
                    JUnit 5 + Mockito for testing
[NOT USED]          Docker for containerization

3. Key Features
                    Product management (CRUD with DTO ↔ Entity mapping)
                    Order and order-item tracking
                    "Frequently Bought Together" recommendation service
[STARTED/NOT DONE]  Multi-tenant support (each tenant has its own schema)
[STARTED/NOT DONE]  Flyway-based DB migrations





**Instructions to Run the Application**
Run Locally with Maven
Make sure you have:
Java 17+ installed (java -version)
Maven 3.9+ installed (mvn -v)

Steps:
# Clone the repository
git clone https://github.com/<your-username>/ecommerce-backend.git
cd ecommerce-backend

# Run with Maven
./mvnw spring-boot:run

The app will start at: http://localhost:8080
