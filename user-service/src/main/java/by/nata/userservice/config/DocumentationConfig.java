package by.nata.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code DocumentationConfig} class configures OpenAPI documentation for the User Service API.
 * <p>
 * Configuration:
 * - OpenAPI bean named 'userOpenAPI'.
 */
@Configuration
public class DocumentationConfig {

    /**
     * Configures OpenAPI documentation for the User Service API.
     *
     * @return OpenAPI configuration.
     */
    @Bean
    public OpenAPI userOpenAPI() {
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
                .info(new Info().title("User service API")
                        .description("User service application")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
