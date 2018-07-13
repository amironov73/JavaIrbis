package ru.arsmagna.infrastructure;

import org.junit.Test;

import ru.arsmagna.TestBase;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class UpperCaseTableTest extends TestBase {

    @Test
    public void getDefault_1() {
        UpperCaseTable table = UpperCaseTable.getDefault();
        assertEquals('A', table.upperCase('a'));
        assertEquals('A', table.upperCase('A'));
        assertEquals('1', table.upperCase('1'));
        assertEquals((char)0x0410, table.upperCase((char)0x0430));
        assertEquals((char)0x0410, table.upperCase((char)0x0410));
        assertEquals((char)0x01E0, table.upperCase((char)0x01E0));
    }

    @Test
    public void parse_1() throws IOException {
        File file = getFile(UpperCaseTable.FILE_NAME);
        UpperCaseTable table = UpperCaseTable.parse(file);
        assertEquals('A', table.upperCase('a'));
        assertEquals('A', table.upperCase('A'));
        assertEquals('1', table.upperCase('1'));
        assertEquals((char)0x0410, table.upperCase((char)0x0430));
        assertEquals((char)0x0410, table.upperCase((char)0x0410));
        assertEquals((char)0x01E0, table.upperCase((char)0x01E0));
    }
}