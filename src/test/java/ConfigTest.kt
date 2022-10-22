import dev.JustRed23.stonebrick.util.Parseable;
import dev.JustRed23.stonebrick.cfg.Config;
import dev.JustRed23.stonebrick.exceptions.ConfigInitializationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigTest {

    @Test
    void testParseable() {
        Parseable testString = new Parseable("hello world");
        Parseable testBoolean = new Parseable(true);
        assertEquals("hello world", testString.asString());
        assertTrue(testBoolean.asBoolean());
    }

    @Test
    void testWrongParse() {
        Parseable testString = new Parseable("hello world");
        assertThrows(Exception.class, testString::asDouble);
    }

    @Test
    void testConfig() {
        assertDoesNotThrow(Config::initialize);
    }

    @Test
    void testThrows() throws ConfigInitializationException {
        Config.initialize();
        assertThrows(IllegalStateException.class, Config::initialize);
    }
}