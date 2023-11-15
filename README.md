# System of News Administration

System of News Administration is a microservices-based project designed for managing and delivering news content.

## Contents
- [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
- [Technology Stack](#technology-stack)
   - [Backend](#backend)
   - [Database](#database)
   - [Testing](#testing)
   - [Documentation](#documentation)
   - [Logging](#logging)
   - [Containerization](#containerization)
- [Installation](#installation)
- [Usage](#usage)
   - [Swagger](#swagger)
   - [Example API Requests](#example-api-requests)

## Getting Started

### Prerequisites
- Java 17
- Docker

### Technology Stack

#### Backend:
- Java
- Spring Boot
- Spring Data JPA
- Liquibase
- Spring Cache
- Spring Security
- Spring Configuration Processor
- Spring Cloud Feign
- Spring Cloud Netflix Eureka
- Spring Cloud Config Server

#### Database
- PostgreSQL
- Redis

#### Testing
- JUnit 5
- Spring Boot Test
- TestContainers
- WireMock

#### Documentation
- Swagger (OpenAPI 3.0)

#### Logging
- Logback

#### Containerization
- Docker
- docker-compose

### Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/NataPetra/system-of-news-administration.git
    ```

2. **Navigate to the project directory:**
    ```bash
    cd system-of-news-administration
    ```

3. **Build and run the Docker containers:**
    ```bash
    docker-compose up --build
    ```

## Usage

To access the news content and perform user-related operations, use the provided APIs. The microservices communicate with each other using Eureka Server and Feign Client.

### Swagger:

- [News-service Swagger](http://localhost:8081/swagger-ui/index.html#/)
- [Users-service Swagger](http://localhost:8082/swagger-ui/index.html#/)

### Example API Requests:

1. **Register a new subscriber:**
    ```bash
    curl -X 'POST' \
      'http://localhost:8080/api/v1/app/users/register/subscriber' \
      -H 'accept: application/json' \
      -H 'Content-Type: application/json' \
      -d '{
      "username": "subscriber10",
      "password": "subscriber"
    }'
    ```

2. **Authenticate a user and get an access token:**
    ```bash
    curl -X 'POST' \
      'http://localhost:8080/api/v1/app/users/login' \
      -H 'accept: application/json' \
      -H 'Content-Type: application/json' \
      -d '{
      "username": "subscriber10",
      "password": "subscriber"
    }'
    ```

3. **Retrieve all news sections:**
    ```bash
    curl -X 'GET' \
      'http://localhost:8080/api/v1/app/news/' \
      -H 'accept: application/json'
    ```

4. **Create and save a new comment:**
    ```bash
    curl -X 'POST' \
      'http://localhost:8080/api/v1/app/comments/' \
      -H 'accept: application/json' \
      -H 'Authorization: Bearer YOUR_ACCESS_TOKEN' \
      -H 'Content-Type: application/json' \
      -d '{
      "text": "Default text",
      "newsId": 1
    }'
    ```

### Authentication API

| Endpoint               | Description                                 | Method  | Request Body                   | Response       |
|------------------------|---------------------------------------------|---------|--------------------------------|--------------------|
| `/api/v1/app/users/login` | Authenticate user and generate access token | POST    | JSON: `{ "username": "example_user", "password": "user_password" }` | Bearer token       |
| `/api/v1/app/users/validate` | Validate user authentication token       | GET     | Header: `Authorization: Bearer user_token` | JSON: `{ "username": "example_user", "role": "user_role" }` |

### User Registration API

| Endpoint                      | Description                          | Method | Request Body                                                                                          | Response Body       |
|-------------------------------|--------------------------------------|--------|-------------------------------------------------------------------------------------------------------|---------------------|
| `/api/v1/app/users/register/admin` | Register an administrator           | POST   | JSON: `{ "username": "example_admin", "password": "admin_password" }`                                 | String: Registered username  |
| `/api/v1/app/users/register/journalist` | Register a journalist            | POST   | JSON: `{ "username": "example_journalist", "password": "journalist_password" }` | String: Registered username |
| `/api/v1/app/users/register/subscriber` | Register a subscriber           | POST   | JSON: `{ "username": "example_subscriber", "password": "subscriber_password" }` | String: Registered username |

### Comment API

| Endpoint                           | Description                                     | Method | Request Body                                    | Response Body |
|------------------------------------|-------------------------------------------------|--------|-------------------------------------------------|---------------|
| `/api/v1/app/comments`             | Save a comment                                  | POST   | JSON: `{ "text": "Comment text", "newsId": 1 }` | CommentResponseDto |
| `/api/v1/app/comments/{id}`        | Update a comment by ID                           | PUT    | JSON: `{ "text": "Updated comment text", "newsId": 1 }`     | CommentResponseDto |
| `/api/v1/app/comments/{id}`        | Get a comment by ID                             | GET    | -                                               | CommentResponseDto |
| `/api/v1/app/comments/news/{newsId}` | Get comments by news ID                        | GET    | -                                               | List<CommentResponseDto> |
| `/api/v1/app/comments/search`      | Search comments by keyword                       | GET    | -                                               | List<CommentResponseDto> |
| `/api/v1/app/comments/{id}`        | Delete a comment by ID                           | DELETE | -                                               | Void          |

### News API

| Endpoint                           | Description                                     | Method | Request Body                   | Response Body |
|------------------------------------|-------------------------------------------------|--------|--------------------------------|---------------|
| `/api/v1/app/news`                 | Save a news section                              | POST   | JSON: `{ "title": "News Title", "text": "News Content" }` | NewsResponseDto |
| `/api/v1/app/news/{id}`            | Update a news section by ID                       | PUT    | JSON: `{ "title": "Updated Title", "text": "Updated Content" }` | NewsResponseDto |
| `/api/v1/app/news/{id}`            | Get a news section by ID                         | GET    | -                              | NewsResponseDto |
| `/api/v1/app/news`                 | Get all news sections                            | GET    | -                              | List<NewsResponseDto> |
| `/api/v1/app/news/{newsId}/comments` | Get news with comments by news ID              | GET    | -                              | NewsWithCommentsResponseDto |
| `/api/v1/app/news/search`          | Search news sections by keyword and date         | GET    | -                              | List<NewsResponseDto> |
| `/api/v1/app/news/{id}`            | Delete a news section by ID                       | DELETE | -                              | Void          |