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
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "exception.handling")
public class ExceptionHandlerProperties {

    private boolean include;
}
