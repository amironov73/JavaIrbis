package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

/**
 * Вспомогательные методы, не вошедшие в прочие классы.
 */
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

    /**
     * Превращаем пустую строку в null.
     *
     * @param text Текст для проверки.
     * @return Тот же текст либо null.
     */
    @Nullable
    public static String emptyToNull (@Nullable String text) {
        if (text == null || text.equals("")) {
            return null;
        }

        return text;
    }

    public static int find
            (
                    int[] array,
                    int item
            ) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == item) {
                return i;
            }
        }

        return -1;
    }

    @Nullable
    public static String iif  (String first, String second) {
        if (first != null) { return first; }

        return second;
    }

    public static String iif
            (
                    String first,
                    String second,
                    String third
            ) {
        if (first != null) {
            return first;
        }
        if (second != null) {
            return second;
        }

        return third;
    }

    public static boolean isNullOrEmpty
            (
                    String text
            ) {
        return text == null || text.length() == 0;
    }

    public static void copyTo
            (
                    InputStream source,
                    OutputStream destination
            )
            throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readed = source.read(buffer, 0, buffer.length);
            if (readed <= 0) {
                break;
            }
            destination.write(buffer, 0, readed);
        }
    }

    public static String readTo
            (
                    StringReader reader,
                    char delimiter
            )
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

    @NotNull
    @Contract(pure = true)
    public static String nullToEmpty (@Nullable String value) {
        return value == null ? "" : value;
    }

    @Nullable
    public static String nullableToString (@Nullable Object value) {
        return value == null ? null : value.toString();
    }

    @NotNull
    public static String toVisible (@Nullable Object value) {
        return value == null ? "(null)" : value.toString();
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
    public static int readInt32Host (InputStream stream) throws IOException {
        byte[] buffer = readExact(stream, 4);
        int result = toInt32(buffer, 0);

        return result;
    }

    /**
     * Read integer in network byte order.
     */
    public static long readInt64Network (InputStream stream) throws IOException {
        byte[] buffer = readExact(stream, 8);
        networkToHost64(buffer, 0);
        long result = toInt64(buffer, 0);

        return result;
    }

    /**
     * Read integer in host byte order.
     */
    public static long readInt64Host (InputStream stream) throws IOException {
        byte[] buffer = readExact(stream, 8);
        long result = toInt64(buffer, 0);

        return result;
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
