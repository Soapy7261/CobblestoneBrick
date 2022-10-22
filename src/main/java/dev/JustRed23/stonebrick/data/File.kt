package dev.JustRed23.stonebrick.data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class File {

    private final String name;
    private final Directory directory;
    private final List<String> content;

    private final java.io.File file;

    private final Object WRITELOCK = new Object();

    public File(String name, Directory directory, String[] content) throws IOException {
        this.name = name;
        this.directory = directory;

        this.file = new java.io.File(directory.getPath() + java.io.File.separator + name);
        if (!this.file.exists()) {
            createIfNotExist();
            this.content = new ArrayList<>();
        } else this.content = Files.readAllLines(getPath());

        if (!isEmpty())
            return;

        for (String line : content) {
            writeLine(line);
        }
    }

    public File(String name, Directory directory) throws IOException {
        this(name, directory, new String[0]);
    }

    public synchronized void write(String text) {
        write(text, false);
    }

    public synchronized void writeLine(String text) {
        write(text, true);
    }

    private synchronized void write(String text, boolean newLine) {
        synchronized (WRITELOCK) {
            try {
                Files.write(getPath(), text.getBytes(), StandardOpenOption.APPEND);
                if (newLine)
                    Files.write(getPath(), String.format("%n").getBytes(), StandardOpenOption.APPEND);
                getContent().add(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void clear() {
        try {
            new FileWriter(getFile(), false).close();
            getContent().clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void delete() {
        try {
            Files.delete(getPath());
            getContent().clear();
            getDirectory().getFiles().remove(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIfNotExist() {
        if (!exists()) {
            try {
                Files.createFile(getPath());
                if (!getDirectory().getFiles().contains(this))
                    getDirectory().getFiles().add(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getName() {
        return name;
    }

    public Directory getDirectory() {
        return directory;
    }

    public Path getPath() {
        return getFile().toPath();
    }

    /**
     * @return an array of strings representing the content of the file, every entry is a line of the file (starting from 0)
     */
    public List<String> getContent() {
        return content;
    }

    public boolean exists() {
        return getFile().exists();
    }

    public boolean isEmpty() {
        return getContent().isEmpty() || !exists();
    }

    /**
     * @return a java.io.File object representing the file
     */
    public java.io.File getFile() {
        return file;
    }
}
