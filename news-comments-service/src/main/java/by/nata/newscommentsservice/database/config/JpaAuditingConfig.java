package by.nata.newscommentsservice.database.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The {@code JpaAuditingConfig} class is a Spring configuration class that enables JPA (Java Persistence
 * API) entity auditing. It allows automatic tracking of entity creation and modification timestamps.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
