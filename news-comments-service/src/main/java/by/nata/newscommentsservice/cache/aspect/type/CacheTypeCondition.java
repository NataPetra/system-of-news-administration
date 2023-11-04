package by.nata.newscommentsservice.cache.aspect.type;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class CacheTypeCondition implements Condition {

    @Value("${cache.algorithm}")
    private String algorithm;

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        if (algorithm != null) {
            CacheType cacheType = CacheType.valueOf(algorithm);
            if (cacheType.equals(CacheType.LRU)) {
                return true;
//            } else if (cacheType.equals(CacheType.LFU)) {
//                return true;
            }
        }
        return false;
    }
}
