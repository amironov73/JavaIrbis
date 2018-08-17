package ru.arsmagna.infrastructure;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ChunkedBufferTest {

    @Test
    public void construction_1() {
        int chunkSize = 4096;
        ChunkedBuffer buffer = new ChunkedBuffer(chunkSize);
        assertEquals(chunkSize, buffer.chunkSize());
        assertEquals(0, buffer.length());
        assertTrue(buffer.is_eof());
    }

    @Test
    public void construction_2() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        assertEquals(ChunkedBuffer.DEFAULT_CHUNK_SIZE, buffer.chunkSize());
        assertEquals(0, buffer.length());
        assertTrue(buffer.is_eof());
    }

    @Test
    public void copy_from_1() throws IOException {
        ByteArrayOutputStream memory = new ByteArrayOutputStream();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 256; j++) {
                memory.write(j);
            }
        }

        byte[] original = memory.toByteArray();
        ByteArrayInputStream stream = new ByteArrayInputStream(original);

        ChunkedBuffer buffer = new ChunkedBuffer();
        buffer.copyFrom(stream, 1024);
        assertEquals(20 * 256, buffer.length());
        byte[] copy = buffer.toBigArray();
        assertArrayEquals(original, copy);
    }

    @Test
    public void read_1() throws IOException {
        ByteArrayOutputStream memory = new ByteArrayOutputStream();
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 256; j++) {
                memory.write(j);
            }
        }

        byte[] original = memory.toByteArray();
        ByteArrayInputStream stream = new ByteArrayInputStream(original);

        ChunkedBuffer buffer = new ChunkedBuffer();
        buffer.copyFrom(stream, 1024);
        for (int i = 0; i < 20; i++) {
            assertFalse(buffer.is_eof());
            byte[] bytes = new byte[256];
            assertEquals(256, buffer.read(bytes));
            for (int j = 0; j < 256; j++) {
                int value = j;
                if (value >= 128) {
                    value = value - 256;
                }
                assertEquals(value, bytes[j]);
            }
        }
        assertTrue(buffer.is_eof());
    }

    @Test
    public void read_2() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        byte[] bytes = new byte[3];
        assertEquals(0, buffer.read(bytes, 0, -1));
        assertEquals(0, buffer.read(bytes, 0, 0));
        assertEquals(3, buffer.read(bytes, 0, 3));
        assertEquals(0, buffer.read(bytes, 0, 3));
    }

    @Test
    public void read_3() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        byte[] bytes = new byte[3];
        assertEquals(0, buffer.read(bytes, 0, -1));
        assertEquals(0, buffer.read(bytes, 0, 0));
        assertEquals(0, buffer.read(bytes, 0, 3));
        assertEquals(0, buffer.read(bytes, 0, 3));
    }

    @Test
    public void peek_1() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        assertEquals(1, buffer.peek());
        assertEquals(1, buffer.peek());
        assertEquals(1, buffer.readByte());
        assertEquals(2, buffer.peek());
        assertEquals(2, buffer.peek());
        assertEquals(2, buffer.readByte());
        assertEquals(3, buffer.peek());
        assertEquals(3, buffer.peek());
        assertEquals(3, buffer.readByte());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.readByte());
    }

    @Test
    public void peek_2() throws IOException {
        ByteArrayOutputStream memory = new ByteArrayOutputStream();
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 256; j++) {
                memory.write(j);
            }
        }

        byte[] original = memory.toByteArray();
        ByteArrayInputStream stream = new ByteArrayInputStream(original);

        ChunkedBuffer buffer = new ChunkedBuffer();
        buffer.copyFrom(stream, 1024);
        assertEquals(0, buffer.peek());
        assertEquals(0, buffer.peek());
        assertEquals(0, buffer.readByte());
        int size = ChunkedBuffer.DEFAULT_CHUNK_SIZE - 1;
        byte[] bytes = new byte[size];
        assertEquals(size, buffer.read(bytes));
        assertEquals(0, buffer.peek());
        assertEquals(0, buffer.peek());
        assertEquals(0, buffer.readByte());
        assertEquals(size, buffer.read(bytes));
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.readByte());
    }

    @Test
    public void peek_3() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        buffer.writeByte((byte) 4);
        buffer.writeByte((byte) 5);
        buffer.writeByte((byte) 6);
        assertEquals(1, buffer.peek());
        assertEquals(1, buffer.peek());
        assertEquals(1, buffer.readByte());
        assertEquals(2, buffer.peek());
        assertEquals(2, buffer.peek());
        assertEquals(2, buffer.readByte());
        assertEquals(3, buffer.peek());
        assertEquals(3, buffer.peek());
        assertEquals(3, buffer.readByte());
        assertEquals(4, buffer.peek());
        assertEquals(4, buffer.peek());
        assertEquals(4, buffer.readByte());
        assertEquals(5, buffer.peek());
        assertEquals(5, buffer.peek());
        assertEquals(5, buffer.readByte());
        assertEquals(6, buffer.peek());
        assertEquals(6, buffer.peek());
        assertEquals(6, buffer.readByte());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.readByte());
    }

    @Test
    public void readByte_1() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.writeByte((byte)1);
        buffer.writeByte((byte)2);
        buffer.writeByte((byte)3);
        buffer.writeByte((byte)4);
        buffer.writeByte((byte)5);
        buffer.writeByte((byte)6);
        assertEquals(1, buffer.readByte());
        assertEquals(2, buffer.readByte());
        assertEquals(3, buffer.readByte());
        assertEquals(4, buffer.readByte());
        assertEquals(5, buffer.readByte());
        assertEquals(6, buffer.readByte());
        assertEquals(-1, buffer.readByte());
    }

    @Test
    public void readByte_2() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.readByte());
    }

    @Test
    public void readByte_3() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        buffer.writeByte((byte) 1);
        assertEquals(1, buffer.peek());
        assertEquals(1, buffer.peek());
        assertEquals(1, buffer.readByte());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.peek());
        assertEquals(-1, buffer.readByte());
    }

    @Test
    public void toArrays_1() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        assertEquals(0, buffer.length());
        byte[][] arrays = buffer.toArrays(0);
        assertEquals(0, arrays.length);
        arrays = buffer.toArrays(1);
        assertEquals(1, arrays.length);
        assertEquals(0, arrays[0].length);
        arrays = buffer.toArrays(2);
        assertEquals(2, arrays.length);
        assertEquals(0, arrays[0].length);
        assertEquals(0, arrays[1].length);
    }

    @Test
    public void toArrays_2() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        buffer.writeByte((byte) 4);
        buffer.writeByte((byte) 5);
        assertEquals(5, buffer.length());
        byte[][] arrays = buffer.toArrays(0);
        assertEquals(3, arrays.length);
        assertEquals(2, arrays[0].length);
        assertEquals(1, arrays[0][0]);
        assertEquals(2, arrays[0][1]);
        assertEquals(2, arrays[1].length);
        assertEquals(3, arrays[1][0]);
        assertEquals(4, arrays[1][1]);
        assertEquals(1, arrays[2].length);
        assertEquals(5, arrays[2][0]);

        arrays = buffer.toArrays(1);
        assertEquals(4, arrays.length);
        assertEquals(0, arrays[0].length);
        assertEquals(2, arrays[1].length);
        assertEquals(2, arrays[2].length);
        assertEquals(1, arrays[3].length);
    }

    @Test
    public void toBigArray_1() {
        ChunkedBuffer buffer = new ChunkedBuffer();
        assertEquals(0, buffer.length());
        byte[] array = buffer.toBigArray();
        assertEquals(0, array.length);
    }

    @Test
    public void toBigArray_2() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.writeByte((byte) 1);
        buffer.writeByte((byte) 2);
        buffer.writeByte((byte) 3);
        buffer.writeByte((byte) 4);
        buffer.writeByte((byte) 5);
        assertEquals(5, buffer.length());
        byte[] array = buffer.toBigArray();
        assertEquals(5, array.length);
        assertEquals(1, array[0]);
        assertEquals(2, array[1]);
        assertEquals(3, array[2]);
        assertEquals(4, array[3]);
        assertEquals(5, array[4]);
    }

    @Test
    public void write_1() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        assertEquals(0, buffer.length());
        byte[] array = new byte[0];
        buffer.write(array);
        assertEquals(0, buffer.length());
        buffer.write(array, 0, -1);
        assertEquals(0, buffer.length());
        buffer.write(array, 0, 0);
        assertEquals(0, buffer.length());
    }

    @Test
    public void write_2() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        assertEquals(0, buffer.length());
        byte[] array = { 1, 2, 3, 4, 5 };
        buffer.write(array);
        assertEquals(5, buffer.length());
    }

    @Test
    public void write_3() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        assertEquals(0, buffer.length());
        buffer.write("Hello", StandardCharsets.US_ASCII);
        assertEquals(5, buffer.length());
        byte[] array = buffer.toBigArray();
        assertEquals(5, array.length);
        assertEquals(72, array[0]);
        assertEquals(101, array[1]);
        assertEquals(108, array[2]);
        assertEquals(108, array[3]);
        assertEquals(111, array[4]);
    }

    @Test
    public void write_4() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        assertEquals(0, buffer.length());
        buffer.write("", StandardCharsets.US_ASCII);
        assertEquals(0, buffer.length());
        byte[] array = buffer.toBigArray();
        assertEquals(0, array.length);
    }

    @Test
    public void readLine_1() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        String line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
    }

    @Test
    public void readLine_2() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        String expected = "Hello";
        buffer.write(expected, StandardCharsets.US_ASCII);
        String actual = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals(expected, actual);
    }

    @Test
    public void readLine_3() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.write("Hello\nworld", StandardCharsets.US_ASCII);
        String line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("Hello", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("world", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
    }

    @Test
    public void readLine_4() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.write("Hello\r\nworld", StandardCharsets.US_ASCII);
        String line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("Hello", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("world", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
    }

    @Test
    public void readLine_5() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.write("Hello\rworld", StandardCharsets.US_ASCII);
        String line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("Hello", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("world", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
    }

    @Test
    public void readLine_6() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.write("Hello\rworld\n", StandardCharsets.US_ASCII);
        String line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("Hello", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("world", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
    }

    @Test
    public void readLine_7() {
        ChunkedBuffer buffer = new ChunkedBuffer(2);
        buffer.write("\n\r\n\r", StandardCharsets.US_ASCII);
        String line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertEquals("", line);
        line = buffer.readLine(StandardCharsets.US_ASCII);
        assertNull(line);
    }
}