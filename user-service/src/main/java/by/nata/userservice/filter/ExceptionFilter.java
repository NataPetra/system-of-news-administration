package by.nata.userservice.filter;

import by.nata.exceptionhandlingstarter.exception.ExceptionMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
@RequiredArgsConstructor
public class ExceptionFilter extends OncePerRequestFilter implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    private static final String EUROPE_MINSK = "Europe/Minsk";

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       org.springframework.security.access.AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        sendForbiddenException(response, accessDeniedException);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException e) {
            sendForbiddenException(response, e);
        } catch (AuthenticationException e) {
            sendUnauthorizedException(response, e);
        }
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
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(exceptionMessage));
    }
}
