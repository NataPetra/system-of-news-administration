package by.nata.newscommentsservice.cache.aspect;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import by.nata.newscommentsservice.service.dto.NewsResponseDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * The {@code CachingAspect} class is an aspect that provides caching functionality for methods
 * marked with custom cache-related annotations. It allows caching of method results, retrieval
 * of cached data, and deletion of cached data based on annotations.
 *
 * <p>Usage:</p>
 * <p>- Include this class in your Spring application to enable caching for methods marked with
 *   custom cache annotations like {@code @CacheableMethodGet}, {@code @CacheableMethodPut}, and
 *   {@code @CacheableMethodDelete}.</p>
 *
 * <p>Dependencies:</p>
 * <p>- {@link Cache}: An interface that defines the caching behavior, including methods for
 *   getting, putting, and deleting cached data.</p>
 *
 * <p>Custom Annotations:</p>
 * <p>- {@link by.nata.newscommentsservice.cache.annotation.CacheableMethodGet}: Marks methods that
 *   retrieve data and can be cached.</p>
 * <p>- {@link by.nata.newscommentsservice.cache.annotation.CacheableMethodPut}: Marks methods that
 *   update or insert data into the cache.</p>
 * <p>- {@link by.nata.newscommentsservice.cache.annotation.CacheableMethodDelete}: Marks methods
 *   that delete data from the cache.</p>
 */
@Aspect
@Component
@Profile({"dev", "aspect"})
public class CachingAspect {

    private final Cache<String, Object> cache;

    /**
     * Constructs an instance of {@code CachingAspect} with the specified cache.
     *
     * @param cache The cache implementation used for storing and managing cached data.
     */
    @Autowired
    public CachingAspect(Cache<String, Object> cache) {
        this.cache = cache;
    }

    /**
     * Caches the result of methods marked with {@code @CacheableMethodGet}.
     *
     * @param joinPoint The method being executed and its arguments.
     * @return The cached result or the method's result if not cached.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodGet)")
    public Object cacheMethodGetResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String key = getPrefix(joinPoint) + args[0];

        Object cachedValue = cache.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }
        Object methodResult = joinPoint.proceed();
        return cache.put(key, methodResult);
    }

    /**
     * Caches the result of methods marked with {@code @CacheableMethodPut}.
     *
     * @param joinPoint The method being executed and its arguments.
     * @return The cached result.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodPut)")
    public Object cacheMethodPutResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object methodResult = joinPoint.proceed();

        Field idField = methodResult.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        Long idValue = (Long) idField.get(methodResult);

        String key = getPrefix(joinPoint) + idValue;

        return cache.put(key, methodResult);
    }

    /**
     * Deletes cached data related to methods marked with {@code @CacheableMethodDelete}.
     *
     * @param joinPoint The method being executed and its arguments.
     * @throws Throwable If an error occurs during method execution.
     */
    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodDelete)")
    public void cacheMethodDeleteResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        String key = getPrefix(joinPoint) + args[0];
        joinPoint.proceed();
        cache.delete(key);
    }

    private static String getPrefix(ProceedingJoinPoint joinPoint) {
        String str;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class returnType = signature.getReturnType();

        if (returnType.isAssignableFrom(NewsResponseDto.class)) {
            str = "news_";
        } else if (returnType.isAssignableFrom(CommentResponseDto.class)) {
            str = "comment_";
        } else {
            str = "other_";
        }
        return str;
    }
}
