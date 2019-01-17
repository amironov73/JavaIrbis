// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.junit.Test;
import ru.arsmagna.infrastructure.IniFile;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class IniFileTest extends TestBase {

    @Test
    public void parse_1() throws IOException {
        File file = getFile("IniFile1.ini");
        IniFile iniFile = IniFile.parse(file);
        String actual = iniFile.getValue("Main", "FirstParameter");
        String expected = "1";
        assertEquals(expected, actual);

        actual = iniFile.getValue("Main", "ThirdParameter");
        assertNull(actual);
    }
}