package ru.arsmagna;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestBase {

    public String testResources() {
        return "./src/test/resources";
    }

    public File getFile(@NotNull String fileName) {
        Path path = Paths.get(testResources(), fileName);

        return path.toFile();
    }
}
