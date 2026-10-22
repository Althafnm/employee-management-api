# Employee Management REST API

A RESTful backend application built with **Java Spring Boot** and **PostgreSQL** for managing employee records. It follows a clean layered architecture and supports full CRUD operations, input validation, centralized exception handling, pagination, search, and status filtering.

Built as a technical assessment for Codenzic Innovations.

---

## Features

- Create, view, update, and delete employees
- View all employees with **pagination and sorting**
- **Search** employees by first name or department
- **Filter** employees by status (`ACTIVE`, `INACTIVE`, `ON_LEAVE`)
- Input validation with clear error messages
- Centralized exception handling with a consistent JSON error format
- Duplicate email detection
- Swagger / OpenAPI documentation
- Unit tests using JUnit 5 and Mockito

---

## Tech Stack

| Technology                     | Purpose |
|--------------------------------|---|
| Java 23                        | Language |
| Spring Boot 4                  | Application framework |
| Spring Web                     | REST controllers |
| Spring Data JPA (Hibernate)    | Database access |
| PostgreSQL                     | Database |
| Jakarta Bean Validation        | Request validation |
| Lombok                         | Boilerplate reduction |
| springdoc-openapi (Swagger UI) | API documentation |
| JUnit 5 and Mockito            | Unit testing |
| Maven                          | Build tool |

---

## Project Structure

```
src/main/java/com/althaf/employeemanagement
├── controller
│   └── EmployeeController.java
├── dto
│   ├── EmployeeRequestDTO.java
│   └── EmployeeResponseDTO.java
├── entity
│   └── Employee.java
├── enums
│   └── EmployeeStatus.java
├── exception
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository
│   └── EmployeeRepository.java
├── service
│   ├── EmployeeService.java
│   └── EmployeeServiceImpl.java
└── EmployeemanagementApplication.java
```

**Layer responsibilities**

- **Controller:** handles HTTP requests and responses and triggers validation
- **Service:** contains the business logic (duplicate email check, DTO and entity mapping)
- **Repository:** database access through Spring Data JPA
- **Entity:** maps the `employees` table
- **DTO:** separates the API contract from the database model
- **Exception handler:** converts exceptions into consistent JSON error responses

---

## Getting Started

### Prerequisites

- Java 23 (JDK)
- Maven 3.8+ (or use the included Maven wrapper)
- PostgreSQL 14+
- Git

### 1. Clone the repository

```bash
git clone https://github.com/Althafnm/employee-management-api.git
cd employee-management-api
```

### 2. Create the database

```sql
CREATE DATABASE employee_db;
```

### 3. Configure the database

Open `src/main/resources/application.properties` and update the username and password:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/employee_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

The `employees` table is created automatically by Hibernate on first startup (`ddl-auto=update`), so no SQL script is needed.

### 4. Run the application

```bash
mvn spring-boot:run
```

Or run `EmployeemanagementApplication` from your IDE.

The application starts on **http://localhost:8080**.

### 5. Run the tests

```bash
mvn test
```

---

## API Documentation

### Swagger UI

Once the application is running, open:

```
http://localhost:8080/swagger-ui/index.html
```

### Postman Collection

A ready-to-import Postman collection is available in the `docs` folder:
`docs\Employee Management API.postman_collection.json`

### Endpoints

Base URL: `http://localhost:8080/api/v1/employees`

| Method | Endpoint | Description | Success |
|---|---|---|---|
| POST | `/api/v1/employees` | Create an employee | 201 Created |
| GET | `/api/v1/employees` | Get all employees (paginated) | 200 OK |
| GET | `/api/v1/employees/{id}` | Get an employee by ID | 200 OK |
| PUT | `/api/v1/employees/{id}` | Update an employee | 200 OK |
| DELETE | `/api/v1/employees/{id}` | Delete an employee | 204 No Content |
| GET | `/api/v1/employees/search` | Search by first name or department | 200 OK |
| GET | `/api/v1/employees/filter` | Filter by status | 200 OK |

### Query Parameters (list, search, and filter)

| Parameter | Default | Description |
|---|---|---|
| `page` | `0` | Page number (starts at 0) |
| `size` | `5` (`10` for filter) | Number of records per page |
| `sortBy` | `id` | Field to sort by (for example `firstName`, `salary`) |
| `direction` | `asc` | `asc` or `desc` |
| `keyword` | none | Search text (search endpoint only) |
| `status` | required | `ACTIVE`, `INACTIVE`, or `ON_LEAVE` (filter endpoint only) |

Examples:

```
GET /api/v1/employees?page=0&size=5&sortBy=salary&direction=desc
GET /api/v1/employees/search?keyword=tom
GET /api/v1/employees/filter?status=ACTIVE
```

### Validation Rules

| Field | Rule                                     |
|---|------------------------------------------|
| `firstName` | Required, 2 to 50 characters             |
| `lastName` | Required, 1 to 50 characters             |
| `email` | Required, valid email format, must be unique |
| `department` | Required                                 |
| `salary` | Required, must be greater than 0         |
| `status` | Required: `ACTIVE`, `INACTIVE`, or `ON_LEAVE` |

