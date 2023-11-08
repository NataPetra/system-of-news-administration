package by.nata.exceptionhandlingstarter.config;

import by.nata.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "exception.handling", name = "include", havingValue = "true")
public class ExceptionHandlerConfig {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandlerAdvice globalHandlerAdvice() {
        return new GlobalExceptionHandlerAdvice();
    }
}
