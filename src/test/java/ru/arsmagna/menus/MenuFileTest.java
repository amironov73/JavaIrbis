package ru.arsmagna.menus;

import org.junit.Test;

import ru.arsmagna.TestBase;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class MenuFileTest extends TestBase {

    @Test
    public void read_1() throws IOException {
        File file = getFile("org.mnu");
        MenuFile menuFile = MenuFile.parse(file);
        assertEquals("rus", menuFile.getValue("6"));
    }
}
