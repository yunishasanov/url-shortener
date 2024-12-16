
# URL Shortener Service

## Overview
The **URL Shortener Service** is a Spring Boot-based REST API that provides functionality to shorten URLs and resolve shortened URLs back to their original form. This service is built using Java 21, leveraging H2 as an in-memory database for persistence and Spring Boot's Web and JPA starters for RESTful communication and data access.

## Features
- **Shorten URL**: Convert a long URL into a short, hash-based identifier.
- **Resolve URL**: Retrieve the original URL from a shortened identifier.
- **Validation**: Ensures that the provided URL is valid before processing.
- **Error Handling**: Provides meaningful error responses for invalid inputs and resource not found scenarios.

## API Endpoints


### Shorten URL
- **Endpoint**: `/api/v1/urls`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "url": "https://example.com"
  }
  ```
- **Response**:
  ```json
  {
    "shortenedUrl": "qwer1234"
  }
  ```
- **Errors**:
    - `400 BAD REQUEST`: If the URL is empty or invalid.

### Resolve URL
- **Endpoint**: `/api/v1/urls/{hash}`
- **Method**: `GET`
- **Response**:
  ```json
  {
    "originalUrl": "https://example.com"
  }
  ```
- **Errors**:
    - `404 NOT FOUND`: If the hash does not map to any URL.

## Technologies Used
- **Java 21**
- **Spring Boot 3.3.6**
- **H2 Database** (In-memory database)
- **Guava** (For hash generation)
- **Lombok** (For boilerplate code reduction)
- **SpringDoc OpenAPI** (For API documentation)
- **PIT Mutation Testing** (For ensuring code quality)

## Getting Started

### Prerequisites
- Java JDK 21
- Apache Maven
- Any IDE (e.g., IntelliJ IDEA, Eclipse)

### Steps to Run
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd url-shortener
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```
5. Access the API at `http://localhost:8080/api/v1/urls`.

### Swagger

You can hit below link to get swagger doc.

- **http://localhost:8080/swagger-ui/index.html**

### Running Tests
Execute the following command to run the unit tests:
```bash
mvn test
```

To check mutation testing results with PIT:
```bash
mvn clean install
```
Checkout the target/pit-reports/index.html

## Configuration
Update the `application.properties` file for custom configurations. Default configurations include:
- `hash.seed`: Seed value for hash generation.

Example:
```properties
hash.seed=42
```

## Error Handling
- **400 BAD REQUEST**: Invalid or empty input data.
- **404 NOT FOUND**: Resource not found.
- **500 INTERNAL SERVER ERROR**: General server errors.

## Project Structure
- **Controller**: Handles API endpoints (`UrlShortenerController`).
- **Service**: Contains business logic (`UrlShortenerService`).
- **Repository**: Manages database interactions (`UrlMappingRepository`).
- **DTOs**: Request and response data transfer objects.
- **Entity**: Represents database models (`UrlMapping`).
- **Exception Handling**: Centralized error management using `GlobalExceptionHandler`.

## Possible Future Enhancements
- Add support for custom short URL aliases.
- Implement expiration logic for shortened URLs.
- Replace in-memory storage with a distributed database (e.g., PostgreSQL, Redis).
- Add rate limiting to protect against abuse.
