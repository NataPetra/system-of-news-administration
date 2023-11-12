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

@Slf4j
@Component
public class JwtService {

    private static final String ROLE_KEY = "role";

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;
    @Value("${spring.security.jwt.expiration}")
    private long jwtExpiration;

    private SecretKey getSecretKey() {
        String base64EncodedSecretKey = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

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

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    public Collection<GrantedAuthority> getAuthorities(String token) {
        String role = getClaims(token).get(ROLE_KEY, String.class);
        return List.of(new SimpleGrantedAuthority(role));
    }

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

    public AppUserResponseDto getUserFromToken(String token) {
        Claims claims = getClaims(token);
        return AppUserResponseDto.builder()
                .withUsername(claims.getSubject())
                .withRole(claims.get(ROLE_KEY, String.class))
                .build();
    }
}
