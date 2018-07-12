package ru.arsmagna;

import org.junit.Test;
import ru.arsmagna.infrastructure.IniLine;
import ru.arsmagna.infrastructure.IniSection;

import static org.junit.Assert.assertEquals;

public class IniSectionTest {
    @Test
    public void toString_1() {
        IniSection section = new IniSection();
        assertEquals("", section.toString());

        section = new IniSection("Hello");
        assertEquals("[Hello]\n", section.toString());

        section.lines.add(new IniLine("Key1", "Value1"));
        assertEquals("[Hello]\nKey1=Value1\n", section.toString());
    }
}