---

## Sample Requests and Responses

### Create an employee

**Request:** `POST /api/v1/employees`

```json
{
  "firstName": "Tom",
  "lastName": "Clark",
  "email": "tom@example.com",
  "department": "IT",
  "salary": 60000,
  "status": "ACTIVE"
}
```

**Response:** `201 Created`

```json
{
  "id": 1,
  "firstName": "Tom",
  "lastName": "Clark",
  "email": "tom@example.com",
  "department": "IT",
  "salary": 60000.0,
  "status": "ACTIVE",
  "createdAt": "2026-10-22T21:30:15.123456",
  "updatedAt": "2026-10-22T21:30:15.123456"
}
```

### Get an employee by ID

**Request:** `GET /api/v1/employees/1`

**Response:** `200 OK`

```json
{
  "id": 1,
  "firstName": "Tom",
  "lastName": "Clark",
  "email": "tom@example.com",
  "department": "IT",
  "salary": 60000.0,
  "status": "ACTIVE",
  "createdAt": "2026-10-22T21:30:15.123456",
  "updatedAt": "2026-10-22T21:30:15.123456"
}
```

### Get all employees (paginated)

**Request:** `GET /api/v1/employees?page=0&size=5&sortBy=id&direction=asc`

**Response:** `200 OK` (abbreviated)

```json
{
  "content": [
    {
      "id": 1,
      "firstName": "Tom",
      "lastName": "Clark",
      "email": "tom@example.com",
      "department": "IT",
      "salary": 60000.0,
      "status": "ACTIVE",
      "createdAt": "2026-10-22T21:30:15.123456",
      "updatedAt": "2026-10-22T21:30:15.123456"
    }
  ],
  "totalElements": 1,
  "totalPages": 1,
  "size": 5,
  "number": 0,
  "first": true,
  "last": true,
  "empty": false
}
```

### Update an employee

**Request:** `PUT /api/v1/employees/1`

```json
{
  "firstName": "Tom",
  "lastName": "Clark",
  "email": "tom@example.com",
  "department": "Finance",
  "salary": 65000,
  "status": "ON_LEAVE"
}
```

**Response:** `200 OK` returns the updated employee with the new values and a refreshed `updatedAt`.

### Delete an employee

**Request:** `DELETE /api/v1/employees/1`

**Response:** `204 No Content` (empty body)

### Search and filter

- `GET /api/v1/employees/search?keyword=IT` returns a paginated list of employees whose first name or department contains the keyword (case-insensitive). With no keyword, all employees are returned.
- `GET /api/v1/employees/filter?status=ACTIVE` returns a paginated list of employees with the given status.

Both return the same paginated structure as "Get all employees".

---

## Error Responses

All errors use the same JSON structure.

### Validation error: `400 Bad Request`

**Request:** `POST /api/v1/employees` with a blank first name and a negative salary

```json
{
  "timestamp": "2026-10-22T21:35:10.456789",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": {
    "firstName": "First name is required",
    "salary": "Salary must be greater than Zero"
  },
  "path": "/api/v1/employees"
}
```

### Not found: `404 Not Found`

**Request:** `GET /api/v1/employees/99`

```json
{
  "timestamp": "2026-10-22T21:36:02.789012",
  "status": 404,
  "error": "Not Found",
  "message": "Employee Not Found With ID: 99",
  "path": "/api/v1/employees/99"
}
```

### Duplicate email: `409 Conflict`

**Request:** `POST /api/v1/employees` with an email that already exists

```json
{
  "timestamp": "2026-10-22T21:37:45.123456",
  "status": 409,
  "error": "Conflict",
  "message": "Employee with email 'tom@example.com' already exists",
  "path": "/api/v1/employees"
}
```

---

## Database Schema

Table: `employees`

| Column | Type | Constraints |
|---|---|---|
| `id` | BIGINT | Primary key, auto-generated |
| `first_name` | VARCHAR(50) | Not null |
| `last_name` | VARCHAR(50) | Not null |
| `email` | VARCHAR(100) | Not null, unique |
| `department` | VARCHAR(50) | Not null |
| `salary` | DOUBLE PRECISION | Not null |
| `status` | VARCHAR | `ACTIVE`, `INACTIVE`, `ON_LEAVE` |
| `created_at` | TIMESTAMP | Set automatically on insert |
| `updated_at` | TIMESTAMP | Updated automatically on change |

---

## Design Decisions

- **DTOs instead of exposing entities:** the API contract stays independent of the database model.
- **Service interface and implementation:** keeps business logic separate and easy to test.
- **Centralized exception handling (`@RestControllerAdvice`):** one place for error formatting, which keeps controllers clean.
- **Read-only transactions for queries:** `@Transactional(readOnly = true)` on read methods.
- **Unit tests with Mockito:** the service layer is tested in isolation with a mocked repository.

---

## Author

**Muhammed Althaf  N M**
GitHub: [@Althafnm](https://github.com/Althafnm)