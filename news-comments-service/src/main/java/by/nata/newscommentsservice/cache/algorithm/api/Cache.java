package by.nata.newscommentsservice.cache.algorithm.api;

import java.util.Optional;

public interface Cache<K, V> {

    V get(K key);

    Optional<V> put(K key, V value);

    void delete(K key);
}
