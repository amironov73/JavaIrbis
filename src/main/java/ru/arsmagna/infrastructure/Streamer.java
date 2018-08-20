package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@SuppressWarnings("ALL")
public class Streamer implements Closeable {

    private final InputStream _stream;
    private final int _bufferSize;
    private final byte[] _buffer;
    private int _position, _limit;

    public Streamer(@NotNull InputStream stream, int bufferSize)
            throws IOException {
        _stream = stream;
        _bufferSize = bufferSize;
        _buffer = new byte[bufferSize];
        _position = 0;
        _limit = 0;
        advance();
    }

    private boolean advance() throws IOException {
        _limit = _stream.read(_buffer, 0, _bufferSize);
        _position = 0;

        return _limit > 0;
    }

    public boolean is_eof() throws IOException {
        return _position >= _limit && !advance();
    }

    public InputStream innerStream() {
        return _stream;
    }

    public int peekByte() throws IOException {
        if (_position < _limit) {
            return _buffer[_position];
        }

        if (!advance()) {
            return -1;
        }

        return _buffer[_position];
    }

    public int read(@NotNull byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    public int read(@NotNull byte[] buffer, int offset, int count)
            throws IOException {
        int total = 0;
        while (count > 0) {
            int tail = _limit - _position;
            int portion = Math.min(tail, count);
            if (portion <= 0) {
                if (!advance()) {
                    break;
                }
                continue;
            }

            System.arraycopy(_buffer, _position, buffer, offset, portion);
            count -= portion;
            offset += portion;
            _position += portion;
            total += portion;
        }

        return total;
    }

    public int readByte() throws IOException {
        if (_position < _limit) {
            return _buffer[_position++];
        }

        if (!advance()) {
            return -1;
        }

        return _buffer[_position++];
    }

    public String readLine(@NotNull Charset encoding) throws IOException {
        if (is_eof()) {
            return null;
        }

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte found = 0;
        while (found == 0) {
            while (_position < _limit) {
                byte one = _buffer[_position++];
                if (one == '\r' || one == '\n') {
                    found = one;
                    break;
                }
                result.write(one);
            }

            if (found == 0 && !advance()) {
                break;
            }
        }

        if (found == '\r') {
            int peek = peekByte();
            if (peek == '\n') {
                readByte();
            }
        }

        return new String(result.toByteArray(), encoding);
    }

    public byte[] toArray() throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        if (_position < _limit) {
            result.write(_buffer, _position, _limit - _position);
            _position = _limit;
        }

        byte[] temp = new byte[1024];
        int len;
        while ((len = _stream.read(temp)) > 0) {
            result.write(temp, 0, len);
        }

        return result.toByteArray();
    }

    @Override
    public void close() throws IOException {
        _stream.close();
    }
}
