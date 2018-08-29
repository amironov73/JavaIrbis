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
    public void clone_1() {
        NumberText original = new NumberText();
        NumberText clone = original.clone();
        assertEquals(original.size(), clone.size());
    }

    @Test
    public void clone_2() {
        NumberText original = new NumberText("hello1world2");
        NumberText clone = original.clone();
        assertEquals(original.size(), clone.size());
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
    public void parse_1() {
        NumberText number = new NumberText();
        number.parse(null);
        assertTrue(number.isEmpty());
        number.parse("");
        assertTrue(number.isEmpty());
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