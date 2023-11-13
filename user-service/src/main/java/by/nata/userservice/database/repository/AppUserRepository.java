package by.nata.userservice.database.repository;

import by.nata.userservice.database.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * The {@code AppUserRepository} interface provides data access methods for the {@code AppUser} entity.
 * <p>
 * - Usage: Used to perform CRUD operations on the user entity in the application's database.
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
}
