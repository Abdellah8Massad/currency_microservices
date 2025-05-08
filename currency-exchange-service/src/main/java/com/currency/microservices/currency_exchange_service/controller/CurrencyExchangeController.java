package com.currency.microservices.currency_exchange_service.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.currency.microservices.currency_exchange_service.model.CurrencyExchange;
import com.currency.microservices.currency_exchange_service.repository.CurrencyExchangeRepository;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private CurrencyExchangeRepository currencyExchangeRepository;

    @Autowired
    private Environment environment;

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
        String port = environment.getProperty("local.server.port");

        // CHANGE-KUBERNETES
        String host = environment.getProperty("HOSTNAME");
        String version = "v11";

        currencyExchange.setEnvironment(port + " " + version + " " + host);
        return currencyExchange;
    }
}