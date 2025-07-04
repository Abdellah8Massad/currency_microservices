package com.currency.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.currency.microservices.currency_exchange_service.model.CurrencyExchange;
import com.currency.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;

import io.micrometer.core.ipc.http.HttpSender.Response;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    public CurrencyExchangeController(CurrencyExchangeRepository currencyExchangeRepository) {
        this.currencyExchangeRepository = currencyExchangeRepository;
    }

    @GetMapping("/currency-exchange/from/{from}/to/{to}")
    public CurrencyExchange getExchangeValue(@PathVariable String from, @PathVariable String to) {

        logger.info("getExchangeValue called with from: {} to: {}", from, to);

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
        CurrencyExchange currencyExchange = currencyExchangeRepository.findByFromAndTo(from, to);
        currencyExchange.setConversionMultiple(conversionMultiple);
        currencyExchangeRepository.save(currencyExchange);
        return ResponseEntity.ok("Conversion multiple updated successfully for " + from + " to " + to);
    }

}