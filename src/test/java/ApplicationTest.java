import dev.JustRed23.stonebrick.app.Application;
import dev.JustRed23.stonebrick.exceptions.ConfigInitializationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void test_success() throws ConfigInitializationException {
        assertDoesNotThrow(() -> Application.launch(ApplicationTest$TestApp.class, new String[]{}));
    }

    @Test
    void test_failure() throws ConfigInitializationException {
        assertThrows(Exception.class, () -> Application.launch(new String[]{}));
    }
}
