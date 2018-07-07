package tests;

import org.junit.jupiter.api.*;

import ru.arsmagna.*;

import static org.junit.jupiter.api.Assertions.*;

class IniLineTest
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
    void IniLine_toString_1()
    {
        IniLine line = new IniLine();
        assertEquals("null=null", line.toString());

        line = new IniLine("a", "Hello");
        assertEquals("a=Hello", line.toString());
    }
}