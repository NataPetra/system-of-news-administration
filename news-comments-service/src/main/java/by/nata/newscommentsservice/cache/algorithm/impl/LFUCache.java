package by.nata.newscommentsservice.cache.algorithm.impl;

import by.nata.newscommentsservice.cache.algorithm.api.Cache;
import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

@Component
@Qualifier("LFUCache")
public class LFUCache<K, V> implements Cache<K, V> {

    private final int maxSize;
    private final Map<K, V> cache;
    private final Map<K, Integer> frequency;
    private final PriorityQueue<K> priorityQueue;

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
    public Optional<V> put(K key, V value) {
        if (cache.size() >= maxSize) {
            K leastFrequent = priorityQueue.poll();
            cache.remove(leastFrequent);
            frequency.remove(leastFrequent);
        }
        cache.put(key, value);
        frequency.put(key, 1);
        priorityQueue.offer(key);
        return Optional.of(value);
    }

    @Override
    public void delete(K key) {
        cache.remove(key);
        frequency.remove(key);
        priorityQueue.remove(key);
    }
}
