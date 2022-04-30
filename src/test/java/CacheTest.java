import dev.JustRed23.stonebrick.cache.types.LRUCache;
import dev.JustRed23.stonebrick.cfg.Config;
import dev.JustRed23.stonebrick.exeptions.ConfigInitializationException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    @Test
    void testLRUCache() throws ConfigInitializationException {
        Config.initialize();
        LRUCache<String, String> cache = new LRUCache<>(1, TimeUnit.MINUTES, 10);

        for (int i = 0; i < 20; i++)
            cache.put(String.valueOf(i), "hello world x" + i);

        assertEquals(10, cache.size());
    }
}