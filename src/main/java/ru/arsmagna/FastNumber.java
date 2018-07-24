package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Fast integer number parsing.
 */
@SuppressWarnings("WeakerAccess")
public class FastNumber {

    /**
     * Convert integer number to string.
     *
     * @param number Number to convert.
     * @return Resulting string.
     */
    @NotNull
    @Contract(pure = true)
    public static String toString(int number) {
        char[] buffer = new char[10];
        int offset = 9;
        if (number == 0) {
            buffer[offset] = '0';
            offset--;
        }
        else {
            for (; number != 0; offset--) {
                int remainder = number % 10;
                number = number / 10;
                buffer[offset] = (char)('0' + remainder);
            }
        }

        return new String(buffer, offset + 1, 9 - offset);
    }

    /**
     * Convert long integer number to string.
     *
     * @param number Number to convert.
     * @return Resulting string.
     */
    @NotNull
    @Contract(pure = true)
    public static String toString(long number) {
        char[] buffer = new char[20];
        int offset = 19;
        if (number == 0) {
            buffer[offset] = '0';
            offset--;
        }
        else {
            for (; number != 0; offset--) {
                long remainder = number % 10;
                number = number / 10;
                buffer[offset] = (char)('0' + remainder);
            }
        }

        return new String(buffer, offset + 1, 19 - offset);
    }

    /**
     * Fast integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static int parseInt32(@NotNull String text) {
        int result = 0, offset=0;
        for (int length = text.length(); length > 0; length--, offset++) {
            result = result * 10 + text.charAt(offset) - '0';
        }

        return result;
    }

    /**
     * Fast integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static int parseInt32(@NotNull String text, int offset, int length) {
        int result = 0;
        for (; length > 0; length--, offset++) {
            result = result * 10 + text.charAt(offset) - '0';
        }

        return result;
    }

    /**
     * Fast integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static int parseInt32(@NotNull char[] text, int offset, int length) {
        int result = 0;
        for (; length > 0; length--, offset++) {
            result = result * 10 + text[offset] - '0';
        }

        return result;
    }

    /**
     * Fast integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static int parseInt32(@NotNull byte[] text, int offset, int length) {
        int result = 0;
        for (; length > 0; length--, offset++) {
            result = result * 10 + text[offset] - '0';
        }

        return result;
    }

    /**
     * Fast long integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static long parseInt64(@NotNull String text) {
        long result = 0;
        int offset=0;
        for (int length = text.length(); length > 0; length--, offset++) {
            result = result * 10 + text.charAt(offset) - '0';
        }

        return result;
    }

    /**
     * Fast long integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static long parseInt64(@NotNull String text, int offset, int length) {
        long result = 0;
        for (; length > 0; length--, offset++) {
            result = result * 10 + text.charAt(offset) - '0';
        }

        return result;
    }

    /**
     * Fast long integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static long parseInt64(@NotNull char[] text, int offset, int length) {
        long result = 0;
        for (; length > 0; length--, offset++) {
            result = result * 10 + text[offset] - '0';
        }

        return result;
    }

    /**
     * Fast long integer number parsing.
     *
     * @param text Text to parse
     * @return Parsed number.
     */
    @Contract(pure = true)
    public static long parseInt64(@NotNull byte[] text, int offset, int length) {
        long result = 0;
        for (; length > 0; length--, offset++) {
            result = result * 10 + text[offset] - '0';
        }

        return result;
    }
}
