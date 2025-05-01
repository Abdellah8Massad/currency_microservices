package com.currency.microservices.currency_exchange_service.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class ResilienceController {

    @GetMapping("/circuit-breaker")
    @CircuitBreaker(name = "myService", fallbackMethod = "fallback")
    public String circuitBreakerExample() {
        simulateFailure();
        return "Success!";
    }

    @GetMapping("/retry")
    @Retry(name = "myService", fallbackMethod = "fallback")
    public String retryExample() {
        simulateFailure();
        return "Success!";
    }

    @GetMapping("/rate-limiter")
    @RateLimiter(name = "myService", fallbackMethod = "fallback")
    public String rateLimiterExample() {
        return "Rate Limiter Success!";
    }

    @GetMapping("/bulkhead")
    @Bulkhead(name = "myService", fallbackMethod = "fallback")
    public String bulkheadExample() {
        return "Bulkhead Success!";
    }

    @GetMapping("/time-limiter")
    @TimeLimiter(name = "myService", fallbackMethod = "fallback")
    public CompletableFuture<String> timeLimiterExample() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay();
            return "Time Limiter Success!";
        });
    }

    // Simulate a failure
    private void simulateFailure() {
        throw new RuntimeException("Simulated failure");
    }

    // Simulate a delay
    private void simulateDelay() {
        try {
            Thread.sleep(3000); // 3 seconds delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Fallback method
    public String fallback(Throwable t) {
        return "Fallback response: " + t.getMessage();
    }
}