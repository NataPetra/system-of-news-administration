package by.nata.apigateway.roures;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

@Configuration
@OpenAPIDefinition
public class RoutesConfig {
    @Bean
    @CrossOrigin
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("user-service", r -> r.path(
                                "/api/v1/app/users/{segment}",
                                "/api/v1/app/users",
                                "/user-service/v3/api-docs")
                        .uri("lb://user-service"))
                .route("news-service", r -> r.path(
                                "/api/v1/app/news/{segment}",
                                "/api/v1/app/news",
                                "/api/v1/app/comments/{segment}",
                                "/api/v1/app/comments",
                                "/news-service/v3/api-docs")
                        .uri("lb://news-service"))
                .build();
    }
}
