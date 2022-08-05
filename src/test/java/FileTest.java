import dev.JustRed23.stonebrick.data.Directory;
import dev.JustRed23.stonebrick.data.File;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class FileTest {

    @Test
    void testFile() throws IOException {
        File file = new File("test.txt", new Directory(Paths.get("debug")));
        file.clear();
        assertEquals("test.txt", file.getName());
        assertEquals(Paths.get("debug/test.txt"), file.getPath());
        assertEquals(0, file.getContent().size());
        assertTrue(file.isEmpty());
        assertTrue(file.getFile().exists());

        file.writeLine("test");
        assertEquals(1, file.getContent().size());
        assertFalse(file.isEmpty());
        assertEquals("test", file.getContent().get(0));

        file.writeLine("test2");
        assertEquals(2, file.getContent().size());
        assertFalse(file.isEmpty());
        assertEquals("test2", file.getContent().get(1));

        file.clear();
        assertEquals(0, file.getContent().size());
        file.delete();
        file.getDirectory().delete();
    }
}
