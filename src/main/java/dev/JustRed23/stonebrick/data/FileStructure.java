package dev.JustRed23.stonebrick.data;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileStructure {

    private static Class<?> fileStructure = DefaultFileStructure.class;
    private static boolean INITIALIZED = false;

    private static final List<Directory> mappedDirectories = new ArrayList<>();
    private static final List<File> mappedFiles = new ArrayList<>();

    public static void init() {
        if (INITIALIZED)
            throw new IllegalStateException("FileStructure has already been initialized");
        INITIALIZED = true;
        System.out.println("=====Initializing File Structure=====");
        System.out.println("File Structure: " + fileStructure.getName());
        Arrays.stream(fileStructure.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(dev.JustRed23.stonebrick.data.annotation.Directory.class))
                .forEach(field -> {
                    if (!field.trySetAccessible())
                        return;

                    try {
                        Directory directory = new Directory(Paths.get(field.getAnnotation(dev.JustRed23.stonebrick.data.annotation.Directory.class).path()));
                        mappedDirectories.add(directory);
                        field.set(field.getType(), directory);
                        System.out.printf("Added directory %s%n", directory.getPath());
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        Arrays.stream(fileStructure.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(dev.JustRed23.stonebrick.data.annotation.File.class))
                .forEach(field -> {
                    if (!field.trySetAccessible())
                        return;

                    dev.JustRed23.stonebrick.data.annotation.File annotation = field.getAnnotation(dev.JustRed23.stonebrick.data.annotation.File.class);

                    Directory dir = getDirectory(annotation.directory());
                    if (dir == null) {
                        System.out.printf("Could not find directory %s%n", annotation.directory());
                        dir = Directory.ROOT;
                    }

                    try {
                        File file = new File(annotation.name(), dir, annotation.content());
                        mappedFiles.add(file);
                        field.set(field.getType(), file);
                        System.out.printf("Added file %s%n", file.getPath());
                    } catch (IOException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
        System.out.println("=====Initialization complete=====");
    }

    public static void discover(Class<?> fileStructure) {
        if (INITIALIZED)
            throw new IllegalStateException("FileStructure has already been initialized");

        if (fileStructure == null)
            throw new IllegalArgumentException("FileStructure cannot be null");
        if (fileStructure == DefaultFileStructure.class)
            throw new IllegalArgumentException("FileStructure cannot be " + DefaultFileStructure.class.getSimpleName());

        if (FileStructure.fileStructure == DefaultFileStructure.class) {
            if (fileStructure.isAnnotationPresent(dev.JustRed23.stonebrick.data.annotation.FileStructure.class)) {
                FileStructure.fileStructure = fileStructure;
            } else
                System.err.println("File structure " + fileStructure.getName() + " is not annotated with @" + dev.JustRed23.stonebrick.data.annotation.FileStructure.class.getSimpleName());
        } else throw new RuntimeException("FileStructure already discovered as " + FileStructure.fileStructure.getName());
    }

    @Nullable
    public static Directory getDirectory(String name) {
        if (name.isBlank() || name.equals("."))
            return Directory.ROOT;
        return mappedDirectories.stream()
                .filter(directory -> directory.getDirectory().getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static File getFile(String name) {
        if (name.isBlank())
            return null;
        return mappedFiles.stream()
                .filter(file -> file.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
