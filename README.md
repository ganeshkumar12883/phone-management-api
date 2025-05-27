# 📱 Phone Management API

A Spring Boot application to manage customer phone number activation. It includes validation, persistence, retrieval, and supports developer tools like Swagger and Actuator for monitoring.

---

## 🚀 Features

- ✅ Activate phone numbers with proper validations.
- 🔎 Retrieve phone numbers by customer name or email.
- 💾 Uses H2 in-memory database.
- 📖 Swagger UI for API documentation.
- 📡 Actuator endpoints for monitoring.
- 🧪 Unit and Integration tests with JaCoCo test coverage.
- 🧰 Dev and Prod profiles supported.

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot
- Spring Data JPA
- Spring Validation
- H2 Database
- Springdoc OpenAPI (Swagger)
- Spring Boot Actuator
- JUnit 5, JaCoCo

---

## 📦 Project Setup

### Clone the repository

```bash
   git clone https://github.com/<your-username>/phone-management-api.git
   cd phone-management-api
```

### ▶️ Build & Run the Application

```bash
   ./gradlew clean build
   
   ./gradlew bootRun
```

### Or use IntelliJ IDEA:

Open the project.

Run PhoneManagementApiApplication.java.

### 🛦️ Docker Setup
Docker config is present at the file 
```
Dockerfile

.dockerignore
```

#### Build the Docker image
```
docker build -t phone-management-api .
```
#### Run the Docker container
```
docker run -p 8080:8080 phone-management-api
```

### 📚 API Documentation
Swagger UI: http://localhost:8080/swagger-ui.html

OpenAPI Docs: http://localhost:8080/v3/api-docs

### 🔧 Dev Tools
H2 Console: http://localhost:8080/h2-console

Actuator Info: http://localhost:8080/actuator/info

Actuator Health: http://localhost:8080/actuator/health

### 📬 Postman Collection
Postman collection is available in:

```bash
   postman/phone-management-api.postman_collection.json
```
You can import this file into Postman to test endpoints.

### 🧪 Testing & Coverage
#### Run tests:
```
./gradlew test
```

#### Generate test coverage report:
```
./gradlew jacocoTestReport
```

#### HTML report is located at:
```
build/reports/jacoco/test/html/index.html
```

### 👨‍💻 Developer Info
##### Name: Ganesh Kumar Vellaichamy
##### Email: ganeshkumar12883@gmail.com

