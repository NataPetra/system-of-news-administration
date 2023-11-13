package by.nata.userservice.filter;

import by.nata.userservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * The {@code AuthenticationJwtFilter} class is a filter that intercepts incoming requests,
 * validates the JWT token, and sets up the authentication context if the token is valid.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationJwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        if (!jwtService.isValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        User userDetails = new User(
                jwtService.getUsername(token),
                jwtService.getUsername(token),
                jwtService.getAuthorities(token));
        userDetails.eraseCredentials();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
