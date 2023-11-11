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

    public static final String TEST_USER = "testUser";
    public static final String PASSWORD = "password";

    UserDetails userDetailsSubscriber = new User(
            TEST_USER,
            PASSWORD,
            List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.SUBSCRIBER.name()))
    );

    @Test
    void generateAccessToken() {
        UserDetails userDetails = new User(
                TEST_USER,
                PASSWORD,
                List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.ADMIN.name()))
        );
        String token = jwtService.generateAccessToken(userDetails);

        assertNotNull(token);
        assertTrue(jwtService.isValid(token));
        assertEquals(TEST_USER, jwtService.getUsername(token));

        Collection<GrantedAuthority> authorities = jwtService.getAuthorities(token);
        assertEquals(1, authorities.size());
        assertEquals("ROLE_ADMIN", authorities.iterator().next().getAuthority());
    }

    @Test
    void getUsername() {
        String token = jwtService.generateAccessToken(userDetailsSubscriber);

        String username = jwtService.getUsername(token);

        assertEquals(TEST_USER, username);
    }

    @Test
    void getExpirationDate() {
        String token = jwtService.generateAccessToken(userDetailsSubscriber);

        Date expirationDate = jwtService.getExpirationDate(token);

        assertNotNull(expirationDate);
    }

    @Test
    void getAuthorities() {
        UserDetails userDetails = new User(
                TEST_USER,
                PASSWORD,
                List.of(new SimpleGrantedAuthority("ROLE_" + AppUserRole.JOURNALIST.name())));
        String token = jwtService.generateAccessToken(userDetails);

        Collection<GrantedAuthority> authorities = jwtService.getAuthorities(token);

        assertEquals(1, authorities.size());
        assertEquals("ROLE_JOURNALIST", authorities.iterator().next().getAuthority());
    }

    @Test
    void isValidWithValidToken() {
        String token = jwtService.generateAccessToken(userDetailsSubscriber);

        assertTrue(jwtService.isValid(token));
    }

    @Test
    void isValidWithInvalidToken() {
        String invalidToken = "invalidToken";

        assertFalse(jwtService.isValid(invalidToken));
    }
}