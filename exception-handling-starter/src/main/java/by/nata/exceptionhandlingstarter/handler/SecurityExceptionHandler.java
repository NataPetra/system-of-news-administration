package by.nata.exceptionhandlingstarter.handler;

import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class SecurityExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    private static final String EUROPE_MINSK = "Europe/Minsk";

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) {
        sendUnauthorizedException(response, authException);
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException
    ) {
        sendForbiddenException(response, accessDeniedException);
    }

    public void sendUnauthorizedException(HttpServletResponse response, Exception e) {
        sendException(response, new ExceptionMessage(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)))
        );
    }

    public void sendForbiddenException(HttpServletResponse response, Exception e) {
        sendException(response, new ExceptionMessage(
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)))
        );
    }

    @SneakyThrows
    private void sendException(HttpServletResponse response, ExceptionMessage exceptionMessage) {
        response.setStatus(exceptionMessage.statusCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(mapper.writeValueAsString(exceptionMessage));
    }
}
