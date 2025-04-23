# University Management System (UMS)

A RESTful web application for managing university operations, including user roles, courses, and enrollments.

## Project Description

The University Management System (UMS) is a Spring Boot application designed to manage university-related activities. It supports user authentication with JWT, role-based access control (ADMIN, TEACHER, STUDENT), and CRUD operations for users, courses, and enrollments. The project includes API documentation with Swagger, unit tests with JUnit and Mockito, and a PostgreSQL database with Flyway migrations.

## Features

- User registration and JWT-based authentication
- Role-based access control (ADMIN, TEACHER, STUDENT)
- CRUD operations for students, courses, and enrollments
- Course sorting by start date or number of students
- Input validation and custom exception handling
- API documentation via Swagger UI
- Unit tests for services and controllers
- Database migrations with Flyway
- Logging with SLF4J

## Prerequisites

- **Java 21**: Install JDK 21 and verify with `java --version`.
- **Maven**: Install Maven for dependency management and verify with `mvn --version`.
- **PostgreSQL**: Install PostgreSQL (version 13 or higher) and verify with `psql --version`.
- **Postman**: Install Postman for manual API testing.
- **Git** (optional): For cloning the project repository.

## Project Structure

```
University Management System/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── qtj4/
│   │   │           └── ums/
│   │   │               ├── config/              # Configuration classes (Security, OpenAPI)
│   │   │               ├── controller/          # REST API controllers
│   │   │               ├── dto/                 # Data Transfer Objects
│   │   │               ├── exception/           # Custom exceptions and handlers
│   │   │               ├── factory/             # Factory pattern implementations
│   │   │               ├── mapper/              # MapStruct mappers for entity-DTO conversion
│   │   │               ├── model/               # JPA entity classes
│   │   │               ├── repository/          # Spring Data JPA repositories
│   │   │               ├── security/            # JWT and security configurations
│   │   │               ├── service/             # Business logic services
│   │   │               └── strategy/            # Strategy pattern for sorting
│   │   └── resources/
│   │       ├── application.properties           # Application configurations
│   │       └── db/migration/                    # Flyway migration scripts
│   └── test/
│       └── java/
│           └── com/
│               └── qtj4/
│                   └── ums/                    # Test classes for services and controllers
└── pom.xml                                      # Maven configuration
└── README.md                                    # Project documentation
```

## Setup and Installation

1. **Clone the Repository** (if applicable):
   ```bash
   git clone https://github.com/qtj4/UniversityManagementSystem
   ```

2. **Set Up PostgreSQL Database**:
    - Create a database named `ums`:
      ```bash
      psql -U postgres -c "CREATE DATABASE ums;"
      ```
    - Update `src/main/resources/application.properties` with your database credentials:
      ```
      spring.datasource.url=jdbc:postgresql://localhost:5432/ums
      spring.datasource.username=your_postgres_username
      spring.datasource.password=your_postgres_password
      jwt.secret=your_jwt_secret_key
      ```
      Replace `your_postgres_username`, `your_postgres_password`, and `your_jwt_secret_key` with appropriate values.

3. **Build the Project**:
    - Install dependencies and build:
      ```bash
      mvn clean install
      ```

4. **Run the Application**:
    - Start the Spring Boot application:
      ```bash
      mvn spring-boot:run
      ```
    - The application runs on `http://localhost:8080`.

5. **Verify the Application**:
    - Access Swagger UI at `http://localhost:8080/swagger-ui.html` to view API documentation.
    - Check console logs to confirm the server is running and Flyway migrations have executed.

## Testing the Application

### Running Unit Tests
- Execute unit tests for services and controllers:
  ```bash
  mvn test
  ```
- Tests use JUnit 5 and Mockito, covering `UserService`, `CourseService`, `EnrollmentService`, and all controllers.

### Manual Testing with Postman

