package by.nata.newscommentsservice.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code CacheableMethodGet} annotation is a custom annotation used to mark methods
 * that perform cache retrieval operations. Methods annotated with {@code @CacheableMethodGet}
 * are typically part of a caching strategy and are responsible for retrieving data from the cache.
 *
 * <p>Usage:</p>
 * <p>- Annotate methods that should be executed to retrieve data from the cache with this annotation.</p>
 * <p>- Typically used in conjunction with caching mechanisms to fetch cached data efficiently.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableMethodGet {
}
