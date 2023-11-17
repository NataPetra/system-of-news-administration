package by.nata.apigateway.roures;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Configures the routes for the API Gateway using Spring Cloud Gateway.
 *
 * <p>Additionally, the "/user-service/v3/api-docs" and "/news-service/v3/api-docs" paths are configured for Swagger documentation.
 *  * These paths allow access to the Swagger definitions of the "user-service" and "news-service" respectively.<p/>
 */
@Configuration
@OpenAPIDefinition
public class RoutesConfig {

    /**
     * Configures RouteLocator to define the routing rules for different service endpoints.
     *
     * @param builder RouteLocatorBuilder to create and configure routes for the API Gateway.
     * @return RouteLocator with defined routes for the API Gateway.
     */
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
