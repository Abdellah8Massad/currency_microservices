package com.currency.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.currency.microservices.currency_exchange_service.model.CurrencyExchange;
import com.currency.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    private final StreamBridge streamBridge;

    public CurrencyExchangeController(CurrencyExchangeRepository currencyExchangeRepository,
            StreamBridge streamBridge) {
        this.currencyExchangeRepository = currencyExchangeRepository;
        this.streamBridge = streamBridge;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getExchangeValue(@PathVariable String from, @PathVariable String to) {

        logger.info("getExchangeValue called with from: {} to: {}", from, to);
        sendNotification("Streaming with rabbitMQ Exchange request from " + from + " to " + to);
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        if (currencyExchange == null) {
            throw new RuntimeException("Unable to find data for " + from + " to " + to);
        }
        currencyExchange.setEnvironment(serverPort + " instance-id");
        return currencyExchange;
    }

    @PostMapping("/currency-exchange/from/{from}/to/{to}/conversion-multiple/{conversionMultiple}")
    public ResponseEntity<String> postMethodName(@PathVariable Double conversionMultiple, @PathVariable String from,
            @PathVariable String to) {
        sendNotification("Streaming with rabbitMQ Update conversion multiple from " + from + " to " + to);
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        currencyExchange.setConversionMultiple(conversionMultiple);
        currencyExchangeRepository.save(currencyExchange);
        return ResponseEntity.ok("Conversion multiple updated successfully for " + from + " to " + to);
    }

    private void sendNotification(String message) {
        var result = streamBridge.send("currencyIn-out-0", message);
        logger.info("Message sent to sendCommunication-out-0: {} (success: {})", message, result);
    }

}