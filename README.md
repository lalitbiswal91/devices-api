## 📱 Devices API

- A Spring Boot REST API for managing devices, built with Java 21.
  The application supports creation, updation, partial updation retrival, and deleting devices with persistence backed by PostgreSQL.

## ✨ Technology Used

- **Java 21**
- **Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **Testcontainers**
- **Maven**
- **Docker**
 
## ✨ Features
- **CRUD operations for devices**
- **Search devices by brand and state**
- **Validation using Jakarta Bean Validation**
- **Global exception handling**
- **Integration tests**
- **Dockerized application**


## 🚀 Getting Started

### Prerequisites

- **Java 21**
- **Docker**
- **Maven**

### Installation

**Clone the application from below GIT link**

```bash
https://github.com/lalitbiswal91/devices-api.git
```

**Start PostgreSQL using Docker Compose**
```sh
docker compose up -d postgres
```

**Build the project using Maven**
```sh
mvn clean install
```

### Running the Application

**Using Maven**

```sh
mvn spring-boot:run
```

**Running Tests**

- run DeviceControllerIT class


## 📚 Devices API Endpoints

### ➕ Create Device (**POST**)

```http
POST /api/devices
```

**Request Body**
```json
{
  "name": "iPhone 15",
  "brand": "Apple",
  "state": "AVAILABLE"
}
```

### 🔍 Fetch Single Device (**GET**)

```http
GET /api/devices/{id}
```

### 📋 Fetch All Devices (**GET**)

```http
GET /api/devices
```

### 📋 Fetch Devices by Brand (**GET**)

```http
GET /api/devices?brand={brandName}
```

### 📋 Fetch Devices by State (**GET**)

```http
GET /api/devices?brand={state}
```

### 🔄 Update Device (FULL) (**PUT**)

```http
PUT /api/devices/{id}
```

**Request Body**
```json
{
  "name": "New Name",
  "brand": "Samsung",
  "state": "IN_USE"
}
```

### 🔄 Update Device (Partial) (**PATCH**)

```http
PUT /api/devices/{id}
```

**Request Body**
```json
{
  "state": "INACTIVE"
}
```

### ❌ Delete Device (**DELETE**)

```http
DELETE /api/devices/{id}
```

### Get Test Coverage

- To generate test coverage, run the below maven command:

```sh
mvn clean verify
```





