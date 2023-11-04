package by.nata.newscommentsservice.cache.aspect;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.service.dto.CommentResponseDto;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Aspect
@Component
public class CachingCommentAspect {

    @Autowired
    @Qualifier(value = "LRUCache")
    private Cache<String, CommentResponseDto> commentCache;

    @Around("@annotation(by.nata.newscommentsservice.cache.annotation.CacheableMethodGet)")
    public Object cacheMethodResult(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("вызов cacheMethodResult(ProceedingJoinPoint joinPoint)");
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
