package ru.arsmagna;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Кодировки.
 */
public class IrbisEncoding
{
    /**
     * UTF-8.
     * @return UTF-8 encoding.
     */
    public static Charset utf()
    {
        return StandardCharsets.UTF_8;
    }

    /**
     * CP1251.
     * @return CP1251 encoding.
     */
    public static Charset ansi()
    {
        return Charset.forName("windows-1251");
    }
}
