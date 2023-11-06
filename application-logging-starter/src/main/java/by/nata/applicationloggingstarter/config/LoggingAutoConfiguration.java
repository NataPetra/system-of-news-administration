package by.nata.applicationloggingstarter.config;

import by.nata.applicationloggingstarter.aspect.ControllersLogging;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
@ConditionalOnMissingBean(ControllersLogging.class)
@ConditionalOnProperty(prefix = "starter.logger", name = "include", havingValue = "true")
public class LoggingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ControllersLogging commonPointcuts() {
        return new ControllersLogging();
    }
}
