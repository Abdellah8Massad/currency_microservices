package com.currency.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class CircuitBreakerController {

    private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);

    @GetMapping("/sample-api")
    @Retry(name = "sample-api", fallbackMethod = "hardcodedResponse")
    public String sampleApi() {
        logger.info("Sample API call received");
        ResponseEntity<String> response = new RestTemplate().getForEntity("http://localhost:8080/sample-api",
                String.class);
        return response.getBody();
    }

    // @CircuitBreaker(name = "default", fallbackMethod = "hardcodedResponse")
    @GetMapping("/sample-api-rate")
    @RateLimiter(name = "default")
    public String sampleApiRate() {
        return "sample-api-rate";
    }

    @GetMapping("/sample-api-bulkhead")
    @Bulkhead(name = "default")
    public String sampleApiBulkhead() {
        return "sample-api-bulkhead";
    }

    public String hardcodedResponse(Exception ex) {
        return "fallback-response";
    }
}
