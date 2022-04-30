package dev.JustRed23.stonebrick.cache;

public class CacheObject<V> {

    public V value;
    public long lastAccessedMs = System.currentTimeMillis();

    public CacheObject(V value) {
        this.value = value;
    }
}