// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.junit.Test;
import ru.arsmagna.infrastructure.IniLine;

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