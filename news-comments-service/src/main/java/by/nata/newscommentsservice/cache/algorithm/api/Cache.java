package by.nata.newscommentsservice.cache.algorithm.api;

/**
 * The {@code Cache} interface defines a simple cache that stores key-value pairs. Implementations
 * of this interface allow for the storage and retrieval of values associated with keys.
 *
 * @param <K> The type of keys in the cache.
 * @param <V> The type of values in the cache.
 */
public interface Cache<K, V> {

    /**
     * Retrieves the value associated with the specified key from the cache.
     *
     * @param key The key whose associated value is to be retrieved.
     * @return The value associated with the specified key, or {@code null} if no mapping for the key exists.
     */
    V get(K key);

    /**
     * Associates the specified value with the specified key in the cache.
     *
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     * @return The previous value associated with the key, or {@code null} if there was no mapping for the key.
     */
    V put(K key, V value);

    /**
     * Deletes the value associated with the specified key from the cache.
     *
     * @param key The key whose associated value is to be deleted.
     */
    void delete(K key);
}
