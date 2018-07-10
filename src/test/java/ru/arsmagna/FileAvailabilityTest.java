package ru.arsmagna;

import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class FileAvailabilityTest {
    @Test
    @Ignore
    public void printCurrentDirectory() {
        File file = new File(".");
        String currentDirectory = file.getAbsolutePath();
        System.out.println(currentDirectory);
    }
}
