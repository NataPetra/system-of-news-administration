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

/**
 * The {@code SecurityExceptionHandler} class is a Spring component that implements both
 * {@code AccessDeniedHandler} and {@code AuthenticationEntryPoint}. It is designed to handle
 * security-related exceptions, specifically unauthorized and forbidden access scenarios.
 *
 * </p>Usage:</p>
 * </p>- It is typically configured in the security configuration of a Spring application to handle
 *   exceptions thrown during the authentication process and access control.
 * </p>- Developers can customize the behavior by extending this class and overriding its methods.
 */
@Component
public class SecurityExceptionHandler implements AccessDeniedHandler, AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper mapper;

    private static final String EUROPE_MINSK = "Europe/Minsk";

    /**
     * Handles the commencement of an authentication failure by sending an HTTP 401 Unauthorized
     * response with a JSON payload containing error details.
     *
     * @param request       The HttpServletRequest.
     * @param response      The HttpServletResponse to which the error response will be written.
     * @param authException The AuthenticationException that caused the failure.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) {
        sendUnauthorizedException(response, authException);
    }

    /**
     * Handles the situation where access is denied, sending an HTTP 403 Forbidden response
     * with a JSON payload containing error details.
     *
     * @param request              The HttpServletRequest.
     * @param response             The HttpServletResponse to which the error response will be written.
     * @param accessDeniedException The AccessDeniedException that caused the access denial.
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException
    ) {
        sendForbiddenException(response, accessDeniedException);
    }

    /**
     * Sends an HTTP 401 Unauthorized response with a JSON payload containing error details.
     *
     * @param response The HttpServletResponse to which the error response will be written.
     * @param e        The Exception that caused the unauthorized access.
     */
    public void sendForbiddenException(HttpServletResponse response, Exception e) {
        sendException(response, new ExceptionMessage(
                HttpStatus.FORBIDDEN.value(),
                e.getMessage(),
                ZonedDateTime.now().withZoneSameInstant(ZoneId.of(EUROPE_MINSK)))
        );
    }

    /**
     * Sends an HTTP 403 Forbidden response with a JSON payload containing error details.
     *
     * @param response The HttpServletResponse to which the error response will be written.
     * @param e        The Exception that caused the unauthorized access.
     */
    public void sendUnauthorizedException(HttpServletResponse response, Exception e) {
        sendException(response, new ExceptionMessage(
                HttpStatus.UNAUTHORIZED.value(),
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
