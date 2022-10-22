package dev.JustRed23.stonebrick.data;

import dev.JustRed23.stonebrick.data.annotation.Directory;
import dev.JustRed23.stonebrick.data.annotation.File;
import dev.JustRed23.stonebrick.data.annotation.FileStructure;

@FileStructure
public class DefaultFileStructure {

    @Directory(path = "config")
    public static dev.JustRed23.stonebrick.data.Directory configDir;

    @Directory(path = "data")
    public static dev.JustRed23.stonebrick.data.Directory dataDir;

    @File(name = "config.yml", directory = "config", content = {"version: 1.0", "foo: bar", "bar: foo", "hello: world"})
    public static dev.JustRed23.stonebrick.data.File config;

    @File(name = "README.MD", directory = "data", content = {"# StoneBrick", "This is the default file structure for StoneBrick. Consider making your own file structure by implementing @FileStructure and adding it to the engine with FileStructure.discover when initializing the engine."})
    public static dev.JustRed23.stonebrick.data.File readme;
}
