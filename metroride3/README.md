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

---

## CI/CD Pipeline (Day 32)

### Jenkinsfile Stages

| Stage | What it does |
|-------|-------------|
| **Checkout** | Clones the repo from SCM (Git) |
| **Maven Build** | Runs `mvn clean package -DskipTests`, produces the JAR |
| **Archive JAR** | Saves `target/*.jar` as a Jenkins build artifact |
| **Docker Login** | Authenticates to the container registry (uses Jenkins credentials) |
| **Docker Build** | Builds the Docker image: `docker build -t myregistry/metroride-api:1.0.0 .` |
| **Docker Push** | Pushes the image to the registry: `docker push myregistry/metroride-api:1.0.0` |

### Docker Commands

```bash
# Build image
docker build -t metroride-api:1.0.0 .

# Run container
docker run -d -p 8080:8080 --name metroride-app metroride-api:1.0.0

# Check logs
docker logs -f metroride-app

# Stop container
docker stop metroride-app && docker rm metroride-app
```

### Docker Compose

```bash
# Start all services (app + monitor)
docker-compose up -d

# View logs
docker-compose logs -f

# Stop all services
docker-compose down

# Rebuild and restart
docker-compose up -d --build
```

Services started:
- `metroride-app` — Spring Boot API on port 8080
- `metroride-monitor` — busybox heartbeat logger

### Health Check Script

```bash
# Run system health check
bash health_check.sh
```

Prints: disk usage, memory usage, running Java processes.

---

## Cloud Deployment & Kubernetes (Day 33)

### SonarQube — Code Quality Scan

```bash
# Start SonarQube locally via Docker
docker run -d --name sonarqube -p 9000:9000 sonarqube:community

# Run analysis (from project root)
sonar-scanner \
  -Dsonar.projectKey=metroride-api \
  -Dsonar.sources=src/main/java \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=<your_token>
```

Dashboard: http://localhost:9000 → Projects → metroride-api

---

### Kubernetes Deployment

```bash
# Apply all manifests
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml

# Verify
kubectl get pods
kubectl get svc

# Access (Minikube)
minikube service metroride-api-svc
```

---

### HPA — Horizontal Pod Autoscaler

Scales pods between 2–10 replicas targeting **50% CPU utilization**.

```bash
kubectl apply -f k8s/hpa.yaml
kubectl get hpa
kubectl describe hpa metroride-api-hpa
```

---

### KEDA — Event-Driven Autoscaling

Scales based on RabbitMQ `metro-requests` queue length (threshold: 10 messages).

```bash
# Install KEDA
helm repo add kedacore https://kedacore.github.io/charts
helm install keda kedacore/keda --namespace keda --create-namespace

# Apply ScaledObject
kubectl apply -f k8s/keda-scaledobject.yaml
kubectl get scaledobject
```

---

### Deployment Strategies

**Rolling Update** (`k8s/rolling-update.yaml`)
Replaces pods one by one. Zero downtime. Rollback: `kubectl rollout undo deployment/metroride-api-rolling`

**Blue/Green** (`k8s/blue-green.yaml`)
Two parallel deployments. Switch live traffic instantly by patching the Service selector:
```bash
# Go live with green
kubectl patch svc metroride-api-svc -p '{"spec":{"selector":{"slot":"green"}}}'

# Rollback to blue
kubectl patch svc metroride-api-svc -p '{"spec":{"selector":{"slot":"blue"}}}'
```

---

### Cloud Deployment — AWS S3

```bash
# Upload JAR artifact to S3
bash cloud-deploy.sh
```

Creates bucket `metroride-artifacts-<date>` in `ap-south-1`, uploads JAR with versioning enabled.
