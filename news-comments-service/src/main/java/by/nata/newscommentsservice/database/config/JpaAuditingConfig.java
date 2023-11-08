package by.nata.newscommentsservice.database.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * The {@code JpaAuditingConfig} class is a Spring configuration class that enables JPA (Java Persistence
 * API) entity auditing. It allows automatic tracking of entity creation and modification timestamps.
 *
 * <p>Usage:</p>
 * <p>- Include this class in your Spring application to enable JPA entity auditing. When entities are
 *   created or modified, their associated audit fields (e.g., creation and modification timestamps)
 *   will be automatically updated.</p>
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
