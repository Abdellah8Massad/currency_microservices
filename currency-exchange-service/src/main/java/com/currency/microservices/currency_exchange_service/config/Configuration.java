package com.currency.microservices.currency_exchange_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("liste-services-uri")
public class Configuration {
    private String envProfil;
    private String uriConversion;
    private String uriExchange;
    private String uriApiGetway;
    private String uriSpringCloud;

    public String getEnvProfil() {
        return envProfil;
    }

    public void setEnvProfil(String envProfil) {
        this.envProfil = envProfil;
    }

    public String getUriConversion() {
        return uriConversion;
    }

    public void setUriConversion(String uriConversion) {
        this.uriConversion = uriConversion;
    }

    public String getUriExchange() {
        return uriExchange;
    }

    public void setUriExchange(String uriExchange) {
        this.uriExchange = uriExchange;
    }

    public String getUriApiGetway() {
        return uriApiGetway;
    }

    public void setUriApiGetway(String uriApiGetway) {
        this.uriApiGetway = uriApiGetway;
    }

    public String getUriSpringCloud() {
        return uriSpringCloud;
    }

    public void setUriSpringCloud(String uriSpringCloud) {
        this.uriSpringCloud = uriSpringCloud;
    }
}
