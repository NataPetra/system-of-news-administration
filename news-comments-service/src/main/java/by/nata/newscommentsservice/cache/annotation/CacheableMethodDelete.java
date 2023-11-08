package by.nata.newscommentsservice.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The {@code CacheableMethodDelete} annotation is a custom annotation used to mark methods
 * that perform cache delete operations. Methods annotated with {@code @CacheableMethodDelete}
 * are typically part of a caching strategy and are responsible for removing data from the cache.
 *
 * <p>Usage:</p>
 * <p>- Annotate methods that should be executed to delete cached data with this annotation.</p>
 * <p>- Typically used in conjunction with caching mechanisms to invalidate or remove cached data.</p>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableMethodDelete {
}
