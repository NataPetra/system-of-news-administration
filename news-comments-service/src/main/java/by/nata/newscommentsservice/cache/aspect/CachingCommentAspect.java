package by.nata.newscommentsservice.cache.aspect;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile({"dev", "aspect"})
public class CachingCommentAspect {

    private final Cache<String, CommentResponseDto> commentCache;

    @Autowired
    public CachingCommentAspect(Cache<String, CommentResponseDto> commentCache) {
        this.commentCache = commentCache;
    }

    public static final String PREFIX = "comment_";

    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodGet)")
    public CommentResponseDto cacheMethodGetResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String key = PREFIX + args[0];
        CommentResponseDto cachedValue = commentCache.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }
        CommentResponseDto methodResult = (CommentResponseDto) joinPoint.proceed();
        return commentCache.put(key, methodResult);
    }

    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodPut)")
    public CommentResponseDto cacheMethodPutResult(ProceedingJoinPoint joinPoint) throws Throwable {
        CommentResponseDto methodResult = (CommentResponseDto) joinPoint.proceed();
        String key = PREFIX + methodResult.id();
        return commentCache.put(key, methodResult);
    }

    @Around(value = "@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodDelete)")
    public void cacheMethodDeleteResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String key = PREFIX + args[0];
        joinPoint.proceed();
        commentCache.delete(key);
    }
}
