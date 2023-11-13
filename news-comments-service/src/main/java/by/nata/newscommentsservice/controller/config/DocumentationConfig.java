package by.nata.newscommentsservice.controller.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@code DocumentationConfig} is a Spring configuration class providing OpenAPI documentation
 * for the News service API. It includes security details for JWT token authentication.
 */
@Configuration
public class DocumentationConfig {

    /**
     * Provides the OpenAPI configuration for the News service API.
     *
     * @return An instance of OpenAPI with security details for JWT token authentication.
     */
    @Bean
    public OpenAPI newsOpenAPI() {
        final String securitySchemeName = "JWT Token";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info().title("News service API")
                        .description("News service application")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}

