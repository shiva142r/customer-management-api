
# Customer Management API

This is a Spring Boot RESTful API for managing customer data. It supports CRUD operations, calculates membership tiers based on spending, and includes Swagger documentation.

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Steps

```bash
git clone <your-repo-url> (if using Git)
cd customer-management-api
mvn spring-boot:run
```

App will be available at: `http://localhost:8080`

### Swagger/OpenAPI Docs
Visit: `http://localhost:8080/swagger-ui.html`

### H2 Database Console
Visit: `http://localhost:8080/h2-console`

Use the following:
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: *(empty)*

## API Endpoints

- `POST /customers`
- `GET /customers/{id}`
- `GET /customers?name={name}`
- `GET /customers?email={email}`
- `PUT /customers/{id}`
- `DELETE /customers/{id}`

## Assumptions
- `tier` is not stored in DB but calculated in responses.
- Validation includes required `name`, `email` with format check.
- UUID is generated automatically during customer creation.
