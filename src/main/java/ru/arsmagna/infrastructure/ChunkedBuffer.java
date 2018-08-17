package ru.arsmagna.infrastructure;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Analog of MemoryStream that uses small memory chunks.
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnusedReturnValue"})
public class ChunkedBuffer {

    /**
     * Default chunk size.
     */
    public static final int DEFAULT_CHUNK_SIZE = 2048;

    /**
     * Chunk of bytes.
     */
    class Chunk {
        public byte[] buffer;
        public Chunk next;

        public Chunk(int size) {
            buffer = new byte[size];
        }
    }

    /**
     * Constructor.
     */
    public ChunkedBuffer() {
        _chunkSize = DEFAULT_CHUNK_SIZE;
    }

    /**
     * Constructor.
     *
     * @param chunkSize size of chunks
     */
    public ChunkedBuffer(int chunkSize) {
        _chunkSize = chunkSize;
    }

    private Chunk _first, _current, _last;
    private final int _chunkSize;
    private int _position, _read;

    private boolean _Advance() {
        if (_current == _last) {
            return false;
        }

        _current = _current.next;
        _read = 0;

        return true;
    }

    private void _AppendChunk() {
        Chunk newChunk = new Chunk(_chunkSize);
        if (_first == null) {
            _first = newChunk;
            _current = newChunk;
        } else {
            _last.next = newChunk;
        }
        _last = newChunk;
        _position = 0;
    }

    /**
     * Chunk size.
     *
     * @return Chunk size
     */
    public int chunkSize() { return _chunkSize; }

    /**
     * End of file reached?
     *
     * @return true of false
     */
    public boolean is_eof() {
        if (_current == null) {
            return true;
        }

        if (_current == _last) {
            return _read >= _position;
        }

        return false;
    }

    /**
     * Total data length.
     *
     * @return Data length
     */
    public int length() {
        int result = 0;
        for (Chunk chunk = _first; chunk != null && chunk != _last;
             chunk = chunk.next) {
            result += _chunkSize;
        }
        result += _position;

        return result;
    }

    /**
     * Copy data from the stream.
     *
     * @param stream     Stream to read
     * @param bufferSize Buffer size
     * @throws IOException Input-output error
     */
    public void copyFrom(InputStream stream, int bufferSize)
            throws IOException {
        byte[] buffer = new byte[bufferSize];
        int read;
        while ((read = stream.read(buffer, 0, buffer.length)) > 0) {
            write(buffer, 0, read);
        }
    }

    /**
     * Peek one byte.
     *
     * @return Negative value on EOF
     */
    public int peek() {
        if (_current == null) {
            return -1;
        }

        if (_current == _last) {
            if (_read >= _position) {
                return -1;
            }
        } else {
            if (_read >= _chunkSize) {
                _Advance();
            }
        }

        return _current.buffer[_read];
    }

    /**
     * Read array of bytes.
     *
     * @param buffer Buffer to use
     * @return Byte count
     */
    public int read(byte[] buffer) {
        return read(buffer, 0, buffer.length);
    }

    /**
     * Read array of bytes.
     *
     * @param buffer Buffer to use
     * @param offset Offset
     * @param count  Bytes to read
     * @return Byte count
     */
    public int read(byte[] buffer, int offset, int count) {
        if (count <= 0) {
            return 0;
        }

        if (_current == null) {
            return 0;
        }

        int total = 0;
        do {
            int remaining = _current == _last
                    ? _position - _read
                    : _chunkSize - _read;
            if (remaining <= 0) {
                if (!_Advance()) {
                    break;
                }
            }

            int portion = Math.min(count, remaining);
            System.arraycopy(_current.buffer, _read,
                    buffer, offset, portion);
            _read += portion;
            offset += portion;
            count -= portion;
            total += portion;
        } while (count > 0);

        return total;
    }

