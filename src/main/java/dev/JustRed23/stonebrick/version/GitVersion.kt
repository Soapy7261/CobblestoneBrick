package dev.JustRed23.stonebrick.version;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public record GitVersion(String gitHash, Date buildTime) implements Version {

    public static GitVersion fromString(String version, Date buildTime) {
        return new GitVersion(version, buildTime);
    }

    public static GitVersion fromFile(File file) {
        try {
            return fromFile(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static GitVersion fromFile(InputStream fileStream) {
        Properties prop = new Properties();
        try (fileStream) {
            prop.load(fileStream);
            return fromString(prop.getProperty("version"), Version.getDateFromString(prop.getProperty("buildTime")));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getVersion() {
        return gitHash;
    }
}
