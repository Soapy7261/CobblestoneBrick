import dev.JustRed23.stonebrick.app.Application;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationTest {

    @Test
    void test_success() {
        assertDoesNotThrow(() -> Application.launch(ApplicationTest$TestApp.class, new String[]{}));
    }

    @Test
    void test_failure() {
        assertThrows(Exception.class, () -> Application.launch(new String[]{}));
    }

    @Test
    void test_service() {
        assertDoesNotThrow(() -> Application.launch(ApplicationTest$TestApp.class, new String[]{}));
    }
}