1. **Set Up Postman**:
    - Download and install Postman from [www.postman.com](https://www.postman.com/downloads/).
    - Create a new collection named `UMS API`.

2. **Register a User**:
    - **Method**: POST
    - **URL**: `http://localhost:8080/auth/register`
    - **Headers**:
        - `Content-Type: application/json`
    - **Body** (raw, JSON):
      ```json
      {
          "username": "admin1",
          "password": "admin123",
          "role": "ADMIN",
          "name": "Admin User",
          "email": "admin@example.com"
      }
      ```
    - **Expected Response**: `200 OK` with `"User registered successfully"`.
    - Repeat for TEACHER and STUDENT users as needed.

3. **Log In to Obtain JWT Token**:
    - **Method**: POST
    - **URL**: `http://localhost:8080/auth/login`
    - **Headers**:
        - `Content-Type: application/json`
    - **Body** (raw, JSON):
      ```json
      {
          "username": "admin1",
          "password": "admin123"
      }
      ```
    - **Expected Response**: `200 OK` with:
      ```json
      {
          "token": "eyJhbGciOiJIUzUxMiJ9..."
      }
      ```
    - Copy the `token` for secured endpoints.

4. **Configure Authorization**:
    - In the `UMS API` collection, set:
        - **Authorization Type**: `Bearer Token`
        - **Token**: Paste the JWT token.
    - Alternatively, add `Authorization: Bearer <token>` to individual request headers.

5. **Test Key Endpoints**:
    - **Create a Student (ADMIN only)**:
        - **Method**: POST
        - **URL**: `http://localhost:8080/students`
        - **Headers**: `Content-Type: application/json`, `Authorization: Bearer <token>`
        - **Body**:
          ```json
          {
              "username": "student1",
              "name": "Student One",
              "email": "student1@example.com"
          }
          ```
        - **Expected Response**: `200 OK` with student details.
    - **Get All Students (ADMIN only)**:
        - **Method**: GET
        - **URL**: `http://localhost:8080/students`
        - **Headers**: `Authorization: Bearer <token>`
        - **Expected Response**: `200 OK` with a list of students.
    - **Create a Course (ADMIN or TEACHER)**:
        - **Method**: POST
        - **URL**: `http://localhost:8080/courses`
        - **Headers**: `Content-Type: application/json`, `Authorization: Bearer <token>`
        - **Body**:
          ```json
          {
              "name": "Mathematics",
              "description": "Introduction to Mathematics",
              "teacherId": 2
          }
          ```
        - **Expected Response**: `200 OK` with course details.
    - **Create an Enrollment (ADMIN or TEACHER)**:
        - **Method**: POST
        - **URL**: `http://localhost:8080/enrollments`
        - **Headers**: `Content-Type: application/json`, `Authorization: Bearer <token>`
        - **Body**:
          ```json
          {
              "studentId": 1,
              "courseId": 1
          }
          ```
        - **Expected Response**: `200 OK` with enrollment details.

6. **Explore All Endpoints**:
    - Use Swagger UI (`http://localhost:8080/swagger-ui.html`) to view all endpoints and their documentation.
    - Test additional endpoints like `PUT /students/{id}`, `DELETE /courses/{id}`, etc., following the same pattern.

### Error Handling
- **401 Unauthorized**: Invalid or missing JWT token. Re-login to get a new token.
- **403 Forbidden**: Insufficient role permissions (e.g., non-ADMIN accessing `/students` POST).
- **404 Not Found**: Resource not found (e.g., invalid `studentId`).
- **400 Bad Request**: Invalid input (check DTO constraints in Swagger).

## API Documentation

- **Swagger UI**: Access at `http://localhost:8080/swagger-ui.html`.
- **OpenAPI JSON**: Available at `http://localhost:8080/v3/api-docs`.
- All endpoints are documented with descriptions, parameters, and response codes.

## Technologies Used

- **Java 21**: Programming language
- **Spring Boot 3.2.0**: Web framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Database access
- **PostgreSQL**: Relational database
- **Flyway**: Database migrations
- **MapStruct**: Object mapping
- **Lombok**: Boilerplate reduction
- **JWT**: Token-based authentication
- **Swagger (Springdoc)**: API documentation
- **JUnit 5 & Mockito**: Unit testing
- **SLF4J**: Logging

## Troubleshooting

- **Database Issues**: Verify PostgreSQL is running (`pg_isready`) and credentials match `application.properties`.
- **Port Conflict**: If `8080` is in use, update `server.port` in `application.properties` (e.g., `server.port=8081`).
- **JWT Issues**: Tokens expire after 24 hours. Re-run `/auth/login` for a new token.
- **Logs**: Check console or `logs/application.log` for errors.

## Contact Information

For questions or issues, contact temirlanzholaev@gmail.com.