# Card Management System

A Spring Boot application designed to securely manage card data and customer profiles.  This project demonstrates the implementation of standard security protocls, strict data validation, and scalable architectural patterns.

## Use Case
The **Card Management System** serves as a centralized backend for financial services to handle the life cycle of cards and customer information.  It solves the need for secure data storage, identity verification through JWT, and automated business rule enforcement(e.g. credit limit/expiry validation).

---

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

---

## Logging & Error Handling
* **SLF4J/Logback:** Integrated the SLF4J logging framework to track authentication success/failure and API requests.
* **Global Exception Handler**: Implemented a `@ControllerAdvice` component to watch for and catch system-wide errors and reutrn consistent, user-friendly JSON error messages instead of raw stack traces and default Java message. 
