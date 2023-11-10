package by.nata.newscommentsservice.cache.algorithm.impl;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * The {@code LFUCache} class is an implementation of Least Frequently Used (LFU) cache.
 * It stores key-value pairs and evicts the least frequently used items when the cache reaches
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
@ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LFU")
public class LFUCache<K, V> implements Cache<K, V> {

    private final int maxSize;
    private final Map<K, V> cache;
    private final Map<K, Integer> frequency;
    private final PriorityQueue<K> priorityQueue;

    /**
     * Constructs an instance of {@code LFUCache} with the specified configuration properties.
     *
     * @param cacheProperties The configuration properties for the cache, including the maximum size.
     */
    @Autowired
    public LFUCache(CacheProperties cacheProperties) {
        this.maxSize = cacheProperties.getMaxSize();
        this.cache = new HashMap<>(maxSize);
        this.frequency = new HashMap<>(maxSize);
        this.priorityQueue = new PriorityQueue<>(maxSize, (k1, k2) -> {
            int freq1 = frequency.get(k1);
            int freq2 = frequency.get(k2);
            return Integer.compare(freq1, freq2);
        });
    }

    @Override
    public V get(K key) {
        if (cache.containsKey(key)) {
            int freq = frequency.get(key);
            frequency.put(key, freq + 1);
            priorityQueue.remove(key);
            priorityQueue.offer(key);
            return cache.get(key);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (cache.size() >= maxSize) {
            K leastFrequent = priorityQueue.poll();
            cache.remove(leastFrequent);
            frequency.remove(leastFrequent);
        }
        cache.put(key, value);
        frequency.put(key, 1);
        priorityQueue.offer(key);
        return value;
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
        frequency.remove(key);
        priorityQueue.remove(key);
    }
}
