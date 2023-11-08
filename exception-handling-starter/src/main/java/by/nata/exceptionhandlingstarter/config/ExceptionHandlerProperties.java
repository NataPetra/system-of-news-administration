package by.nata.exceptionhandlingstarter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>The <code>ExceptionHandlerProperties</code> class is a configuration properties class that
 * allows customization of the exception handling behavior in the application.
 * It is intended to be used with the Spring Boot <code>@ConfigurationProperties</code> annotation.</p>
 *
 * <p>Usage:</p>
 * <p>- Create an instance of this class to customize exception handling behavior through
 *   application properties.</p>
 * <p>- Use the <code>exception.handling.include</code> property to control whether exception
 *   handling should be enabled (default is "true").</p>
 *
 * <p>Configuration Properties:</p>
 * <p>- <code>include</code>: A boolean property that controls whether exception handling should be
 *   enabled (default is "true").</p>
 *
 * <p>Dependencies:</p>
 * <p>- {@link by.nata.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice}:
 *   The global exception handler advice responsible for handling exceptions.</p>
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "exception.handling")
public class ExceptionHandlerProperties {

    /**
     * Get whether exception handling should be enabled.
     */
    private boolean include;
}
