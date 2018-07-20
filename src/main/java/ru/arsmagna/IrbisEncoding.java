package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Кодировки.
 */
public final class IrbisEncoding {
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

    private static Charset _windows1251;
}
