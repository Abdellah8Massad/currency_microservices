package com.gateway.microservices.api_gateway;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import com.gateway.microservices.api_gateway.config.Configuration;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Autowired
    private Configuration configuration;

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Logging Filter: request id -> " + exchange.getRequest().getId());

        logger.info("-------------------------------------------------");
        logger.info("Env Profil: " + configuration.getEnvProfil());
        logger.info("URI Conversion: " + configuration.getUriConversion());
        logger.info("URI Exchange: " + configuration.getUriExchange());
        logger.info("URI API Gateway: " + configuration.getUriApiGetway());
        logger.info("URI Spring Cloud: " + configuration.getUriSpringCloud());
        logger.info("-------------------------------------------------");

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 1;
    }

}
