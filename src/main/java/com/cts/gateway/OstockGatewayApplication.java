package com.cts.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OstockGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(OstockGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                      .route(ps -> ps.path("/license/**")
                                     .filters(fs -> fs.rewritePath("/license/(?<path>.*)", "/${path}"))
                                     .uri("lb://licensing-service"))
                      .build();
    }

}
