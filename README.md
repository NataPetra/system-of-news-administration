# System of News Administration

System of News Administration is a microservices-based project designed for managing and delivering news content.

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
    git clone https://github.com/your-username/system-of-news-administration.git
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

```text
# Example API registers a new subscriber
curl -X 'POST' \
  'http://localhost:8082/api/v1/app/users/register/subscriber' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "subscriber10",
  "password": "subscriber"
}'
```

```text
# Example API authenticates a user and returns an access token
curl -X 'POST' \
  'http://localhost:8082/api/v1/app/users/login' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "username": "subscriber10",
  "password": "subscriber"
}'
```

```text
# Example API retrieves all news sections
curl -X 'GET' \
  'http://localhost:8081/api/v1/app/news/' \
  -H 'accept: application/json'
```

```text
# Example API creates and saves a new comment
curl -X 'POST' \
  'http://localhost:8081/api/v1/app/comments/' \
  -H 'accept: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdWJzY3JpYmVyMTAiLCJpYXQiOjE2OTk5ODMxNDgsImV4cCI6MTY5OTk4Njc0OCwicm9sZSI6IlJPTEVfU1VCU0NSSUJFUiJ9.2l_ZxabnSWCucQa7jUwO8OrfUF5z7Ifk0MRwj35GD9EYY8XbpRQl2akmRIY7_ld8Hg4pRLZ406Ak-1LA97IqUw' \
  -H 'Content-Type: application/json' \
  -d '{
  "text": "Default text",
  "newsId": 1
}'
```



