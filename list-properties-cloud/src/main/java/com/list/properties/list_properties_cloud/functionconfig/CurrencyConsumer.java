package com.list.properties.list_properties_cloud.functionconfig;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

@Configuration
public class CurrencyConsumer {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(CurrencyConsumer.class);

    @Bean
    public Consumer<String> currencyIn() {
        return payload -> {
            logger.info("Received from RabbitMQ: " + payload);
            System.out.println("Received from RabbitMQ : " + payload);
        };
    }
}
