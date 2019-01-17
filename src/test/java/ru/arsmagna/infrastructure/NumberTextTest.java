// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberTextTest {

    @Test
    public void construction_1() {
        NumberText number = new NumberText();
        assertTrue(number.isEmpty());
    }

    @Test
    public void construction_2() {
        NumberText number = new NumberText("hello1");
        assertFalse(number.isEmpty());
    }

    @Test
    public void construction_3() {
        NumberText number = new NumberText("hello11world22");
        assertEquals(2, number.size());
    }

    @Test
    public void append_1() {
        NumberText number = new NumberText();
        assertEquals(0, number.size());
        number.append("hello", 1, 0);
        assertEquals(1, number.size());
        number.append("world", 2, 0);
        assertEquals(2, number.size());
    }

    @Test
    public void append_2() {
        NumberText number = new NumberText();
        number.append("hello");
        number.append(1);
        assertEquals(2, number.size());
        assertEquals("hello1", number.toString());
    }

    @Test
    public void append_3() {
        NumberText number = new NumberText();
        number.append("hello").append(1).append("world").append(2);
        assertEquals(4, number.size());
        assertEquals("hello1world2", number.toString());
        assertTrue(number.havePrefix(0));
        assertFalse(number.havePrefix(1));
        assertTrue(number.havePrefix(2));
        assertFalse(number.havePrefix(3));
        assertFalse(number.haveValue(0));
        assertTrue(number.haveValue(1));
        assertFalse(number.haveValue(2));
        assertTrue(number.haveValue(3));
    }

    @Test
    public void append_4() {
        NumberText number = new NumberText();
        number.append("hello", 1, 0)
                .append("world", 2, 0);
        assertEquals(2, number.size());
        assertEquals("hello1world2", number.toString());
        assertTrue(number.havePrefix(0));
        assertTrue(number.haveValue(0));
        assertTrue(number.havePrefix(1));
        assertTrue(number.haveValue(1));
    }

    @Test
    public void clone_1() {
        NumberText original = new NumberText();
        NumberText clone = original.clone();
        assertEquals(original.isEmpty(), clone.isEmpty());
        assertEquals(original.size(), clone.size());
    }

    @Test
    public void clone_2() {
        NumberText original = new NumberText("hello1again2");
        NumberText clone = original.clone();
        assertEquals(original.isEmpty(), clone.isEmpty());
        assertEquals(original.size(), clone.size());
        assertEquals(original.getPrefix(0), clone.getPrefix(0));
        assertEquals(original.getPrefix(1), clone.getPrefix(1));
        assertEquals(original.getValue(0), clone.getValue(0));
        assertEquals(original.getValue(1), clone.getValue(1));
    }

    @Test
    public void increment_1() {
        NumberText number = new NumberText("hello2");
        NumberText result = number.increment();
        assertEquals("hello3", number.toString());
    }

    @Test
    public void increment_2() {
        NumberText number = new NumberText("hello2world3");
        NumberText result = number.increment();
        assertEquals("hello2world4", number.toString());
    }

    @Test
    public void compare_1() {
        NumberText first = new NumberText("hello1");
        NumberText second = new NumberText("hello2");
        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
    }

    @Test
    public void compare_2() {
        NumberText first = new NumberText("hello1");
        NumberText second = new NumberText("hello1world");
        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
    }

    @Test
    public void compare_3() {
        NumberText first = new NumberText("hello1");
        NumberText second = new NumberText().append("hello", 1, 0);
        assertTrue(first.compareTo(second) == 0);
        assertTrue(second.compareTo(first) == 0);
    }

    @Test
    public void compare_4() {
        NumberText first = new NumberText();
        NumberText second = new NumberText();
        assertEquals(0, first.compareTo(second));
    }

    @Test
    public void equals_1() {
        NumberText first = new NumberText();
        NumberText second = new NumberText();
        assertEquals(first, second);
    }

    @Test
    public void equals_2() {
        NumberText first = new NumberText("hello1");
        NumberText second = new NumberText("hello01");
        assertEquals(first, second);
    }

    @Test
    public void hashCode_1() {
        NumberText first = new NumberText();
        NumberText second = new NumberText();
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void hashCode_2() {
        NumberText first = new NumberText("hello1");
        NumberText second = new NumberText("hello01");
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    public void parse_1() {
        NumberText number = new NumberText();
        number.parse(null);
        assertTrue(number.isEmpty());
        number.parse("");
        assertTrue(number.isEmpty());
    }

    @Test
    public void parse_2() {
        NumberText number = new NumberText("hello1");
        assertFalse(number.isEmpty());
        assertEquals(1, number.size());
        assertEquals("hello", number.getPrefix(0));
        assertEquals(1L, number.getValue(0));
    }

    @Test
    public void parse_3() {
        NumberText number = new NumberText("hello1again2");
        assertFalse(number.isEmpty());
        assertEquals(2, number.size());
        assertEquals("hello", number.getPrefix(0));
        assertEquals(1L, number.getValue(0));
        assertEquals("again", number.getPrefix(1));
        assertEquals(2L, number.getValue(1));
    }

    @Test
    public void parse_4() {
        NumberText number = new NumberText(null);
        assertTrue(number.isEmpty());
        assertEquals(0, number.size());
    }

    @Test
    public void parse_5() {
        NumberText number = new NumberText("hello");
        assertFalse(number.isEmpty());
        assertEquals(1, number.size());
        assertTrue(number.havePrefix(0));
        assertFalse(number.haveValue(0));
    }

    @Test
    public void parse_6() {
        NumberText number = new NumberText("333");
        assertFalse(number.isEmpty());
        assertEquals(1, number.size());
        assertFalse(number.havePrefix(0));
        assertTrue(number.haveValue(0));
    }

    @Test
    public void removeValue_1() {
        NumberText before = new NumberText("hello1world2");
        NumberText after = before.removeValue(1);
        assertEquals(before, after);
        assertFalse(before.haveValue(1));
        assertEquals("hello1world", before.toString());
    }

    @Test
    public void setPrefix_1() {
        NumberText before = new NumberText("hello1world2");
        NumberText after = before.setPrefix(1, "java");
        assertEquals(before, after);
        assertEquals("java", before.getPrefix(1));
        assertEquals("hello1java2", before.toString());
    }

    @Test
    public void setValue_1() {
        NumberText before = new NumberText("hello1world2");
        NumberText after = before.setValue(1, 222);
        assertEquals(before, after);
        assertEquals(222L, before.getValue(1));
        assertEquals("hello1world222", before.toString());
    }

    @Test
    public void toString_1() {
        NumberText number = new NumberText();
        assertEquals("", number.toString());
    }

    @Test
    public void toString_2() {
        NumberText number = new NumberText("hello0001");
        assertEquals("hello0001", number.toString());
    }
}