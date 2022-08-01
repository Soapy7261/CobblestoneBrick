import dev.JustRed23.stonebrick.version.BasicVersion;
import dev.JustRed23.stonebrick.version.CompareResult;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class VersioningTest {

    @Test
    public void testVersioning() throws InterruptedException {
        BasicVersion v1 = BasicVersion.fromString("1.0.0", new Date());
        BasicVersion v2 = BasicVersion.fromString("1.0.1", new Date());
        TimeUnit.SECONDS.sleep(1);
        BasicVersion v3 = BasicVersion.fromString("1.0.0", new Date());
        //get version.properties from the resources folder and parse it to a file
        File versionFile = new File("src/test/resources/version.properties");
        BasicVersion v4 = BasicVersion.fromFile(versionFile);

        assertSame(v1.compare(v2), CompareResult.OLDER);
        assertSame(v1.compare(v3), CompareResult.OLDER);
        assertSame(v2.compare(v3), CompareResult.NEWER);
        assertSame(v2.compare(v1), CompareResult.NEWER);
        assertSame(v3.compare(v3), CompareResult.EQUAL);
        assertSame(v1.compare(v4), CompareResult.NEWER);
    }
}
