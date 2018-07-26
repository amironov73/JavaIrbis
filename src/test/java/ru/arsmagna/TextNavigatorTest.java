package ru.arsmagna;

import org.junit.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static ru.arsmagna.TextNavigator.EOT;

public class TextNavigatorTest extends TestBase {

    @Test
    public void fromFile_1() throws IOException {
        Path path = getPath("org.mnu");
        Charset ansi = IrbisEncoding.ansi();
        TextNavigator navigator = new TextNavigator(path, ansi);
        String line = navigator.readLine();
        assertEquals("1", line);
        line = navigator.readLine();
        assertEquals("RU", line);
    }

    @Test
    public void clone_1() {
        TextNavigator original = new TextNavigator("Hello");
        original.readChar();
        TextNavigator clone = original.clone();
        assertEquals(original.column(), clone.column());
        assertEquals(original.line(), clone.line());
        assertEquals(original.length(), clone.length());
        assertEquals(original.position(), clone.position());
        assertEquals(original.eot(), clone.eot());
    }

    @Test
    public void charAt_1() {
        TextNavigator navigator = new TextNavigator("Hello");
        assertEquals('H', navigator.charAt(0));
        assertEquals('e', navigator.charAt(1));
        assertEquals('l', navigator.charAt(2));
        assertEquals('l', navigator.charAt(3));
        assertEquals('o', navigator.charAt(4));
        assertEquals(EOT, navigator.charAt(5));
        assertEquals(EOT, navigator.charAt(6));
        assertEquals(EOT, navigator.charAt(-1));
    }

    @Test
    public void readChar_1() {
        TextNavigator navigator = new TextNavigator("Hello");
        assertFalse(navigator.isControl());
        assertFalse(navigator.isDigit());
        assertFalse(navigator.isWhitespace());
        assertFalse(navigator.eot());
        assertTrue(navigator.isLetter());
        assertEquals('H', navigator.readChar());
        assertFalse(navigator.eot());
        assertTrue(navigator.isLetter());
        assertEquals('e', navigator.readChar());
        assertFalse(navigator.eot());
        assertTrue(navigator.isLetter());
        assertEquals('l', navigator.readChar());
        assertFalse(navigator.eot());
        assertTrue(navigator.isLetter());
        assertEquals('l', navigator.readChar());
        assertFalse(navigator.eot());
        assertTrue(navigator.isLetter());
        assertEquals('o', navigator.readChar());
        assertTrue(navigator.eot());
        assertFalse(navigator.isLetter());
        assertEquals(EOT, navigator.readChar());
    }

    @Test
    public void readLine_1() {
        TextNavigator navigator = new TextNavigator("Hello\nworld");
        assertEquals("Hello", navigator.readLine());
        assertEquals("world", navigator.readLine());
        assertNull(navigator.readLine());
    }

    @Test
    public void readLine_2() {
        TextNavigator navigator = new TextNavigator("Hello\r\nworld");
        assertEquals("Hello", navigator.readLine());
        assertEquals("world", navigator.readLine());
        assertNull(navigator.readLine());
    }

    @Test
    public void peekChar_1() {
        TextNavigator navigator = new TextNavigator("Hello");
        assertEquals('H', navigator.peekChar());
        assertEquals('H', navigator.peekChar());
    }

    @Test
    public void RemainingText_1() {
        TextNavigator navigator = new TextNavigator("Hello");
        navigator.readChar();
        assertEquals("ello", navigator.remainingText());
        navigator.readLine();
        assertNull(navigator.remainingText());
    }

}