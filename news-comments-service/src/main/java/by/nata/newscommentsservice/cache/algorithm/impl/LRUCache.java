package by.nata.newscommentsservice.cache.algorithm.impl;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Scope("prototype")
@ConditionalOnProperty(prefix = "cache", name = "algorithm", havingValue = "LRU")
public class LRUCache<K, V> implements Cache<K, V> {

    private final int maxSize;
    private final LinkedHashMap<K, V> cache;

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
