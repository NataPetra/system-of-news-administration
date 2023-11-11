package by.nata.userservice.config;

import by.nata.userservice.filter.AuthenticationJwtFilter;
import by.nata.userservice.filter.ExceptionFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationJwtFilter authJwtFilter;
    private final UserDetailsService userDetailsService;
    private final ExceptionFilter exceptionFilter;

    //TODO: check logic exceptionHandling
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint((req, resp, exception) -> exceptionFilter.sendUnauthorizedException(resp, exception))
                        .authenticationEntryPoint((req, resp, exception) -> exceptionFilter.sendForbiddenException(resp, exception))
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/api/v1/app/users/login", "/api/v1/app/users/").anonymous()
                        //.requestMatchers("/test/**").authenticated()
                        //.requestMatchers("/test/protected/subscriber").hasRole("SUBSCRIBER")
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .requestMatchers("/api/v1/app/users/register/**").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authJwtFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionFilter, AuthenticationJwtFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
