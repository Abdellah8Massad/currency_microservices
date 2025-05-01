package com.currency.microservices.currency_conversion_service.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.currency.microservices.currency_conversion_service.model.CurrencyConversion;
import com.currency.microservices.currency_conversion_service.proxy.CurrencyExchangeProxy;

@RestController
public class ConversionController {

        @Autowired
        private CurrencyExchangeProxy proxy;

        @Value("${server.port}")
        private String serverPort;

        @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
        public CurrencyConversion calculateCurrencyConversion(
                        @PathVariable String from,
                        @PathVariable String to,
                        @PathVariable double quantity) {

                // Mock response for demonstration
                double conversionMultiple = 65.00;
                double totalCalculatedAmount = quantity * conversionMultiple;

                HashMap<String, String> uriVariables = new HashMap<>();
                uriVariables.put("from", from);
                uriVariables.put("to", to);

                ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                                CurrencyConversion.class, uriVariables);

                CurrencyConversion conversion = responseEntity.getBody();

                return new CurrencyConversion(
                                conversion.getId(),
                                from,
                                to,
                                conversion.getConversionMultiple(),
                                quantity,
                                conversion.getConversionMultiple() * quantity,
                                conversion.getEnvironment());
        }

        @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
        public CurrencyConversion calculateCurrencyConversionFeign(
                        @PathVariable String from,
                        @PathVariable String to,
                        @PathVariable double quantity) {

                CurrencyConversion conversion = proxy.getExchangeValue(from, to);

                return new CurrencyConversion(
                                conversion.getId(),
                                from,
                                to,
                                conversion.getConversionMultiple(),
                                quantity,
                                conversion.getConversionMultiple() * quantity,
                                conversion.getEnvironment() + " feign");
        }
}