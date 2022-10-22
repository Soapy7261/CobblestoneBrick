import dev.JustRed23.stonebrick.cfg.Config;
import dev.JustRed23.stonebrick.exceptions.ConfigInitializationException;
import dev.JustRed23.stonebrick.log.SBLogger;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoggerTest {

    @Test
    void testLogger() throws ConfigInitializationException {
        Config.initialize();
        final Logger logger = SBLogger.getLogger(getClass());
        logger.info("Hello world!");
        assertNotNull(logger);
    }
}