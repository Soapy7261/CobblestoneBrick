package dev.JustRed23.stonebrick.cache.types;

class CacheObject<V> {

    protected V value;
    protected long lastAccessedMs = System.currentTimeMillis();

    protected CacheObject(V value) {
        this.value = value;
    }
}