package ru.arsmagna;

import org.junit.*;

import static org.junit.Assert.*;

public class IniLineTest
{
    @Test
    public void toString_1()
    {
        IniLine line = new IniLine();
        assertEquals("null=null", line.toString());

        line = new IniLine("a", "Hello");
        assertEquals("a=Hello", line.toString());
    }
}