# MetroRide API

A Spring Boot REST API providing real-time metro timing information.

**Version:** 1.0.0

---

## Prerequisites

- Java 21+
- Maven 3.8+ or Gradle 8+

---

## Run with Maven

```bash
# Build
mvn clean package

# Run
java -jar target/metroride-api-1.0.0.jar

# Or using Spring Boot plugin directly
mvn spring-boot:run
```

## Run with Gradle

```bash
# Build
gradle build

# Run
java -jar build/libs/metroride-api-1.0.0.jar

# Or using Gradle task
gradle bootRun
```

---

## API Endpoints

Base URL: `http://localhost:8080`

| Method | Endpoint                        | Description                   |
|--------|---------------------------------|-------------------------------|
| GET    | `/api/metro/health`             | Service health check          |
| GET    | `/api/metro/timings`            | Get all station timings        |
| GET    | `/api/metro/timings/{station}`  | Get timing for a station       |

### Sample Requests

```bash
# Health check
curl http://localhost:8080/api/metro/health

# All timings
curl http://localhost:8080/api/metro/timings

# Station-specific
curl http://localhost:8080/api/metro/timings/Central
```

### Sample Response — `/api/metro/timings`

```json
[
  { "station": "Central", "next_train": "10:05 AM", "line": "Blue" },
  { "station": "Airport", "next_train": "10:12 AM", "line": "Red" },
  { "station": "City Park", "next_train": "10:08 AM", "line": "Green" }
]
```

---

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for branch naming, commit format, and MR guidelines.
