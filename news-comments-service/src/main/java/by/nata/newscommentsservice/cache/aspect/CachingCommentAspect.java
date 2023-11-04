package by.nata.newscommentsservice.cache.aspect;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.cache.algorithm.impl.LFUCache;
import by.nata.newscommentsservice.cache.algorithm.impl.LRUCache;
import by.nata.newscommentsservice.cache.aspect.type.CacheType;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
@Profile("dev")
public class CachingCommentAspect {

    private final Cache<String, CommentResponseDto> commentCache;

    @Autowired
    public CachingCommentAspect(@Value("${cache.algorithm}") String algorithm,
                                LFUCache<String, CommentResponseDto> lfuCache,
                                LRUCache<String, CommentResponseDto> lruCache) {
        if (CacheType.LFU.name().equalsIgnoreCase(algorithm)) {
            this.commentCache = lfuCache;
        } else {
            this.commentCache = lruCache;
        }
    }

    @Around("@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodGet)")
    public Object cacheMethodResult(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String key = "comment_" + args[0];

        CommentResponseDto cachedValue = commentCache.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }

        CommentResponseDto methodResult = (CommentResponseDto) joinPoint.proceed();
        Optional<CommentResponseDto> put = commentCache.put(key, methodResult);

        return put.get();
    }

}
