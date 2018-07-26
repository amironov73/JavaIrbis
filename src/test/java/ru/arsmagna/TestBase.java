package ru.arsmagna;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("WeakerAccess")
public class TestBase {

    @SuppressWarnings("SameReturnValue")
    public String testResources() {
        return "./src/test/resources";
    }

    public Path getPath(@NotNull String fileName) {
        Path result = Paths.get(testResources(), fileName);

        return result;
    }

    public File getFile(@NotNull String fileName) {
        Path path = Paths.get(testResources(), fileName);

        return path.toFile();
    }
}
