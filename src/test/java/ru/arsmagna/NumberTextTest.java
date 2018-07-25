package ru.arsmagna;

import org.junit.Test;

import static org.junit.Assert.*;

public class NumberTextTest {

    @Test
    public void construction_1() {
        NumberText number = new NumberText();
        assertTrue(number.isEmpty());
    }

    @Test
    public void append_1() {
        NumberText number = new NumberText();
        number.append("hello").append(1).append("world").append(2);
        assertEquals(4, number.length());
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
    public void append_2() {
        NumberText number = new NumberText();
        number.append("hello", 1, 0)
                .append("world", 2, 0);
        assertEquals(2, number.length());
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
        assertEquals(original.length(), clone.length());
    }

    @Test
    public void clone_2() {
        NumberText original = NumberText.parse("hello1again2");
        NumberText clone = original.clone();
        assertEquals(original.isEmpty(), clone.isEmpty());
        assertEquals(original.length(), clone.length());
        assertEquals(original.getPrefix(0), clone.getPrefix(0));
        assertEquals(original.getPrefix(1), clone.getPrefix(1));
        assertEquals(original.getValue(0), clone.getValue(0));
        assertEquals(original.getValue(1), clone.getValue(1));
    }

    @Test
    public void compare_1() {
        NumberText left = new NumberText();
        NumberText right = new NumberText();
        assertEquals(0, left.compareTo(right));
    }

    @Test
    public void compare_2() {
        NumberText left = new NumberText();
        NumberText right = NumberText.parse("hello1");
        assertTrue(left.compareTo(right) < 0);
    }

    @Test
    public void compare_3() {
        NumberText left = NumberText.parse("hello1");
        NumberText right = NumberText.parse("hello1");
        assertEquals(0, left.compareTo(right));
    }

    @Test
    public void compare_4() {
        NumberText left = NumberText.parse("hello1");
        NumberText right = NumberText.parse("hello2");
        assertTrue(left.compareTo(right) < 0);
    }

    @Test
    public void increment_1() {
        NumberText before = NumberText.parse("hello1");
        NumberText after = before.increment(10);
        assertEquals(before, after);
        assertEquals("hello11", after.toString());
    }

    @Test
    public void increment_2() {
        NumberText before = NumberText.parse("hello1world2");
        NumberText after = before.increment(10);
        assertEquals(before, after);
        assertEquals("hello1world12", after.toString());
    }

    @Test
    public void increment_3() {
        NumberText before = NumberText.parse("hello1world2");
        NumberText after = before.increment(0, 10);
        assertEquals(before, after);
        assertEquals("hello11world2", after.toString());
    }

    @Test
    public void increment_4() {
        NumberText before = NumberText.parse("hello1world2");
        NumberText after = before.increment();
        assertEquals(before, after);
        assertEquals("hello1world3", after.toString());
    }

    @Test
    public void parse_1() {
        NumberText number = NumberText.parse("hello1");
        assertFalse(number.isEmpty());
        assertEquals(1, number.length());
        assertEquals("hello", number.getPrefix(0));
        assertEquals(1L, number.getValue(0));
    }

    @Test
    public void parse_2() {
        NumberText number = NumberText.parse("hello1again2");
        assertFalse(number.isEmpty());
        assertEquals(2, number.length());
        assertEquals("hello", number.getPrefix(0));
        assertEquals(1L, number.getValue(0));
        assertEquals("again", number.getPrefix(1));
        assertEquals(2L, number.getValue(1));
    }

    @Test
    public void parse_3() {
        NumberText number = NumberText.parse(null);
        assertTrue(number.isEmpty());
        assertEquals(0, number.length());
    }

    @Test
    public void parse_4() {
        NumberText number = NumberText.parse("hello");
        assertFalse(number.isEmpty());
        assertEquals(1, number.length());
        assertTrue(number.havePrefix(0));
        assertFalse(number.haveValue(0));
    }

    @Test
    public void parse_5() {
        NumberText number = NumberText.parse("333");
        assertFalse(number.isEmpty());
        assertEquals(1, number.length());
        assertFalse(number.havePrefix(0));
        assertTrue(number.haveValue(0));
    }

    @Test
    public void removeValue_1() {
        NumberText before = NumberText.parse("hello1world2");
        NumberText after = before.removeValue(1);
        assertEquals(before, after);
        assertFalse(before.haveValue(1));
        assertEquals("hello1world", before.toString());
    }

    @Test
    public void setPrefix_1() {
        NumberText before = NumberText.parse("hello1world2");
        NumberText after = before.setPrefix(1, "java");
        assertEquals(before, after);
        assertEquals("java", before.getPrefix(1));
        assertEquals("hello1java2", before.toString());
    }

    @Test
    public void setValue_1() {
        NumberText before = NumberText.parse("hello1world2");
        NumberText after = before.setValue(1, 222);
        assertEquals(before, after);
        assertEquals(222L, before.getValue(1));
        assertEquals("hello1world222", before.toString());
    }

}