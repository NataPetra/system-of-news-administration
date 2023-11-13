package by.nata.userservice.config;

import by.nata.exceptionhandlingstarter.handler.SecurityExceptionHandler;
import by.nata.userservice.filter.AuthenticationJwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * The {@code SecurityConfig} class configures security settings for the application.
 * <p>
 * Beans:
 * <p>- SecurityFilterChain: Configures the security filter chain.
 * <p>- PasswordEncoder: Provides BCrypt password encoding.
 * <p>- AuthenticationProvider: Configures a custom authentication provider.
 * <p>- AuthenticationManager: Configures the authentication manager.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile({"dev", "prod"})
public class SecurityConfig {

    private final AuthenticationJwtFilter authJwtFilter;
    private final UserDetailsService userDetailsService;
    private final SecurityExceptionHandler securityExceptionHandler;

    /**
     * Configures the security filter chain.
     *
     * @param http HttpSecurity object for configuring security settings.
     * @return SecurityFilterChain object.
     * @throws Exception if an exception occurs while configuring security.
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
                        .requestMatchers(HttpMethod.POST, "/api/v1/app/users/login").anonymous()
                        .requestMatchers(HttpMethod.GET, "/api/v1/app/users/validate").permitAll()
                        .requestMatchers("/api/v1/app/users/register/**").permitAll()
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Provides BCrypt password encoding.
     *
     * @return PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures a custom authentication provider.
     *
     * @return AuthenticationProvider bean.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    /**
     * Configures the authentication manager.
     *
     * @param config AuthenticationConfiguration object.
     * @return AuthenticationManager bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
