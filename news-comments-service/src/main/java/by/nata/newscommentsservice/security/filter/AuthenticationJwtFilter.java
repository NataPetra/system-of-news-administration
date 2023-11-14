package by.nata.newscommentsservice.security.filter;

import by.nata.newscommentsservice.security.client.UserClienProvider;
import by.nata.newscommentsservice.security.dto.AppUserResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * The {@code AuthenticationJwtFilter} class is a Spring component implementing a custom filter for handling JWT authentication.
 * <p>
 * Dependencies:
 * - UserClient: Feign client for interacting with the User Service to validate and retrieve user details.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationJwtFilter extends OncePerRequestFilter {

    private final UserClienProvider userClienProvider;

    /**
     * Filters the incoming HTTP requests to handle JWT authentication.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        AppUserResponseDto userFromUserService = userClienProvider.getUser(header);

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userFromUserService.role()));
        User userDetails = new User(
                userFromUserService.username(),
                " ",
                authorities);
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
