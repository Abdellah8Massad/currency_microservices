# Resilience4j Examples with Spring Boot

This project demonstrates the use of **Resilience4j** features in a Spring Boot application. Resilience4j provides fault-tolerance mechanisms such as Circuit Breaker, Retry, Rate Limiter, Bulkhead, and Time Limiter to make applications more resilient and robust.

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