package by.nata.applicationloggingstarter.config;

import by.nata.applicationloggingstarter.aspect.ControllersLogging;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The `LoggingAutoConfiguration` class is a Spring Boot auto-configuration class that
 * configures logging aspects for controller methods based on the presence of the
 * `by.nata.applicationloggingstarter.annotation.MethodLog` annotation.
 * <p>
 * It enables the logging of method invocations in controllers and allows for
 * customizable configuration through the `LoggingProperties` class.
 * <p>
 * Usage:
 * - Include this class in your Spring Boot application to enable automatic
 *   configuration of the `ControllersLogging` aspect.
 * - Use the `LoggingProperties` class to customize logging behavior.
 * <p>
 * Configuration properties:
 * - `starter.logger.include`: A property that controls whether the logging aspect
 *   should be enabled (default is "true").
 * <p>
 * @see by.nata.applicationloggingstarter.aspect.ControllersLogging
 * @see LoggingProperties
 */
@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnMissingBean(ControllersLogging.class)
@ConditionalOnProperty(prefix = "starter.logger", name = "include", havingValue = "true")
public class LoggingAutoConfiguration {

    /**
     * Creates a bean for the `ControllersLogging` aspect if not already defined.
     *
     * @return An instance of `ControllersLogging` aspect.
     */
    @Bean
    @ConditionalOnMissingBean
    public ControllersLogging commonPointcuts() {
        return new ControllersLogging();
    }
}
