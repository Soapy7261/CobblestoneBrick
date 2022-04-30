package dev.JustRed23.stonebrick.cache;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface ICache<K, V> {
    void put(@NotNull K key, @NotNull V value);
    Optional<V> get(@NotNull K key);
    void remove(@NotNull K key);
    int size();
    boolean isEmpty();
    void clear();
}