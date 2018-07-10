package ru.arsmagna;

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
    public static Charset utf() {
        return StandardCharsets.UTF_8;
    }

    /**
     * CP1251.
     *
     * @return CP1251 encoding.
     */
    @NotNull
    public static Charset ansi() {
        return Charset.forName("windows-1251");
    }
}
