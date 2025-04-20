package com.udemy.eazybytes.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import org.springframework.cloud.client.circuitbreaker.Customizer;
import reactor.core.publisher.Mono;

import java.time.Duration;
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
                                        .setFallbackUri("forward:/contact-support"))
                        ).uri("lb://ACCOUNTS"))
                .route(p -> p.path("/eazybanks/loans/**")
                        .filters(f -> f.rewritePath("/eazybanks/loans/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("x-response-time", LocalDateTime.now().toString())
                                .retry(retryCongif -> retryCongif.setRetries(3)     //  Retry service pattern
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true))
                        ).uri("lb://LOANS"))
                .route(p -> p.path("/eazybanks/cards/**")
                        .filters(f -> f.rewritePath("/eazybanks/cards/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("x-response-time", LocalDateTime.now().toString())
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                        ).uri("lb://CARDS")).build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()).build());
    }

    @Bean
    public RedisRateLimiter redisRateLimiter(){
        return new RedisRateLimiter(1,1,1);
    }

    @Bean
    KeyResolver userKeyResolver(){
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(""))
                .defaultIfEmpty("anonymous");
    }
}
