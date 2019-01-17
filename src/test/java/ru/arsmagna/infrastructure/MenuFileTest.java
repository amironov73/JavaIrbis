// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.junit.Test;

import ru.arsmagna.TestBase;
import ru.arsmagna.infrastructure.MenuFile;

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
