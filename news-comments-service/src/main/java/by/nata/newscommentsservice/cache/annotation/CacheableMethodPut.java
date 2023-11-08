package by.nata.newscommentsservice.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code CacheableMethodPut} annotation is a custom annotation used to mark methods
 * that perform cache update or insertion operations. Methods annotated with {@code @CacheableMethodPut}
 * are typically part of a caching strategy and are responsible for storing or updating data in the cache.
 *
 * <p>Usage:</p>
 * <p>- Annotate methods that should be executed to store or update data in the cache with this annotation.</p>
 * <p>- Typically used in conjunction with caching mechanisms to efficiently manage cached data.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableMethodPut {
}
