package com.gateway.microservices.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiGatewayConfiguration {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p
                        .path("/get")
                        .filters(f -> f
                                .addRequestHeader("MyHeader", "MyURI"))
                        .uri("http://httpbin.org:80"))
                .route("currency-exchange", r -> r.path("/currency-exchange/**")
                        .uri("lb://currency-exchange"))
                .route("currency-conversion", r -> r.path("/currency-conversion/**")
                        .uri("lb://currency-conversion"))
                .route("currency-conversion-feign", r -> r.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion"))
                .route("currency-conversion-new", r -> r.path("/currency-conversion-new/**")
                        .filters(f -> f.rewritePath(
                                "/currency-conversion-new/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion"))
                .build();
    }
}
