package dev.JustRed23.stonebrick.cache.types;

import dev.JustRed23.stonebrick.cache.CacheObject;
import dev.JustRed23.stonebrick.cache.ICache;
import dev.JustRed23.stonebrick.log.SBLogger;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class LRUCache<K, V> implements ICache<K, V> {

    private static final Logger LOGGER = SBLogger.getLogger(LRUCache.class);

    private final long timeToLiveMs;
    private final LRUMap<K, CacheObject<V>> lru;

    public LRUCache(@Range(from = 1, to = Integer.MAX_VALUE) int timeToLive, @NotNull TimeUnit unit, @Range(from = 1, to = Integer.MAX_VALUE) int maxSize) {
        this.timeToLiveMs = unit.toMillis(timeToLive);
        this.lru = new LRUMap<>(maxSize);
        LOGGER.debug("Creating cache with a max size of {} and a time to live of {} ms", maxSize, timeToLiveMs);

        Thread cleanupThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                cleanup();
            }
        }, "LRUCache Cleanup Thread");

        cleanupThread.setDaemon(true);
        LOGGER.debug("Starting cleanup thread");
        cleanupThread.start();
    }

    private void cleanup() {
        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey;

        synchronized (lru) {
            MapIterator<K, CacheObject<V>> itr = lru.mapIterator();

            deleteKey = new ArrayList<>((lru.size() / 2) + 1);

            K key;
            CacheObject<V> cacheObject;

            while (itr.hasNext()) {
                key = itr.next();
                cacheObject = itr.getValue();

                if (cacheObject != null && (now >= (timeToLiveMs + cacheObject.lastAccessedMs)))
                    deleteKey.add(key);
            }
        }

        for (K key : deleteKey) {
            LOGGER.debug("Key {} reached time to live. Removing from cache", key);
            remove(key);
            Thread.yield();
        }
    }

    public void put(@NotNull K key, @NotNull V value) {
        synchronized (lru) {
            lru.put(key, new CacheObject<>(value));
        }
    }

    public Optional<V> get(@NotNull K key) {
        synchronized (lru) {
            CacheObject<V> cacheObject = lru.get(key);

            if (cacheObject == null)
                return Optional.empty();
            else {
                cacheObject.lastAccessedMs = System.currentTimeMillis();
                return Optional.of(cacheObject.value);
            }
        }
    }

    public void remove(@NotNull K key) {
        synchronized (lru) {
            lru.remove(key);
        }
    }

    public int size() {
        synchronized (lru) {
            return lru.size();
        }
    }

    public int maxSize() {
        synchronized (lru) {
            return lru.maxSize();
        }
    }

    public boolean isEmpty() {
        synchronized (lru) {
            return lru.isEmpty();
        }
    }

    public void clear() {
        synchronized (lru) {
            lru.clear();
        }
    }
}