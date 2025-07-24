package com.list.properties.list_properties_cloud.controller;

import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "currency-queue")
    public void receiveMessage(String message) {
        logger.info("Received message from RabbitMQ: " + message);
        System.out.println("Received message: " + message);
    }

}
