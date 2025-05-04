# Resilience4j Examples with Spring Boot

## Microservices with Eureka and Load Balancing

This project demonstrates a microservice architecture using the following components:

1. **naming-server**: Acts as the Eureka server for service discovery.
2. **currency-exchange**: A microservice that provides currency exchange rates.
3. **currency-conversion**: A microservice that calculates currency conversions by calling the `currency-exchange` service.
4. **api-gateway**: A gateway that routes requests to the appropriate microservices and provides load balancing.

### Key Concepts:
1. **Service Discovery**: Microservices (`currency-exchange` and `currency-conversion`) register themselves with the `naming-server`. The `api-gateway` dynamically discovers these services.
2. **Load Balancing**: The `api-gateway` uses Eureka to distribute requests across multiple instances of a service.
3. **Fault Tolerance**: Resilience4j is integrated into the microservices to handle failures gracefully.

### Workflow:
1. **Service Registration**: 
   - `currency-exchange` and `currency-conversion` register with the `naming-server`.
   - The `api-gateway` queries the `naming-server` to discover available services.
2. **Request Flow**:
   - A client sends a request to the `api-gateway`.
   - The `api-gateway` routes the request to the appropriate service (e.g., `currency-conversion`).
   - If `currency-conversion` needs exchange rates, it calls the `currency-exchange` service.
3. **Load Balancing**:
   - If multiple instances of `currency-exchange` or `currency-conversion` are running, the `api-gateway` distributes requests among them.

### Example:
- A client requests a currency conversion via the `api-gateway`:
  ```bash
  curl http://localhost:8000/currency-conversion/from/USD/to/INR/quantity/100
  ```
- The `api-gateway` routes the request to the `currency-conversion` service.
- The `currency-conversion` service calls the `currency-exchange` service to get the exchange rate.
- The response is returned to the client via the `api-gateway`.

---

## Features Implemented

1. **Circuit Breaker**: Prevents repeated calls to a failing service by "breaking" the circuit after a threshold of failures.
2. **Retry**: Automatically retries failed operations based on a configurable retry policy.
3. **Rate Limiter**: Limits the number of requests to a service within a specified time window.
4. **Bulkhead**: Limits the number of concurrent calls to a service to prevent resource exhaustion.
5. **Time Limiter**: Ensures operations complete within a specified timeout duration.

---

## Configuration

### Dependencies
Add the following dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
    <version>2.0.2</version>
</dependency>
```

### application.properties

The following configuration is used to define the behavior of Resilience4j features:

#### Circuit Breaker Configuration
```properties
resilience4j.circuitbreaker.instances.myService.failure-rate-threshold=50
# If 50% of the requests fail, the circuit breaker will open.

resilience4j.circuitbreaker.instances.myService.sliding-window-size=10
# The circuit breaker will evaluate the last 10 requests to calculate the failure rate.

resilience4j.circuitbreaker.instances.myService.wait-duration-in-open-state=10000
# The circuit breaker will remain open for 10 seconds before transitioning to a half-open state.
```

#### Retry Configuration
```properties
resilience4j.retry.instances.myService.max-attempts=3
# The service will retry a failed operation up to 3 times.

resilience4j.retry.instances.myService.wait-duration=500ms
# The service will wait 500 milliseconds between retry attempts.
```

#### Rate Limiter Configuration
```properties
resilience4j.ratelimiter.instances.myService.limit-for-period=5
# Allows up to 5 requests per second.

resilience4j.ratelimiter.instances.myService.limit-refresh-period=1s
# The rate limiter refreshes its limit every 1 second.
```

#### Bulkhead Configuration
```properties
resilience4j.bulkhead.instances.myService.max-concurrent-calls=3
# Allows up to 3 concurrent calls to the service.