    /**
     * Read one byte.
     *
     * @return Negative value on EOF
     */
    public int readByte() {
        if (_current == null) {
            return -1;
        }

        if (_current == _last) {
            if (_read >= _position) {
                return -1;
            }
        } else {
            if (_read >= _chunkSize) {
                _Advance();
            }
        }

        return _current.buffer[_read++];
    }

    /**
     * Read one line from the current position.
     *
     * @param encoding Encoding to use
     * @return null on EOF
     */
    public String readLine(Charset encoding) {
        if (is_eof()) {
            return null;
        }

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte found = 0;
        while (found == 0) {
            byte[] buffer = _current.buffer;
            int stop = _current == _last ? _position : _chunkSize;
            int head = _read;
            for (; head < stop; head++) {
                byte c = buffer[head];
                if (c == '\r' || c == '\n') {
                    found = c;
                    break;
                }
            }
            result.write(buffer, _read, head - _read);
            _read = head;
            if (found != 0) {
                _read++;
            } else {
                if (!_Advance()) {
                    break;
                }
            }
        }

        if (found == '\r') {
            if (peek() == '\n') {
                readByte();
            }
        }

        return new String(result.toByteArray(), encoding);
    }

    /**
     * Rewind to the beginning.
     */
    public void rewind() {
        _current = _first;
        _read = 0;
    }

    /**
     * Get internal buffers.
     *
     * @param prefix Count of empty buffers to prepend
     * @return Array of buffers
     */
    public byte[][] toArrays(int prefix) {
        ArrayList<byte[]> result = new ArrayList<>();

        for (int i = 0; i < prefix; i++) {
            result.add(new byte[0]);
        }

        for (Chunk chunk = _first; chunk != null && chunk != _last;
             chunk = chunk.next) {
            result.add(chunk.buffer);
        }

        if (_position != 0) {
            byte[] chunk = new byte[_position];
            System.arraycopy(_last.buffer, 0, chunk, 0, _position);
            result.add(chunk);
        }

        return result.toArray(new byte[0][]);
    }

    /**
     * Get all data as one big array of bytes.
     *
     * @return All data
     */
    public byte[] toBigArray() {
        int total = length();
        byte[] result = new byte[total];
        int offset = 0;
        for (Chunk chunk = _first; chunk != null && chunk != _last;
             chunk = chunk.next) {
            System.arraycopy(chunk.buffer, 0, result, offset, _chunkSize);
            offset += _chunkSize;
        }

        if (_position != 0) {
            System.arraycopy(_last.buffer, 0, result, offset, _position);
        }

        return result;
    }

    /**
     * Write a block of bytes to the current stream
     * using data read from a buffer.
     *
     * @param buffer Buffer to use
     */
    public void write(byte[] buffer) {
        write(buffer, 0, buffer.length);
    }

    /**
     * Write a block of bytes to the current stream
     * using data read from a buffer.
     *
     * @param buffer Buffer to use
     * @param offset Offset
     * @param count Byte count
     */
    public void write(byte[] buffer, int offset, int count) {
        if (count <= 0) {
            return;
        }

        if (_first == null) {
            _AppendChunk();
        }

        do {
            int free = _chunkSize - _position;
            if (free == 0) {
                _AppendChunk();
                free = _chunkSize;
            }

            int portion = Math.min(count, free);
            System.arraycopy(buffer, offset, _last.buffer, _position, portion);
            _position += portion;
            count -= portion;
            offset += portion;
        } while (count > 0);
    }

    /**
     * Write the text with encoding.
     *
     * @param text Text to write
     * @param encoding Encoding to use
     */
    public void write(String text, Charset encoding) {
        ByteBuffer buffer = encoding.encode(text);
        write(buffer.array());
    }

    /**
     * Write a byte to the current stream at the current position.
     * @param value Byte to write
     */
    public void writeByte(byte value) {
        if (_first == null) {
            _AppendChunk();
        }

        if (_position >= _chunkSize) {
            _AppendChunk();
        }

        _last.buffer[_position++] = value;
    }
}
