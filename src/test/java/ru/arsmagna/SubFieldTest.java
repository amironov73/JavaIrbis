// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.junit.Test;

import static org.junit.Assert.*;

public class SubFieldTest {
    @Test
    public void clone_1() {
        SubField first = new SubField('a', "Some text");
        first.userData = "User data";
        SubField second = first.clone();
        assertEquals(first.code, second.code);
        assertEquals(first.value, second.value);
        assertNull(second.userData);
    }

    @Test
    public void compare_1() {
        SubField first = new SubField('a', "Some text");
        SubField second = new SubField('b', "Other text");
        assertTrue(SubField.compare(first, second) < 0);
    }

    @Test
    public void compareCodes_1() {
        assertTrue(SubField.compareCodes('a', 'b') < 0);
        assertTrue(SubField.compareCodes('a', 'A') == 0);
    }

    @Test
    public void normalize_1() {
        assertEquals('a', SubField.normalize('a'));
        assertEquals('a', SubField.normalize('A'));
    }

    @Test
    public void toString_1() {
        SubField subField = new SubField('a', "Some text");
        assertEquals("^aSome text", subField.toString());
    }
}