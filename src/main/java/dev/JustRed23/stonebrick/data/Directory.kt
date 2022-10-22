package dev.JustRed23.stonebrick.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Directory {

    public static final Directory ROOT = new Directory(Paths.get(System.getProperty("user.dir")));
    private final java.io.File directory;
    private final List<File> files;

    public Directory(Path path) {
        this.directory = new java.io.File(path.toString());
        this.files = new ArrayList<>();
        if (!this.directory.exists()) {
            if (!this.directory.mkdirs())
                System.err.println("Could not create directory " + getPath().toString());
        }
    }

    public synchronized void delete() {
        try {
            if (!isEmpty())
                List.copyOf(getFiles())
                        .stream()
                        .filter(File::exists)
                        .forEach(File::delete);
            getFiles().clear();
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

    public boolean isEmpty() {
        return getFiles().stream().noneMatch(File::exists) || !exists();
    }

    public java.io.File getDirectory() {
        return directory;
    }

    public List<File> getFiles() {
        return files;
    }

    public Path getPath() {
        return getDirectory().toPath();
    }
}
