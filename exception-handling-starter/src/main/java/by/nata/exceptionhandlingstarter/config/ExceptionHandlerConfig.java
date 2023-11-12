package by.nata.exceptionhandlingstarter.config;

import by.nata.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.nata.exceptionhandlingstarter.handler.SecurityExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The `ExceptionHandlerConfig` class is a Spring configuration class that enables
 * the automatic configuration of exception handling using the `GlobalExceptionHandlerAdvice` and
 * `SecurityExceptionHandler`.
 * It is conditional and can be enabled based on the properties specified in the application.yml.
 * <p>
 * Usage:
 * - Include this class in your Spring application to enable exception handling with
 * the `GlobalExceptionHandlerAdvice` and `SecurityExceptionHandler`.
 * <p>
 * Configuration Properties:
 * - `exception.handling.include`: A property that controls whether the exception handling
 * should be enabled (default is "true").
 * <p>
 *
 * @see by.nata.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice
 * @see by.nata.exceptionhandlingstarter.handler.SecurityExceptionHandler
 */
@Configuration
@ConditionalOnProperty(prefix = "exception.handling", name = "include", havingValue = "true")
public class ExceptionHandlerConfig {

    /**
     * Creates a bean for the `GlobalExceptionHandlerAdvice` if not already defined.
     *
     * @return An instance of `GlobalExceptionHandlerAdvice` for handling exceptions.
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandlerAdvice globalHandlerAdvice() {
        return new GlobalExceptionHandlerAdvice();
    }

    /**
     * Creates a bean for the `SecurityExceptionHandler` if not already defined.
     *
     * @return An instance of `SecurityExceptionHandler` for handling security exceptions.
     */
    @Bean
    @ConditionalOnMissingBean
    public SecurityExceptionHandler securityExceptionHandler() {
        return new SecurityExceptionHandler();
    }
}
