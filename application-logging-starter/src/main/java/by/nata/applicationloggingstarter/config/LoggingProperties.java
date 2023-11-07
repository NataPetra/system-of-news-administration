package by.nata.applicationloggingstarter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "starter.logger")
public class LoggingProperties {

    private boolean include;
}
