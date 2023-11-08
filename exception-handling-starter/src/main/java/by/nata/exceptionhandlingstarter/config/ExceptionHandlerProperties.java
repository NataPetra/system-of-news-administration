package by.nata.exceptionhandlingstarter.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "exception.handling")
public class ExceptionHandlerProperties {

    private boolean include;
}
