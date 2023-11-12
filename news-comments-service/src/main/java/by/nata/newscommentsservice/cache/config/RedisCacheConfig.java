package by.nata.newscommentsservice.cache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

/**
 * The {@code RedisCacheConfig} class is a Spring configuration class for setting up caching with
 * Redis. It enables caching and configures the default cache settings when the "prod" profile is active.
 *
 * <p>Bean:</p>
 * <p>- The default cache configuration includes a 5-minute time-to-live (TTL) for cache entries and
 *   disables caching of null values.</p>
 */
@Configuration
@EnableCaching
@Profile("prod")
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues();
    }
}
