package by.nata.newscommentsservice.security.config;

import by.nata.exceptionhandlingstarter.handler.SecurityExceptionHandler;
import by.nata.newscommentsservice.security.filter.AuthenticationJwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The {@code SecurityConfig} class configures security settings for the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String SUBSCRIBER = "SUBSCRIBER";
    private final AuthenticationJwtFilter authJwtFilter;
    private final SecurityExceptionHandler securityExceptionHandler;
    public static final String ADMIN = "ADMIN";
    public static final String JOURNALIST = "JOURNALIST";

    /**
     * Configures the security filter chain for HTTP security settings.
     *
     * @param http The HttpSecurity instance to be configured.
     * @return SecurityFilterChain with the configured settings.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(xh -> xh
                        .accessDeniedHandler(securityExceptionHandler)
                        .authenticationEntryPoint(securityExceptionHandler))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/v1/app/news/").hasAnyRole(JOURNALIST, ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/app/news/{id}").hasAnyRole(JOURNALIST, ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/app/news/{id}").hasAnyRole(JOURNALIST, ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/app/comments/").hasAnyRole(SUBSCRIBER, ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/app/comments/{id}").hasAnyRole(SUBSCRIBER, ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/app/comments/{id}").hasAnyRole(SUBSCRIBER, ADMIN)
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
