package com.udemy.eazybytes.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayserverApplication.class, args);
    }

    //	For custom routing (endpoints)
    @Bean
    public RouteLocator eazyBanksRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p.path("/eazybanks/accounts/**")
                        .filters(f -> f.rewritePath("/eazybanks/accounts/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("x-response-time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("accountsCircuitBreaker")
                                        .setFallbackUri("forward:/contact-support")))
                        .uri("lb://ACCOUNTS"))
                .route(p -> p.path("/eazybanks/loans/**")
                        .filters(f -> f.rewritePath("/eazybanks/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("x-response-time", LocalDateTime.now().toString()))
                        .uri("lb://LOANS"))
                .route(p -> p.path("/eazybanks/cards/**")
                        .filters(f -> f.rewritePath("/eazybanks/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("x-response-time", LocalDateTime.now().toString()))
                        .uri("lb://CARDS")).build();
    }
}
