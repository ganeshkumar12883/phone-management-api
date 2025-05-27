# 📞 Phone Numbers Management API

A Spring Boot application for managing check and activate phone number(s) for new/existing customer.
It also includes basic customer data persistence if a new customer is coming in.

## 🛠 Technical Details
* Java 17
* Spring Boot
* H2 Database (In Memory DB)
* JPA (Hibernate)
* Gradle
* JUnit 5
* Docker (optional)

---

## 🚀 Features

- Retrieve all existing phone numbers
- Retrieve phone numbers by customer name or email id
- Activate phone numbers for customers existing or new customers
- Validate input with meaningful error responses
- H2 in-memory database with schema and sample data preloaded
- H2 Console enabled for DB inspection
- Exposes Actuator endpoints (health, info, metrics)

---

## 📦 Requirements

- Java 17+
- Gradle 8.x
- (Optional) Docker (for containerized builds)

---

## ▶️ Run Locally

### 🛠 Clone & Build

```bash
git clone https://github.com/your-username/phone-management-api.git
cd phone-management-api
./mvnw clean package