package ru.arsmagna;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IniLineTest {
    @Test
    public void toString_1() {
        IniLine line = new IniLine();
        assertEquals("(null)=", line.toString());

        line = new IniLine("a", "Hello");
        assertEquals("a=Hello", line.toString());
    }
}