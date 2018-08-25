package ru.arsmagna.infrastructure;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SuppressWarnings("UnnecessaryLocalVariable")
public class StreamerTest {

    private ByteArrayInputStream getStream(String text) {
        ByteBuffer buffer = StandardCharsets.UTF_8.encode(text);
        byte[] bytes = Arrays.copyOf(buffer.array(), buffer.limit());
        ByteArrayInputStream result = new ByteArrayInputStream(bytes);

        return result;
    }

    private ByteArrayInputStream getStream() {
        return getStream("Hello,\r\nWorld!");
    }

    @Test
    public void construction_1() throws IOException {
        ByteArrayInputStream memory = getStream();
        Streamer streamer = new Streamer(memory, 4);
        assertSame(memory, streamer.innerStream());
        assertFalse(streamer.is_eof());
    }

    @Test
    public void peekByte_1() throws IOException {
        ByteArrayInputStream memory = getStream("\01\02\03");
        Streamer streamer = new Streamer(memory, 2);
        assertEquals(1, streamer.peekByte());
        assertEquals(1, streamer.peekByte());
        assertEquals(1, streamer.readByte());
        assertEquals(2, streamer.peekByte());
        assertEquals(2, streamer.peekByte());
        assertEquals(2, streamer.readByte());
        assertEquals(3, streamer.peekByte());
        assertEquals(3, streamer.peekByte());
        assertEquals(3, streamer.readByte());
        assertEquals(-1, streamer.peekByte());
        assertEquals(-1, streamer.peekByte());
        assertEquals(-1, streamer.readByte());
    }

    @Test
    public void read_1() throws IOException {
        ByteArrayInputStream memory = getStream("\01\02\03\04\05\06");
        Streamer streamer = new Streamer(memory, 2);
        byte[] buffer = new byte[4];
        assertEquals(4, streamer.read(buffer));
        assertEquals(1, buffer[0]);
        assertEquals(2, buffer[1]);
        assertEquals(3, buffer[2]);
        assertEquals(4, buffer[3]);
        assertEquals(2, streamer.read(buffer));
        assertEquals(5, buffer[0]);
        assertEquals(6, buffer[1]);
        assertEquals(3, buffer[2]);
        assertEquals(0, streamer.read(buffer));
        assertEquals(0, streamer.read(buffer));
        assertEquals(5, buffer[0]);
    }

    @Test
    public void read_2() throws IOException {
        ByteArrayInputStream memory = getStream();
        Streamer streamer = new Streamer(memory, 2);
        byte[] buffer = new byte[4];
        assertEquals(0, streamer.read(buffer, 0, -1));
        assertEquals(0, streamer.read(buffer, 0, 0));
    }

    @Test
    public void readByte_1() throws IOException {
        ByteArrayInputStream memory = getStream("\01\02\03");
        Streamer streamer = new Streamer(memory, 2);
        assertEquals(1, streamer.readByte());
        assertEquals(2, streamer.readByte());
        assertEquals(3, streamer.readByte());
        assertEquals(-1, streamer.readByte());
    }

    @Test
    public void readLine_1() throws IOException {
        ByteArrayInputStream memory = getStream();
        Streamer streamer = new Streamer(memory, 4);
        assertFalse(streamer.is_eof());
        assertEquals("Hello,", streamer.readLine(StandardCharsets.UTF_8));
        assertFalse(streamer.is_eof());
        assertEquals("World!", streamer.readLine(StandardCharsets.UTF_8));
        assertTrue(streamer.is_eof());
        assertNull(streamer.readLine(StandardCharsets.UTF_8));
    }

    @Test
    public void dispose_1() throws IOException {
        InputStream mock = mock(InputStream.class);
        Streamer streamer = new Streamer(mock, 4);
        streamer.close();
        verify(mock).close();
    }

    @Test
    public void toArray_1() throws IOException {
        ByteArrayInputStream memory = getStream();
        Streamer streamer = new Streamer(memory, 4);
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 44, 13, 10, 87, 111, 114, 108, 100, 33};
        byte[] actual = streamer.toArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void toArray_2() throws IOException {
        byte[] expected = new byte[0];
        ByteArrayInputStream memory = new ByteArrayInputStream(expected);
        Streamer streamer = new Streamer(memory, 4);
        byte[] actual = streamer.toArray();
        assertArrayEquals(expected, actual);
    }
}