resilience4j.bulkhead.instances.myService.max-wait-duration=0
# If the bulkhead is full, additional calls will immediately fail without waiting.
```

#### Time Limiter Configuration
```properties
resilience4j.timelimiter.instances.myService.timeout-duration=2s
# If a service call takes longer than 2 seconds, it will time out and trigger the fallback.
```

---

## Endpoints and Expected Results

### Circuit Breaker: `/circuit-breaker`
- **Behavior**: Simulates a failure and triggers the circuit breaker.
- **Fallback Response**: `Fallback response: Simulated failure.`
- **Example Command**:
  ```bash
  curl http://localhost:8000/circuit-breaker
  ```
- **Expected Result**:
  - Initially, the service will fail.
  - After repeated failures, the circuit will open, and the fallback response will be returned.

---

### Retry: `/retry`
- **Behavior**: Simulates a failure and retries the operation up to 3 times before triggering the fallback.
- **Fallback Response**: `Fallback response: Simulated failure.`
- **Example Command**:
  ```bash
  curl http://localhost:8000/retry
  ```
- **Expected Result**:
  - The service will retry up to 3 times.
  - If all attempts fail, the fallback response will be returned.

---

### Rate Limiter: `/rate-limiter`
- **Behavior**: Limits the number of requests to 5 per second. Excess requests trigger the fallback.
- **Fallback Response**: `Fallback response: Request not permitted.`
- **Example Command**:
  ```bash
  curl http://localhost:8000/rate-limiter
  ```
- **Expected Result**:
  - Up to 5 requests per second will succeed.
  - Additional requests within the same second will trigger the fallback response.

---

### Bulkhead: `/bulkhead`
- **Behavior**: Limits the number of concurrent calls to 3. Excess calls trigger the fallback.
- **Fallback Response**: `Fallback response: Bulkhead full.`
- **Example Command**:
  ```bash
  curl http://localhost:8000/bulkhead
  ```
- **Expected Result**:
  - Up to 3 concurrent calls will succeed.
  - Additional concurrent calls will trigger the fallback response.

---

### Time Limiter: `/time-limiter`
- **Behavior**: Simulates a delay. If the operation takes longer than 2 seconds, the fallback is triggered.
- **Fallback Response**: `Fallback response: Timeout.`
- **Example Command**:
  ```bash
  curl http://localhost:8000/time-limiter
  ```
- **Expected Result**:
  - If the operation completes within 2 seconds, the normal response will be returned.
  - If the operation exceeds 2 seconds, the fallback response will be returned.

---

## Automate Testing with `watch`
To repeatedly call the endpoints and observe the behavior, use the `watch` command:

```bash
watch -n 1 curl http://localhost:8000/{endpoint}
```

Replace `{endpoint}` with the specific endpoint you want to test:

- `/circuit-breaker`
- `/retry`
- `/rate-limiter`
- `/bulkhead`
- `/time-limiter`

For example:
```bash
watch -n 1 curl http://localhost:8000/circuit-breaker
```

---

## Running the Application with Docker

### Step 1: Generate the Docker Image
To create a Docker image for the Spring application, follow these steps:
1. Ensure Docker is installed and running on your machine.
2. Build the Docker image using Maven:
   ```bash
   mvn spring-boot:build-image -DskipTests
   ```
   This command will generate a Docker image for the application using the Spring Boot plugin.

### Step 2: Run the Application with Docker Compose
To run the application and its dependencies using Docker Compose:
1. Ensure `docker-compose.yml` is properly configured in the project directory.
2. Start the services using Docker Compose:
   ```bash
   docker-compose up
   ```
3. To run the services in detached mode, use:
   ```bash
   docker-compose up -d
   ```

### Step 3: Verify the Application
Once the services are running, you can verify the application:
- Access the application at `http://localhost:<port>` (replace `<port>` with the configured port in `docker-compose.yml`).
- Check the logs using:
   ```bash
   docker-compose logs
   ```

### Step 4: Stop the Services
To stop the running services, use:
```bash
docker-compose down
```

---

## Service Mapping in `application.properties`

The `application.properties` file is used to configure the mapping between services. Below is an example of how services are mapped:

```properties
# Service URLs
currency-exchange-service.url=http://currency-exchange:8000
currency-conversion-service.url=http://currency-conversion:8100
api-gateway-service.url=http://api-gateway:8765
```

### Explanation:
1. **currency-exchange-service.url**: Maps to the `currency-exchange` service running on port `8000`.
2. **currency-conversion-service.url**: Maps to the `currency-conversion` service running on port `8100`.
3. **api-gateway-service.url**: Maps to the `api-gateway` service running on port `8765`.

These mappings allow the services to communicate with each other using their respective URLs. Ensure that the service names match the ones defined in `docker-compose.yml`.