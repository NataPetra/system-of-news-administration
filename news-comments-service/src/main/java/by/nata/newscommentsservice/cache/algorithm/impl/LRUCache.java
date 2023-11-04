package by.nata.newscommentsservice.cache.algorithm.impl;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("LRUCache")
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
    public Optional<V> put(K key, V value) {
        cache.put(key, value);
        return Optional.of(value);
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
    }
}
