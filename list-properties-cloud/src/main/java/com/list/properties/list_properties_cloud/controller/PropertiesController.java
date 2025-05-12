package com.list.properties.list_properties_cloud.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.list.properties.list_properties_cloud.config.Configuration;
import com.list.properties.list_properties_cloud.model.CurrencyConversion;
import com.list.properties.list_properties_cloud.model.PropConfig;

@RestController
public class PropertiesController {

    @Autowired
    private Configuration configuration;

    @Autowired
    private RestTemplate restTemplate;

    private PropConfig propConfig;

    @GetMapping("/properties")
    public PropConfig getProperties() {
        propConfig = new PropConfig(
                configuration.getEnvProfil(),
                configuration.getUriConversion(),
                configuration.getUriExchange(),
                configuration.getUriApiGetway(),
                configuration.getUriSpringCloud());
        return propConfig;
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public CurrencyConversion calculateCurrencyConversion(
            @PathVariable String from,
            @PathVariable String to,
            @PathVariable double quantity) {

        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        // ResponseEntity<CurrencyConversion> responseEntity = new
        // RestTemplate().getForEntity(
        ResponseEntity<CurrencyConversion> responseEntity = restTemplate.getForEntity(
                configuration.getUriExchange() + ":8000/currency-exchange/from/{from}/to/{to}",
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

}
