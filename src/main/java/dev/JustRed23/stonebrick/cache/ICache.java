package dev.JustRed23.stonebrick.cache;

import java.util.Optional;

public interface ICache<K, V> {
    boolean set(K key, V value);
    Optional<V> get(K key);
    int size();
    boolean isEmpty();
    void clear();
}