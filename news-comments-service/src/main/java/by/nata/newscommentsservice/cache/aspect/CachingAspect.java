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

@Aspect
@Component
@Profile({"dev", "aspect"})
public class CachingAspect {

    private final Cache<String, Object> cache;

    @Autowired
    public CachingAspect(Cache<String, Object> cache) {
        this.cache = cache;
    }

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

    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodPut)")
    public Object cacheMethodPutResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object methodResult = joinPoint.proceed();

        Field idField = methodResult.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        Long idValue = (Long) idField.get(methodResult);

        String key = getPrefix(joinPoint) + idValue;

        return cache.put(key, methodResult);
    }

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
