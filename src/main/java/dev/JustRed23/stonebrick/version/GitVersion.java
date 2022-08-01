package dev.JustRed23.stonebrick.version;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

public record GitVersion(String gitHash, Date buildTime) implements Version {

    public static GitVersion fromString(String version, Date buildTime) {
        return new GitVersion(version, buildTime);
    }

    public static GitVersion fromFile(File versionFile) {
        Properties prop = new Properties();
        try (FileInputStream stream = new FileInputStream(versionFile)) {
            prop.load(stream);
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
