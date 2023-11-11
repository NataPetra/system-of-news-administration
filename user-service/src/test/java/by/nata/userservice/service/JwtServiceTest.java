package by.nata.userservice.service;

import by.nata.userservice.database.enumeration.AppUserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JwtService.class)
@TestPropertySource(properties = {
        "spring.security.jwt.secret=2442264fs8478454584455vsvsv48782F413F4428472B4B6250655368566D5970337336d",
        "spring.security.jwt.expiration=3600000"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void generateAccessToken() {
        UserDetails userDetails = new User("testUser", "password", List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.ADMIN.name())));
        String token = jwtService.generateAccessToken(userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
        assertEquals("testUser", jwtService.getUsername(token));

        Collection<GrantedAuthority> authorities = jwtService.getAuthorities(token);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void getUsername() {
        UserDetails userDetails = new User("testUser", "password", List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.SUBSCRIBER.name())));
        String token = jwtService.generateAccessToken(userDetails);

        String username = jwtService.getUsername(token);

        assertEquals("testUser", username);
    }

    @Test
    void getExpirationDate() {
        UserDetails userDetails = new User("testUser", "password", List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.SUBSCRIBER.name())));
        String token = jwtService.generateAccessToken(userDetails);

        Date expirationDate = jwtService.getExpirationDate(token);

        assertNotNull(expirationDate);
    }

    @Test
    void getAuthorities() {
        UserDetails userDetails = new User("testUser", "password", List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.JOURNALIST.name())));
        String token = jwtService.generateAccessToken(userDetails);

        Collection<GrantedAuthority> authorities = jwtService.getAuthorities(token);

        assertEquals(1, authorities.size());
        assertEquals("ROLE_JOURNALIST", authorities.iterator().next().getAuthority());
    }

    @Test
    void isValidWithValidToken() {
        UserDetails userDetails = new User("testUser", "password", List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.SUBSCRIBER.name())));
        String token = jwtService.generateAccessToken(userDetails);

        assertTrue(jwtService.isValid(token));
    }

    @Test
    void isValidWithInvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtService.isValid(invalidToken));
    }
}