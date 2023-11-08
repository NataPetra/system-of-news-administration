package by.nata.newscommentsservice.cache.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * The {@code CacheProperties} class is a configuration properties class that allows customization
 * of cache-related behavior, such as the maximum size and algorithm for caching.
 *
 * <p>Usage:</p>
 * <p>- Create an instance of this class to customize cache behavior through application properties.</p>
 * <p>- Use the "cache.maxSize" property to set the maximum size of the cache.</p>
 * <p>- Use the "cache.algorithm" property to specify the caching algorithm (e.g., "LRU" or "LFU").</p>
 *
 * <p>Configuration Properties:</p>
 * <p>- "maxSize": An integer property that defines the maximum size of the cache.</p>
 * <p>- "algorithm": A string property that specifies the caching algorithm (e.g., "LRU" or "LFU").</p>
 */
@Component
@ConfigurationProperties("cache")
public class CacheProperties {

    private int maxSize;
    private String algorithm;

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }
}
