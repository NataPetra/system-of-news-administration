package by.nata.userservice.service;

import by.nata.userservice.database.enumeration.AppUserRole;
import by.nata.userservice.database.model.AppUser;
import by.nata.userservice.database.repository.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @InjectMocks
    private AppUserService appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Test
    void loadUserByUsername() {
        String username = "testUser";
        AppUser appUser = getAppUser();

        doReturn(Optional.of(appUser))
                .when(appUserRepository).findByUsername(username);

        UserDetails userDetails = appUserService.loadUserByUsername(username);

        assertThat(userDetails)
                .hasFieldOrPropertyWithValue("username", appUser.getUsername())
                .hasFieldOrPropertyWithValue("password", appUser.getPassword())
                .hasFieldOrPropertyWithValue("accountNonExpired", appUser.getUnblocked())
                .hasFieldOrPropertyWithValue("accountNonLocked", appUser.getUnblocked())
                .hasFieldOrPropertyWithValue("credentialsNonExpired", appUser.getUnblocked())
                .hasFieldOrPropertyWithValue("enabled", appUser.getUnblocked());

        Collection<? extends GrantedAuthority> expectedAuthorities =
                AuthorityUtils.createAuthorityList("ROLE_" + appUser.getRole());

        assertThat(AuthorityUtils.authorityListToSet(userDetails.getAuthorities()))
                .containsExactlyInAnyOrderElementsOf(AuthorityUtils.authorityListToSet(expectedAuthorities));
    }

    @Test
    void loadUserByUsernameThrowsUsernameNotFoundException() {
        String username = "nonExistentUser";

        doReturn(Optional.empty())
                .when(appUserRepository).findByUsername(username);

        assertThatThrownBy(() -> appUserService.loadUserByUsername(username))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(username);
    }

    private AppUser getAppUser() {
        AppUser appUser = new AppUser();
        appUser.setUsername("testUser");
        appUser.setPassword("password");
        appUser.setUnblocked(true);
        appUser.setRole(AppUserRole.ADMIN);
        return appUser;
    }
}