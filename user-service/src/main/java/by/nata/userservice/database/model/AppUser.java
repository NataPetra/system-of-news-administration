package by.nata.userservice.database.model;

import by.nata.userservice.database.enumeration.AppUserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Builder(setterPrefix = "with")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "app-user-service-db", name = "app_users")
public class AppUser implements Serializable {

    @EqualsAndHashCode.Exclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String username;

    @ToString.Exclude
    @Column(nullable = false, length = 72)
    private String password;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean blocked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private AppUserRole role;
}
