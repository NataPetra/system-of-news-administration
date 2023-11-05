package by.nata.newscommentsservice.cache.algorithm.api;

public interface Cache<K, V> {

    V get(K key);

    V put(K key, V value);

    void delete(K key);
}
