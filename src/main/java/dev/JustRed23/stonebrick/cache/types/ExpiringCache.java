package dev.JustRed23.stonebrick.cache.types;

import dev.JustRed23.stonebrick.cache.CacheObject;
import dev.JustRed23.stonebrick.cache.ICache;
import dev.JustRed23.stonebrick.log.SBLogger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ExpiringCache<K, V> implements ICache<K, V> {

    private static final Logger LOGGER = SBLogger.getLogger(ExpiringCache.class);

    private final long timeToLiveMs;
    private final Map<K, CacheObject<V>> cache;

    public ExpiringCache(@Range(from = 1, to = Integer.MAX_VALUE) int timeToLive, @NotNull TimeUnit unit) {
        this.timeToLiveMs = unit.toMillis(timeToLive);
        this.cache = new ConcurrentHashMap<>();
        LOGGER.debug("Created expiring cache with a time to live of {} ms", timeToLiveMs);
    }

    public void put(@NotNull K key, @NotNull V value) {
        cache.put(key, new CacheObject<>(value));
    }

    public Optional<V> get(@NotNull K key) {
        if (cache.containsKey(key)) {
            CacheObject<V> cacheObject = cache.get(key);
            long now = System.currentTimeMillis();

            if (now >= cacheObject.lastAccessedMs + timeToLiveMs) {
                LOGGER.debug("Key {} reached time to live. Removing from cache", key);
                cache.remove(key);
            } else return Optional.of(cacheObject.value);
        }
        return Optional.empty();
    }

    public void remove(@NotNull K key) {
        cache.remove(key);
    }

    public int size() {
        return cache.size();
    }

    public boolean isEmpty() {
        return cache.isEmpty();
    }

    public void clear() {
        cache.clear();
    }
}