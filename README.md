# 🎬 Movie Catalog Service

Director & Movie Management REST API built with Spring Boot, featuring JPA relationships, validation, H2 database, and OpenAPI documentation.

---

## 🚀 Features

* Manage **Movies, Directors, and Genres**
* Associate and remove genres from movies
* Filter movies by **genre** and **release year**
* Bean Validation for request DTOs
* Global exception handling with structured responses
* OpenAPI 3.0 documentation (YAML + Swagger UI)
* Unit and integration testing
* Optimized data fetching using Hibernate batch fetching
* Dockerized for easy setup and deployment

---

## 🏗️ Tech Stack

* Java 21
* Spring Boot
* Spring Data JPA (Hibernate)
* H2 In-Memory Database
* JUnit 5 + Mockito
* MockMvc (Integration Testing)
* Springdoc OpenAPI
* Maven Wrapper (`./mvnw`)

---

## 🔄 Request Flow

```
Client → Controller → Service → Repository → Database (H2)
                          ↓
                     DTO Mapping
                          ↓
                     Response
```
---

## 🗄️ Data Model

```
Director (1) ────< (Many) Movie (Many) >──── (Many) Genre
```
----

## ⚙️ Configuration

Defined in `application.properties`:

```properties
app.name=Movie Catalog Service
app.version=1.0

catalog.max-genres-per-movie=3
catalog.default-rating=5.0

spring.jpa.properties.hibernate.default_batch_fetch_size=20
```

---

## ▶️ How to Run

### 1. Clone Repository

```bash
git clone https://github.com/rik-m-27/movie-catalog-service.git
cd movie-catalog-service
```

### 2. Run Application

```bash
./mvnw spring-boot:run
```

Application will start at:

```
http://localhost:8090
```

> No external database setup required. Uses in-memory H2 database.

---

## 🐳 Run with Docker

### Build and Run using Docker Compose

```bash
./mvnw clean package -DskipTests
docker-compose up --build
```

Application will be available at:

```
http://localhost:8090
```

> Docker Compose builds the image and starts the service in one command.

---

## 📄 API Documentation

### Swagger UI

```
http://localhost:8090/swagger-ui/index.html
```

### OpenAPI YAML

```
openapi.yaml (available in project root)
```

---

## 🧪 Testing

Run all tests:

```bash
./mvnw test
```

Includes:

* Unit tests for service layer (JUnit + Mockito)
* Integration tests using MockMvc (`@SpringBootTest`)

---

## 📌 API Endpoints

### 🎥 Movies

* `POST /api/movies` → Create a movie (include genreIds) 
* `GET /api/movies` → List all movies (filter: genre, year)
* `GET /api/movies/{id}` → Get movie by ID with genres
* `PUT /api/movies/{id}` → Update movie details
* `DELETE /api/movies/{id}` → Delete a movie 
* `POST /api/movies/{id}/genres/{gid}` → Associate a genre with a movie 
* `DELETE /api/movies/{id}/genres/{gid}` → Remove a genre from a movie

### 🎬 Directors

* `POST /api/directors` → Create a new director
* `GET /api/directors` → List all directors
* `GET /api/directors/{id}` → Get director by ID
* `PUT /api/directors/{id}` → Update a director
* `DELETE /api/directors/{id}` → Delete a director
* `GET /api/directors/{id}/movies` → Get all movies by a director 

### 🎭 Genres

* `POST /api/genres` → Create a genre
* `GET /api/genres` → List all genres

### ⚙️ System

* `GET /api/info` → Application info

---

## 🔍 Sample cURL & Output

### 1. System Info
**Request:**
```bash
curl -X 'GET' \
  'http://localhost:8090/api/info' \
  -H 'accept: application/json'
```

**Response:**

```json
{
  "name": "Movie Catalog Service",
  "version": "1.0"
}
```

---

### 2. Create Director

**Request:**

```bash
curl -X 'POST' \
  'http://localhost:8090/api/directors' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "Aditya Dhar",
  "nationality": "Indian",
  "birthYear": 1983
}'
```

**Response:**

```json
{
  "id": 1,
  "name": "Aditya Dhar",
  "nationality": "Indian",
  "birthYear": 1983
}
```

---

### 3. Fetch All Movies

**Request:**

```bash
curl -X 'GET' \
  'http://localhost:8090/api/movies' \
  -H 'accept: application/json'
```

**Response:**

```json
[
  {
    "id": 2,
    "title": "Dhurandhar",
    "releaseYear": 2025,
    "rating": 9,
    "director": {
      "id": 1,
      "name": "Aditya Dhar",
      "nationality": "Indian",
      "birthYear": 1983
    },
    "genres": [
      {
        "id": 2,
        "name": "Drama"
      },
      {
        "id": 1,
        "name": "Action"
      }
    ]
  }
]
```

---

## ⚡ Performance Consideration

To avoid the N+1 query problem, Hibernate batch fetching is enabled:

```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=20
```

This ensures efficient loading of related entities like genres and directors.

---

## 📌 Highlights

* Clean layered architecture (Controller → Service → Repository)
* Strong separation of concerns and maintainable design
* Robust validation and centralized error handling
* Efficient data fetching using Hibernate batch strategy
* Well-tested service and API layers (unit + integration tests)
* Clear and complete API documentation (OpenAPI + Swagger)
* Fully containerized and easy to run with Docker Compose

---

## 👤 Author

Sourav Mandal

---
