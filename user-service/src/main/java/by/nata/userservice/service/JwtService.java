package by.nata.userservice.service;

import by.nata.userservice.service.dto.AppUserResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * The {@code JwtService} class provides methods for generating, parsing, and validating JSON Web Tokens (JWT) used in authentication.
 */
@Slf4j
@Component
public class JwtService {

    private static final String ROLE_KEY = "role";

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    /**
     * Retrieves the secret key used for signing and verifying JWTs.
     *
     * @return SecretKey object.
     */
    private SecretKey getSecretKey() {
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Parses and retrieves the claims from a JWT.
     *
     * @param token The JWT to parse.
     * @return Claims object containing the parsed claims.
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Generates a JWT access token for the provided UserDetails.
     *
     * @param user UserDetails object.
     * @return Generated JWT access token.
     */
    public String generateAccessToken(UserDetails user) {
        String grantedAuthority = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElseThrow();

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .claim(ROLE_KEY, grantedAuthority)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * Retrieves the username from a JWT.
     *
     * @param token The JWT to extract the username from.
     * @return Username extracted from the JWT.
     */
    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * Retrieves the expiration date from a JWT.
     *
     * @param token The JWT to extract the expiration date from.
     * @return Expiration date extracted from the JWT.
     */
    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * Retrieves the authorities from a JWT.
     *
     * @param token The JWT to extract authorities from.
     * @return Collection of GrantedAuthority objects.
     */
    public Collection<GrantedAuthority> getAuthorities(String token) {
        String role = getClaims(token).get(ROLE_KEY, String.class);
        return List.of(new SimpleGrantedAuthority(role));
    }

    /**
     * Validates whether a JWT is valid.
     *
     * @param token The JWT to validate.
     * @return True if the JWT is valid, false otherwise.
     */
    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Bad JWT credentials - {}", e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves user details (username and role) from a JWT.
     *
     * @param token The JWT to extract user details from.
     * @return AppUserResponseDto containing username and role.
     */
    public AppUserResponseDto getUserFromToken(String token) {
        Claims claims = getClaims(token);
        return AppUserResponseDto.builder()
                .withUsername(claims.getSubject())
                .withRole(claims.get(ROLE_KEY, String.class))
                .build();
    }
}
