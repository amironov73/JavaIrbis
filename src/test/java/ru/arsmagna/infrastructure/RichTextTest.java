// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.junit.Test;
import ru.arsmagna.UnicodeRange;

import static org.junit.Assert.*;

@SuppressWarnings("SimplifiableJUnitAssertion")
public class RichTextTest {

    @Test
    public void construction_1() {
        assertNotNull(RichText.CentralEuropeanPrologue);
        assertNotNull(RichText.CommonPrologue);
        assertNotNull(RichText.RussianPrologue);
        assertNotNull(RichText.WesternEuropeanPrologue);
    }

    @Test
    public void decode_1() {
        assertEquals(null, RichText.decode(null));
        assertEquals("", RichText.decode(""));
        assertEquals("Hello", RichText.decode("Hello"));
        assertEquals("\\{\\}", RichText.decode("\\{\\}"));
        assertEquals("\\b Hello \\b0", RichText.decode("\\b Hello \\b0"));
    }

    @Test
    public void decode_2() {
        assertEquals("Привет", RichText.decode("\\u1055?\\u1088?\\u1080?\\u1074?\\u1077?\\u1090?"));
    }

    @Test
    public void decode_3() {
        assertEquals("Hello\\", RichText.decode("Hello\\"));
    }

    @Test
    public void encode_1() {
        assertEquals(null, RichText.encode(null, null));
        assertEquals("", RichText.encode("", null));
        assertEquals("Hello", RichText.encode("Hello", null));
        assertEquals("\\{\\}", RichText.encode("{}", null));
        assertEquals("Hel\\\\lo", RichText.encode("Hel\\lo", null));
    }

    @Test
    public void encode_2() {
        assertEquals("Hello,\\'0aWorld!", RichText.encode("Hello,\nWorld!", null));
    }

    @Test
    public void encode_3() {
        UnicodeRange range = UnicodeRange.cyrillic;
        assertEquals("Hello", RichText.encode("Hello", range));
        assertEquals("Привет", RichText.encode("Привет", range));
    }

    @Test
    public void encode_4() {
        UnicodeRange range = UnicodeRange.latinExtended;
        assertEquals("Hello", RichText.encode("Hello", range));
        assertEquals("\\u1055?\\u1088?\\u1080?\\u1074?\\u1077?\\u1090?", RichText.encode("Привет", range));
    }

    @Test
    public void encode_5() {
        assertEquals("Hello\\'88", RichText.encode("Hello\u0088", null));
    }
}