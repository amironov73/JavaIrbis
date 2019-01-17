// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.junit.Test;
import ru.arsmagna.TestBase;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class AlphabetTableTest extends TestBase {

    @Test
    public void isAlpha_1() {
        AlphabetTable table = AlphabetTable.getDefault();
        assertTrue(table.isAlpha('A'));
        assertTrue(table.isAlpha((char)0x0410));
        assertFalse(table.isAlpha(' '));
        assertFalse(table.isAlpha('\n'));
        assertFalse(table.isAlpha((char)0x01E0));
    }

    @Test
    public void parse_1() throws IOException {
        File file = getFile(AlphabetTable.FILE_NAME);
        AlphabetTable table = AlphabetTable.parse(file);
        assertTrue(table.isAlpha('A'));
        assertTrue(table.isAlpha((char)0x0410));
        assertFalse(table.isAlpha(' '));
        assertFalse(table.isAlpha('\n'));
        assertFalse(table.isAlpha((char)0x01E0));
    }

    @Test
    public void splitWords_1() {
        AlphabetTable table = AlphabetTable.getDefault();
        String[] splitted = table.splitWords("Hello, world!");
        assertEquals(2, splitted.length);
        assertEquals("Hello", splitted[0]);
        assertEquals("world", splitted[1]);

        splitted = table.splitWords("Привет, мир!");
        assertEquals(2, splitted.length);
        assertEquals("Привет", splitted[0]);
        assertEquals("мир", splitted[1]);

        splitted = table.splitWords("");
        assertEquals(0, splitted.length);
    }

    @Test
    public void trim_1() {
        AlphabetTable table = AlphabetTable.getDefault();
        assertEquals("", table.trim(""));
        assertEquals("", table.trim("!?!"));
        assertEquals("Hello", table.trim("Hello"));
        assertEquals("Hello", table.trim("(Hello)"));
        assertEquals("Привет", table.trim("Привет"));
        assertEquals("Привет", table.trim("(Привет)"));
    }
}