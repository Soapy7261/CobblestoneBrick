package dev.JustRed23.TestApp;

import dev.JustRed23.stonebrick.data.annotation.Directory;
import dev.JustRed23.stonebrick.data.annotation.File;
import dev.JustRed23.stonebrick.data.annotation.FileStructure;

@FileStructure
public class TestFileStructure {

    @Directory(path = "fruits")
    public static dev.JustRed23.stonebrick.data.Directory fruits;

    @Directory(path = "vegetables")
    public static dev.JustRed23.stonebrick.data.Directory vegetables;

    @File(name = "apple", directory = "fruits", content = "red")
    public static dev.JustRed23.stonebrick.data.File apple;

    @File(name = "carrot", directory = "vegetables", content = "orange")
    public static dev.JustRed23.stonebrick.data.File carrot;
}
