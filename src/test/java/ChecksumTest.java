import dev.JustRed23.stonebrick.validation.Checksum;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ChecksumTest {

    @Test
    void testChecksum() {
        assertEquals("6cd3556deb0da54bca060b4c39479839", Checksum.MD5.checksum(new File("src/test/resources/checksumtest.txt")));
        assertEquals("943a702d06f34599aee1f8da8ef9f7296031d699", Checksum.SHA1.checksum(new File("src/test/resources/checksumtest.txt")));
        assertTrue(Checksum.MD5.isValid(new File("src/test/resources/checksumtest.txt"), "6cd3556deb0da54bca060b4c39479839"));
        assertTrue(Checksum.SHA1.isValid(new File("src/test/resources/checksumtest.txt"), "943a702d06f34599aee1f8da8ef9f7296031d699"));
    }
}
