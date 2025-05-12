package com.currency.microservices.currency_conversion_service.model;

public record PropConfig(
                String envProfil,
                String uriConversion,
                String uriExchange,
                String uriApiGetway,
                String uriSpringCloud,
                String activeSp,
                String configProfile,
                String urlSpring) {
}