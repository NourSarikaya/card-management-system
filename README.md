# Card Management System

A Spring Boot application designed to securely manage card data and customer profiles.  This project demonstrates the implementation of standard security protocls, strict data validation, and scalable architectural patterns.

## Use Case
The **Card Management System** serves as a centralized backend for financial services to handle the life cycle of cards and customer information.  It solves the need for secure data storage, identity verification through JWT, and automated business rule enforcement(e.g. credit limit/expiry validation).

---
## Technology Stack
* **Java**: version 17
* **Spring Boot**: version 4.0.2
* **JDK**: Eclipse Temurin 25
* **Database**: PostgreSQL
* **SLF4J**: version 1.7.7
* **OpenAPI Swagger**: version 3.0.0

## Key Accomplishments 
* **Secure Authentication Pipeline**: Successfully transitioned from basic request-body credentials to a dual layered **Basic Auth** (for login) and **JWT Bearer Auth** (for stateless session management) system.
* **Custom Validation Engine**: Developed custom annotations to enforce business rules, such as credit cards being the only cards to have a credit limit and date formatting (`yyyyMM`).
* **Automated API Documentation**: Integrated **Swagger UI**, providing a living interactive documentation portal that simplifies manual testing.
* **Stateless Security Architecture**: Built a security filter chain that decodes and validates JWTs, ensuring the application remains stateless and horizontally scalable.

---

## Feature & Technical Implementation
* **Identity Exchange**: Implemented an 'AuthController' that trades **Base64-encoded basic credentials** for a  **JSON Web Token (JWT)**.
* **Bearer Authorization**: Secured all data endpoints ('/api/cards/**', '/api/customers/**') using a custom JwtAuthenticationFilter' that intercepts requests and verifies the cryptographic signature against a server-side secret.

---

## Design Patterns & Architecture
* **Controller -> Service -> Repository**: Followed the classic layered architecture to ensure separation of concerns and maintainability.
* **DTO Pattern**: Utilized **Data Transfer Objects** (where all fields are handled as `Strings`) to decouple the API contract from the database layer, allowing for use of our own validation.
* **Mapper Pattern**: Implemented dedicated mapper components to handle transformation logic between entities and DTO's

  <img align="center" width="400" height="711" alt="CMS-architecture-flow-chart" src="https://github.com/user-attachments/assets/96ae8ee6-23a4-4184-bb94-5f13b995aa18" />

---

## Logging & Error Handling
* **SLF4J/Logback:** Integrated the SLF4J logging framework to track authentication success/failure and API requests.
* **Global Exception Handler**: Implemented a `@ControllerAdvice` component to watch for and catch system-wide errors and return consistent, user-friendly JSON error messages instead of raw stack traces and default Java messages.

## Data Models 
**Entities**

<img width="624" height="372" alt="image" src="https://github.com/user-attachments/assets/862095d4-e092-4472-bc80-1d66f345b161" />

**DTOs**

<img width="663" height="322" alt="image" src="https://github.com/user-attachments/assets/4499146c-823a-4f5c-8df8-02ebc578a725" />

<img width="665" height="378" alt="image" src="https://github.com/user-attachments/assets/6d2d90a7-3940-4c9b-8172-026cc0ef0a59" />

**V1 DTOs**

<img width="636" height="375" alt="image" src="https://github.com/user-attachments/assets/c403d587-ef03-4fd6-a4cb-778a621ca3b5" />

