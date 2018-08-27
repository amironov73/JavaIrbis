package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Вспомогательные методы, не вошедшие в прочие классы.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Utility {
    /**
     * Максимальное количество записей в пакете.
     */
    public static final int MAX_PACKET = 32758;

    /**
     * Список баз данных для администратора.
     */
    public static final String ADMINISTRATOR_DATABASE_LIST = "dbnam1.mnu";

    /**
     * Список баз данных для каталогизатора.
     */
    public static final String CATALOGER_DATABASE_LIST = "dbnam2.mnu";

    /**
     * Список баз данных для читателя.
     */
    public static final String READER_DATABASE_LIST = "dbnam3.mnu";

    /**
     * Carriage return and line feed symbols.
     */
    public static final byte[] CRLF = {0x0D, 0x0A};

    /**
     * Line feed symbol.
     */
    @SuppressFBWarnings("MS_MUTABLE_ARRAY")
    public static final byte[] LF = {0x0A};

    /**
     * Return codes that are valid for the ReadRecord command.
     */
    final static int[] READ_RECORD_CODES = {-201, -600, -602, -603};

    /**
     * Return codes that are valid for the ReadTerms command.
     */
    final static int[] READ_TERMS_CODES = {-202, -203, -204};

    //=========================================================================

    // ЛОГИРОВАНИЕ

    private static final Logger _logger = Logger.getLogger(Utility.class.getName());

    public static void logInfo(@NotNull String message) {
        _logger.log(Level.INFO, message);
    }

    public static void logError(@NotNull String message) {
        _logger.log(Level.SEVERE, message);
    }

    //=========================================================================

    // ОТДЕЛЬНЫЕ СИМВОЛЫ

    /**
     * Is digit from 0 to 9?
     */
    @Contract(pure = true)
    public static boolean isArabicDigit(char c) {
        return c >= '0' && c <= '9';
    }

    /**
     * Is letter from A to Z or a to z?
     */
    @Contract(pure = true)
    public static boolean isLatinLetter(char c) {
        return c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z';
    }

    /**
     * Is digit from 0 to 9
     * or letter from A to Z or a to z?
     */
    @Contract(pure = true)
    public static boolean isLatinLetterOrArabicDigit(char c)
    {
        return c >= '0' && c <= '9'
                || c >= 'A' && c <= 'Z'
                || c >= 'a' && c <= 'z';
    }

    /**
     * Is letter from А to Я or а to я?
     */
    @Contract(pure = true)
    public static boolean isRussianLetter(char c) {
        return c >= 'А' && c <= 'я'
                || c == 'Ё' || c == 'ё';
    }

    //=========================================================================

    // СТРОКИ

    /**
     * Превращаем пустую строку в null.
     *
     * @param text Текст для проверки.
     * @return Тот же текст либо null.
     */
    @Nullable
    @Contract(value = "null -> null", pure = true)
    public static String emptyToNull(@Nullable String text) {
        if (text == null || text.equals("")) {
            return null;
        }

        return text;
    }

    /**
     * Выбираем строку, не равную null (если такая вообще есть).
     *
     * @param first Первая строка.
     * @param second Вторая строка.
     * @return Та, которая не равна null.
     */
    @Nullable
    @Contract(value = "!null, _ -> !null", pure = true)
    public static String iif(String first, String second) {
        if (first != null) {
            return first;
        }

        return second;
    }

    /**
     * Выбираем строку, не равную null (если такая вообще есть).
     *
     * @param first Первая строка.
     * @param second Вторая строка.
     * @param third Третья строка.
     * @return Та, которая не равна null.
     */
    @Contract(value = "!null, _, _ -> !null; null, !null, _ -> !null", pure = true)
    public static String iif(String first, String second, String third) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }

        return third;
    }

    /**
     * Строка пустая или равна null?
     *
     * @param text Проверяемая строка.
     * @return true, если пустая или равна null.
     */
    @Contract(value = "null -> true", pure = true)
    public static boolean isNullOrEmpty(@Nullable String text) {
        return text == null || text.length() == 0;
    }

    @Contract(pure = true)
    public static boolean sameString(@Nullable String s1, @Nullable String s2) {
        if (s1 == s2) {
            return true;
        }

        if (s1 == null || s2 == null) {
            return false;
        }

        return s1.compareToIgnoreCase(s2) == 0;
    }

    /**
     * Безопасное сравнение строк (любая из них может быть равна null).
     *
     * @param left Первая строка.
     * @param right Вторая строка.
     * @return Результат сравнения.
     */
    @Contract(pure = true)
    public static int safeCompare(@Nullable String left, @Nullable String right) {
        if (left == null) {
            if (right == null) {
                return 0;
            }
            return -1;
        }

        if (right == null) {
            return 1;
        }

        return left.compareTo(right);
    }

    @NotNull
    @Contract(pure = true)
    public static String nullToEmpty(@Nullable String value) {
        return value == null ? "" : value;
    }

    @Nullable
    public static String nullableToString(@Nullable Object value) {
        return value == null ? null : value.toString();
    }

    @NotNull
    public static String toVisible(@Nullable Object value) {
        return value == null ? "(null)" : value.toString();
    }

    //=========================================================================

    // МАССИВЫ

    @Contract(pure = true)
    public static int find(@NotNull int[] array, int item) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }

        return -1;
    }

    @Contract(pure = true)
    public static int itemAt(@Nullable int[] array, int index) {
        if (array == null || index < 0 || index >= array.length) {
            return 0;
        }

        return array[index];
    }

    //=========================================================================

    // ПОТОКИ

    public static byte[] readToEnd(@NotNull InputStream stream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        while (true) {
            int read = stream.read(buffer);
            if (read <= 0) {
                break;
            }
            result.write(buffer, 0, read);
        }

        return result.toByteArray();
    }

    public static byte[] readAllBytes(@NotNull File file)
            throws IOException {
        try(InputStream stream = new FileInputStream(file)) {
            byte[] result = readToEnd(stream);
            return result;
        }
    }

    public static void copyTo(@NotNull InputStream source, @NotNull OutputStream destination)
            throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int read = source.read(buffer);
            if (read <= 0) {
                break;
            }
            destination.write(buffer, 0, read);
        }
    }

    public static String readTo(StringReader reader, char delimiter)
            throws IOException {
        StringBuilder result = new StringBuilder();

        while (true) {
            int next = reader.read();
            if (next < 0) {
                break;
            }
            char c = (char) next;
            if (c == delimiter) {
                break;
            }
            result.append(c);
        }

        return result.toString();
    }

    /**
     * Network to host byte conversion.
     */
    public static void networkToHost16
    (
            byte[] array,
            int offset
    ) {
        byte temp = array[offset];
        array[offset] = array[offset + 1];
        array[offset + 1] = temp;
    }

    /**
     * Network to host byte conversion.
     */
    public static void networkToHost32
    (
            byte[] array,
            int offset
    ) {
        byte temp1 = array[offset];
        byte temp2 = array[offset + 1];
        array[offset] = array[offset + 3];
        array[offset + 1] = array[offset + 2];
        array[offset + 3] = temp1;
        array[offset + 2] = temp2;
    }

    /**
     * Network to host byte conversion.
     * IRBIS64-oriented!
     */
    public static void networkToHost64
    (
            byte[] array,
            int offset
    ) {
        networkToHost32(array, offset);
        networkToHost32(array, offset + 4);
    }

    /**
     * Host to network byte conversion.
     */
    public static void hostToNetwork16
    (
            byte[] array,
            int offset
    ) {
        byte temp = array[offset];
        array[offset] = array[offset + 1];
        array[offset + 1] = temp;
    }

    /**
     * Host to network byte conversion.
     */
    public static void hostToNetwork32
    (
            byte[] array,
            int offset
    ) {
        byte temp1 = array[offset];
        byte temp2 = array[offset + 1];
        array[offset] = array[offset + 3];
        array[offset + 1] = array[offset + 2];
        array[offset + 3] = temp1;
        array[offset + 2] = temp2;
    }

    /**
     * Host to network byte conversion.
     * IRBIS64-oriented!
     */
    public static void hostToNetwork64
    (
            byte[] array,
            int offset
    ) {
        hostToNetwork32(array, offset);
        hostToNetwork32(array, offset + 4);
    }

    /**
     * Чтение точного числа байт.
     */
    public static byte[] readExact
    (
            InputStream stream,
            int length
    )
            throws IOException {
        byte[] buffer = new byte[length];
        if (stream.read(buffer) != length) {
            throw new IOException();
        }

        return buffer;
    }

    /**
     * Read integer in network byte order.
     */
    public static short readInt16Network
    (
            InputStream stream
    )
            throws IOException {
        byte[] buffer = readExact(stream, 2);
        networkToHost16(buffer, 0);
        short result = toInt16(buffer, 0);

        return result;
    }

    /**
     * Read integer in host byte order.
     */
    public static short readInt16Host
    (
            InputStream stream
    )
            throws IOException {
        byte[] buffer = readExact(stream, 2);
        short result = toInt16(buffer, 0);

        return result;
    }

    /**
     * Read integer in network byte order.
     */
    public static int readInt32Network
    (
            InputStream stream
    )
            throws IOException {
        byte[] buffer = readExact(stream, 4);
        networkToHost32(buffer, 0);
        int result = toInt32(buffer, 0);

        return result;
    }

    /**
     * Read integer in host byte order.
     */
    public static int readInt32Host(InputStream stream) throws IOException {
        byte[] buffer = readExact(stream, 4);
        int result = toInt32(buffer, 0);

        return result;
    }

    /**
     * Read integer in network byte order.
     */
    public static long readInt64Network(InputStream stream) throws IOException {
        byte[] buffer = readExact(stream, 8);
        networkToHost64(buffer, 0);
        long result = toInt64(buffer, 0);

        return result;
    }

    /**
     * Read integer in host byte order.
     */
    public static long readInt64Host(InputStream stream) throws IOException {
        byte[] buffer = readExact(stream, 8);
        long result = toInt64(buffer, 0);

        return result;
    }

    public static void writeInt16Host(@NotNull OutputStream stream, short value)
            throws IOException {
    }

    public static void writeInt16Network(@NotNull OutputStream stream, short value)
        throws IOException {
    }

    public static void writeInt32Host(@NotNull OutputStream stream, int value)
            throws IOException {
    }

    public static void writeInt32Network(@NotNull OutputStream stream, int value)
        throws IOException {
    }

    public static void writeInt64Host(@NotNull OutputStream stream, long value)
            throws IOException {
    }

    public static void writeInt64Network(@NotNull OutputStream stream, long value)
        throws IOException {
    }

    /**
     * Преобразование в 16-битное целое.
     *
     * @param array  Массив байт.
     * @param offset Смещение в массиве.
     * @return Результат преобразования.
     */
    public static short toInt16
    (
            byte[] array,
            int offset
    ) {
        // TODO variant for big-endian

        return (short) (array[offset] | array[offset + 1] << 8);
    }

    /**
     * Преобразование в 32-битное целое.
     *
     * @param array  Массив байт.
     * @param offset Смещение в массиве.
     * @return Результат преобразования.
     */
    public static int toInt32
    (
            byte[] array,
            int offset
    ) {
        // TODO variant for big-endian

        return array[offset]
                | array[offset + 1] << 8
                | array[offset + 2] << 16
                | array[offset + 3] << 24;
    }

    /**
     * Преобразование в 64-битное целое.
     *
     * @param array  Массив байт.
     * @param offset Смещение в массиве.
     * @return Результат преобразования.
     */
    public static long toInt64
    (
            byte[] array,
            int offset
    ) {
        // TODO variant for big-endian

        int first = array[offset]
                | array[offset + 1] << 8
                | array[offset + 2] << 16
                | array[offset + 3] << 24;
        int second = array[offset + 4]
                | array[offset + 5] << 8
                | array[offset + 6] << 16
                | array[offset + 7] << 24;
        return first | ((long) second) << 32;
    }
}
