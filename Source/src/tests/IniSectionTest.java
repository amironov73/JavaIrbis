package tests;

import org.junit.jupiter.api.*;

import ru.arsmagna.*;

import static org.junit.jupiter.api.Assertions.*;

class IniSectionTest
{
    @BeforeEach
    void setUp()
    {
    }

    @AfterEach
    void tearDown()
    {
    }

    @Test
    void IniSection_toString_1()
    {
        IniSection section = new IniSection();
        assertEquals("[null]", section.toString());

        section = new IniSection("Hello");
        assertEquals("[Hello]", section.toString());

        section.lines.add(new IniLine("Key1", "Value1"));
        assertEquals("[Hello]", section.toString());
    }
}