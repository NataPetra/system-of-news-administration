package by.nata.applicationloggingstarter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The `LoggingProperties` class is a configuration properties class that allows
 * customization of the logging behavior for the auto-configured `ControllersLogging`
 * aspect. It is intended to be used with the Spring Boot `@ConfigurationProperties`
 * annotation.
 * <p>
 * Usage:
 * - Create an instance of this class to customize logging behavior through
 *   application properties.
 * - Use the `starter.logger.include` property to control whether the `ControllersLogging`
 *   aspect should be enabled (default is "true").
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "starter.logger")
public class LoggingProperties {

    private boolean include;
}
