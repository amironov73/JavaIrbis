// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Манипуляции с текстом, свойственные ИРБИС.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class IrbisText {

    /**
     * Разделитель строк в ИРБИС.
     */
    public static final String IRBIS_DELIMITER = "\u001F\u001E";

    /**
     * Короткая версия разделителя строк.
     */
    public static final String SHORT_DELIMITER = "\u001E";

    /**
     * Разделитель строк в MSDOS
     */
    public static final String MSDOS_DELIMITER = "\r\n";

    /**
     * Разделитель строк в UNIX.
     */
    public static final String UNIX_DELIMITER = "\n";

    public static final char[] SEARCH_DELIMITERS = {'#'};

    /**
     * Замена разделителей текста с ИРБИС на MS-DOS.
     *
     * @param text Текст с разделителями ИРБИС.
     * @return Преобразованный текст.
     */
    @Nullable
    public static String fromIrbisToDos (@Nullable String text) {
        if (!isNullOrEmpty(text)) {
            text = text.replaceAll(IRBIS_DELIMITER, MSDOS_DELIMITER);
        }

        return text;
    }

    /**
     * Замена разделителей текста с MS-DOS на ИРБИС.
     *
     * @param text Текст с разделителями MS-DOS.
     * @return Преобразованный текст.
     */
    @Nullable
    public static String fromDosToIrbis (@Nullable String text) {
        if (!isNullOrEmpty(text)) {
            text = text.replaceAll(MSDOS_DELIMITER, IRBIS_DELIMITER);
        }

        return text;
    }

    /**
     * Замена разделителей текста с MS-DOS на UNIX.
     *
     * @param text Текст с разделителями MS-DOS.
     * @return Преобразованный текст.
     */
    @Nullable
    public static String fromDosToUnix(@Nullable String text) {
        if (!isNullOrEmpty(text)) {
            text = text.replaceAll(MSDOS_DELIMITER, UNIX_DELIMITER);
        }

        return text;
    }

    /**
     * Разбивка ответа сервера по строкам (полный вариант разделителя).
     *
     * @param text Текст ответа сервера.
     * @return Массив строк.
     */
    @NotNull
    public static String[] fromFullDelimiter (@Nullable String text) {
        if (!isNullOrEmpty(text)) {
            return text.split(IRBIS_DELIMITER);
        }

        return new String[0];
    }

    /**
     * Строки, приходящие в ответ на команду WriteRecord.
     *
     * @param text Текст ответа сервера.
     * @return Строки, в которых содержится модифицированная запись.
     */
    @NotNull
    public static String[] fromShortDelimiter(@Nullable String text) {
        if (!isNullOrEmpty(text)) {
            return text.split(SHORT_DELIMITER);
        }

        return new String[0];
    }
}
