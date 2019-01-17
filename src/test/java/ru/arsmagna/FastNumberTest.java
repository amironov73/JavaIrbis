// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class FastNumberTest {

    @Test
    public void toString_1() {
        assertEquals("0", FastNumber.toString(0));
        assertEquals("1", FastNumber.toString(1));
        assertEquals("12", FastNumber.toString(12));
        assertEquals("123", FastNumber.toString(123));
        assertEquals("1234", FastNumber.toString(1234));
        assertEquals("12345", FastNumber.toString(12345));
        assertEquals("123456", FastNumber.toString(123456));
        assertEquals("1234567", FastNumber.toString(1234567));
        assertEquals("12345678", FastNumber.toString(12345678));
        assertEquals("123456789", FastNumber.toString(123456789));
    }

    @Test
    public void toString_2() {
        assertEquals("0", FastNumber.toString(0L));
        assertEquals("1", FastNumber.toString(1L));
        assertEquals("12", FastNumber.toString(12L));
        assertEquals("123", FastNumber.toString(123L));
        assertEquals("1234", FastNumber.toString(1234L));
        assertEquals("12345", FastNumber.toString(12345L));
        assertEquals("123456", FastNumber.toString(123456L));
        assertEquals("1234567", FastNumber.toString(1234567L));
        assertEquals("12345678", FastNumber.toString(12345678L));
        assertEquals("123456789", FastNumber.toString(123456789L));
        assertEquals("1234567890", FastNumber.toString(1234567890L));
        assertEquals("12345678901", FastNumber.toString(12345678901L));
        assertEquals("123456789012", FastNumber.toString(123456789012L));
        assertEquals("1234567890123", FastNumber.toString(1234567890123L));
        assertEquals("12345678901234", FastNumber.toString(12345678901234L));
        assertEquals("123456789012345", FastNumber.toString(123456789012345L));
        assertEquals("1234567890123456", FastNumber.toString(1234567890123456L));
        assertEquals("12345678901234567", FastNumber.toString(12345678901234567L));
        assertEquals("123456789012345678", FastNumber.toString(123456789012345678L));
        assertEquals("1234567890123456789", FastNumber.toString(1234567890123456789L));
    }

    @Test
    public void parseInt32_1() {
        assertEquals(0, FastNumber.parseInt32("0"));
        assertEquals(1, FastNumber.parseInt32("1"));
        assertEquals(12, FastNumber.parseInt32("12"));
        assertEquals(123, FastNumber.parseInt32("123"));
        assertEquals(1234, FastNumber.parseInt32("1234"));
        assertEquals(12345, FastNumber.parseInt32("12345"));
        assertEquals(123456, FastNumber.parseInt32("123456"));
        assertEquals(1234567, FastNumber.parseInt32("1234567"));
        assertEquals(12345678, FastNumber.parseInt32("12345678"));
        assertEquals(123456789, FastNumber.parseInt32("123456789"));
    }

    @Test
    public void parseInt32_2() {
        String text = "123456789";
        assertEquals(0, FastNumber.parseInt32("0", 0, 1));
        assertEquals(1, FastNumber.parseInt32(text, 0, 1));
        assertEquals(12, FastNumber.parseInt32(text, 0, 2));
        assertEquals(123, FastNumber.parseInt32(text, 0, 3));
        assertEquals(1234, FastNumber.parseInt32(text, 0, 4));
        assertEquals(12345, FastNumber.parseInt32(text, 0, 5));
        assertEquals(123456, FastNumber.parseInt32(text, 0, 6));
        assertEquals(1234567, FastNumber.parseInt32(text, 0, 7));
        assertEquals(12345678, FastNumber.parseInt32(text, 0, 8));
        assertEquals(123456789, FastNumber.parseInt32(text, 0, 9));
    }

    @Test
    public void parseInt32_3() {
        char[] text = "123456789".toCharArray();
        assertEquals(0, FastNumber.parseInt32("0".toCharArray(), 0, 1));
        assertEquals(1, FastNumber.parseInt32(text, 0, 1));
        assertEquals(12, FastNumber.parseInt32(text, 0, 2));
        assertEquals(123, FastNumber.parseInt32(text, 0, 3));
        assertEquals(1234, FastNumber.parseInt32(text, 0, 4));
        assertEquals(12345, FastNumber.parseInt32(text, 0, 5));
        assertEquals(123456, FastNumber.parseInt32(text, 0, 6));
        assertEquals(1234567, FastNumber.parseInt32(text, 0, 7));
        assertEquals(12345678, FastNumber.parseInt32(text, 0, 8));
        assertEquals(123456789, FastNumber.parseInt32(text, 0, 9));
    }

    @Test
    public void parseInt32_4() {
        byte[] text = StandardCharsets.US_ASCII.encode("123456789").array();
        assertEquals(0, FastNumber.parseInt32(new byte[]{0x30}, 0, 1));
        assertEquals(1, FastNumber.parseInt32(text, 0, 1));
        assertEquals(12, FastNumber.parseInt32(text, 0, 2));
        assertEquals(123, FastNumber.parseInt32(text, 0, 3));
        assertEquals(1234, FastNumber.parseInt32(text, 0, 4));
        assertEquals(12345, FastNumber.parseInt32(text, 0, 5));
        assertEquals(123456, FastNumber.parseInt32(text, 0, 6));
        assertEquals(1234567, FastNumber.parseInt32(text, 0, 7));
        assertEquals(12345678, FastNumber.parseInt32(text, 0, 8));
        assertEquals(123456789, FastNumber.parseInt32(text, 0, 9));
    }

    @Test
    public void parseInt64_1() {
        assertEquals(0L, FastNumber.parseInt64("0"));
        assertEquals(1L, FastNumber.parseInt64("1"));
        assertEquals(12L, FastNumber.parseInt64("12"));
        assertEquals(123L, FastNumber.parseInt64("123"));
        assertEquals(1234L, FastNumber.parseInt64("1234"));
        assertEquals(12345L, FastNumber.parseInt64("12345"));
        assertEquals(123456L, FastNumber.parseInt64("123456"));
        assertEquals(1234567L, FastNumber.parseInt64("1234567"));
        assertEquals(12345678L, FastNumber.parseInt64("12345678"));
        assertEquals(123456789L, FastNumber.parseInt64("123456789"));
        assertEquals(1234567890L, FastNumber.parseInt64("1234567890"));
        assertEquals(12345678901L, FastNumber.parseInt64("12345678901"));
        assertEquals(123456789012L, FastNumber.parseInt64("123456789012"));
        assertEquals(1234567890123L, FastNumber.parseInt64("1234567890123"));
        assertEquals(12345678901234L, FastNumber.parseInt64("12345678901234"));
        assertEquals(123456789012345L, FastNumber.parseInt64("123456789012345"));
        assertEquals(1234567890123456L, FastNumber.parseInt64("1234567890123456"));
        assertEquals(12345678901234567L, FastNumber.parseInt64("12345678901234567"));
        assertEquals(123456789012345678L, FastNumber.parseInt64("123456789012345678"));
        assertEquals(1234567890123456789L, FastNumber.parseInt64("1234567890123456789"));
    }

    @Test
    public void parseInt64_2() {
        String text = "1234567890123456789";
        assertEquals(0L, FastNumber.parseInt64("0", 0, 1));
        assertEquals(1L, FastNumber.parseInt64(text, 0, 1));
        assertEquals(12L, FastNumber.parseInt64(text, 0, 2));
        assertEquals(123L, FastNumber.parseInt64(text, 0, 3));
        assertEquals(1234L, FastNumber.parseInt64(text, 0, 4));
        assertEquals(12345L, FastNumber.parseInt64(text, 0, 5));
        assertEquals(123456L, FastNumber.parseInt64(text, 0, 6));
        assertEquals(1234567L, FastNumber.parseInt64(text, 0, 7));
        assertEquals(12345678L, FastNumber.parseInt64(text, 0, 8));
        assertEquals(123456789L, FastNumber.parseInt64(text, 0, 9));
        assertEquals(1234567890L, FastNumber.parseInt64(text, 0, 10));
        assertEquals(12345678901L, FastNumber.parseInt64(text, 0, 11));
        assertEquals(123456789012L, FastNumber.parseInt64(text, 0, 12));
        assertEquals(1234567890123L, FastNumber.parseInt64(text, 0, 13));
        assertEquals(12345678901234L, FastNumber.parseInt64(text, 0, 14));
        assertEquals(123456789012345L, FastNumber.parseInt64(text, 0, 15));
        assertEquals(1234567890123456L, FastNumber.parseInt64(text, 0, 16));
        assertEquals(12345678901234567L, FastNumber.parseInt64(text, 0, 17));
        assertEquals(123456789012345678L, FastNumber.parseInt64(text, 0, 18));
        assertEquals(1234567890123456789L, FastNumber.parseInt64(text, 0, 19));
    }

    @Test
    public void parseInt64_3() {
        char[] text = "1234567890123456789".toCharArray();
        assertEquals(0L, FastNumber.parseInt64("0".toCharArray(), 0, 1));
        assertEquals(1L, FastNumber.parseInt64(text, 0, 1));
        assertEquals(12L, FastNumber.parseInt64(text, 0, 2));
        assertEquals(123L, FastNumber.parseInt64(text, 0, 3));
        assertEquals(1234L, FastNumber.parseInt64(text, 0, 4));
        assertEquals(12345L, FastNumber.parseInt64(text, 0, 5));
        assertEquals(123456L, FastNumber.parseInt64(text, 0, 6));
        assertEquals(1234567L, FastNumber.parseInt64(text, 0, 7));
        assertEquals(12345678L, FastNumber.parseInt64(text, 0, 8));
        assertEquals(123456789L, FastNumber.parseInt64(text, 0, 9));
        assertEquals(1234567890L, FastNumber.parseInt64(text, 0, 10));
        assertEquals(12345678901L, FastNumber.parseInt64(text, 0, 11));
        assertEquals(123456789012L, FastNumber.parseInt64(text, 0, 12));
        assertEquals(1234567890123L, FastNumber.parseInt64(text, 0, 13));
        assertEquals(12345678901234L, FastNumber.parseInt64(text, 0, 14));
        assertEquals(123456789012345L, FastNumber.parseInt64(text, 0, 15));
        assertEquals(1234567890123456L, FastNumber.parseInt64(text, 0, 16));
        assertEquals(12345678901234567L, FastNumber.parseInt64(text, 0, 17));
        assertEquals(123456789012345678L, FastNumber.parseInt64(text, 0, 18));
        assertEquals(1234567890123456789L, FastNumber.parseInt64(text, 0, 19));
    }

    @Test
    public void parseInt64_4() {
        byte[] text = StandardCharsets.US_ASCII.encode("1234567890123456789").array();
        assertEquals(0L, FastNumber.parseInt64(new byte[]{0x30}, 0, 1));
        assertEquals(1L, FastNumber.parseInt64(text, 0, 1));
        assertEquals(12L, FastNumber.parseInt64(text, 0, 2));
        assertEquals(123L, FastNumber.parseInt64(text, 0, 3));
        assertEquals(1234L, FastNumber.parseInt64(text, 0, 4));
        assertEquals(12345L, FastNumber.parseInt64(text, 0, 5));
        assertEquals(123456L, FastNumber.parseInt64(text, 0, 6));
        assertEquals(1234567L, FastNumber.parseInt64(text, 0, 7));
        assertEquals(12345678L, FastNumber.parseInt64(text, 0, 8));
        assertEquals(123456789L, FastNumber.parseInt64(text, 0, 9));
        assertEquals(1234567890L, FastNumber.parseInt64(text, 0, 10));
        assertEquals(12345678901L, FastNumber.parseInt64(text, 0, 11));
        assertEquals(123456789012L, FastNumber.parseInt64(text, 0, 12));
        assertEquals(1234567890123L, FastNumber.parseInt64(text, 0, 13));
        assertEquals(12345678901234L, FastNumber.parseInt64(text, 0, 14));
        assertEquals(123456789012345L, FastNumber.parseInt64(text, 0, 15));
        assertEquals(1234567890123456L, FastNumber.parseInt64(text, 0, 16));
        assertEquals(12345678901234567L, FastNumber.parseInt64(text, 0, 17));
        assertEquals(123456789012345678L, FastNumber.parseInt64(text, 0, 18));
        assertEquals(1234567890123456789L, FastNumber.parseInt64(text, 0, 19));
    }

}