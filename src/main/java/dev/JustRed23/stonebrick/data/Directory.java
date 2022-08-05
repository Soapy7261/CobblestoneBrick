package dev.JustRed23.stonebrick.data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Directory {

    public static final Directory ROOT = new Directory(Paths.get(System.getProperty("user.dir")));
    private final File directory;

    public Directory(Path path) {
        this.directory = new File(path.toString());
        if (!this.directory.exists()) {
            if (!this.directory.mkdirs())
                System.err.println("Could not create directory " + getPath().toString());
        }
    }

    public void delete() {
        try {
            Files.delete(getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIfNotExist() {
        if (!exists()) {
            try {
                Files.createFile(getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean exists() {
        return getDirectory().exists();
    }

    public File getDirectory() {
        return directory;
    }

    public Path getPath() {
        return getDirectory().toPath();
    }
}
