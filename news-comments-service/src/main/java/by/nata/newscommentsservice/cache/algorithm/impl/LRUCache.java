package by.nata.newscommentsservice.cache.algorithm.impl;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The {@code LRUCache} class is an implementation of Least Recently Used (LRU) cache.
 * It stores key-value pairs and evicts the least recently used items when the cache reaches
 * its maximum size. This class is annotated with {@code @Component} to be managed as a Spring
 * bean and is configured with prototype scope to create a new instance for each request.
 *
 * <p>Dependencies:</p>
 * <p>- {@link ConditionalOnProperty}: A Spring Boot annotation to conditionally enable the bean
 *   based on the value of the "cache.algorithm" property.</p>
 *
 * @param <K> The type of keys in the cache.
 * @param <V> The type of values in the cache.
 */
@Component
@Scope("prototype")
@ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LRU")
public class LRUCache<K, V> implements Cache<K, V> {

    private final int maxSize;
    private final LinkedHashMap<K, V> cache;

    /**
     * Constructs an instance of {@code LRUCache} with the specified configuration properties.
     *
     * @param cacheProperties The configuration properties for the cache, including the maximum size.
     */
    @Autowired
    public LRUCache(CacheProperties cacheProperties) {
        this.maxSize = cacheProperties.getMaxSize();
        this.cache = new LinkedHashMap<>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > maxSize;
            }
        };
    }

    @Override
    public V get(K key) {
        return cache.get(key);
    }

    @Override
    public V put(K key, V value) {
        cache.put(key, value);
        return value;
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
    }
}
