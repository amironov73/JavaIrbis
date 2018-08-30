package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Кодировки.
 */
public final class IrbisEncoding {

    /**
     * Get byte-length of the text.
     *
     * @param text Text to measure
     * @param encoding Encoding
     * @return Length
     */
    @Contract(pure = true)
    public static int getByteCount(@Nullable String text, @NotNull Charset encoding) {
        if (text == null) {
            return 0;
        }
        return text.getBytes(encoding).length;
    }

    /**
     * UTF-8.
     *
     * @return UTF-8 encoding.
     */
    @NotNull
    @Contract(pure = true)
    @SuppressWarnings("SameReturnValue")
    public static Charset utf() {
        return StandardCharsets.UTF_8;
    }

    /**
     * CP1251.
     *
     * @return CP1251 encoding.
     */
    @NotNull
    public static synchronized Charset ansi() {
        if (_windows1251 == null) {
            _windows1251 = Charset.forName("windows-1251");
        }

        return _windows1251;
    }

    //=========================================================================

    public static byte[] encode(@NotNull Charset encoding, @NotNull String text) {
        ByteBuffer buffer = encoding.encode(text);
        byte[] result = buffer.array();
        if (buffer.limit() != result.length) {
            result = Arrays.copyOf(result, buffer.limit());
        }

        return result;
    }

    //=========================================================================

    private static Charset _windows1251;
}
