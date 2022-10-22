package dev.JustRed23.stonebrick.version;

import java.io.*;
import java.util.Date;
import java.util.Properties;

public record BasicVersion(int major, int minor, int patch, Date buildTime) implements Version {

    public static BasicVersion fromString(String version, Date buildTime) {
        String[] split = version.split("\\.");
        return new BasicVersion(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), buildTime);
    }

    public static BasicVersion fromFile(File file) {
        try {
            return fromFile(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BasicVersion fromFile(InputStream fileStream) {
        Properties prop = new Properties();
        try (fileStream) {
            prop.load(fileStream);
            return fromString(prop.getProperty("version"), Version.getDateFromString(prop.getProperty("buildTime")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public CompareResult compare(Version other) {
        if (major() > ((BasicVersion) other).major())
            return CompareResult.NEWER;
        else if (major() < ((BasicVersion) other).major())
            return CompareResult.OLDER;
        else if (minor() > ((BasicVersion) other).minor())
            return CompareResult.NEWER;
        else if (minor() < ((BasicVersion) other).minor())
            return CompareResult.OLDER;
        else if (patch() > ((BasicVersion) other).patch())
            return CompareResult.NEWER;
        else if (patch() < ((BasicVersion) other).patch())
            return CompareResult.OLDER;

        else if (buildTime().after(other.buildTime()))
            return CompareResult.NEWER;
        else if (buildTime().before(other.buildTime()))
            return CompareResult.OLDER;

        else if (getVersion().equals(other.getVersion()))
            return CompareResult.EQUAL;

        else
            return CompareResult.UNKNOWN;
    }

    public String getVersion() {
        return patch == 0 ? String.format("%d.%d", major, minor) : String.format("%d.%d.%d", major, minor, patch);
    }
}
