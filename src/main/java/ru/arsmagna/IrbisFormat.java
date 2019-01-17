// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.jetbrains.annotations.NotNull;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Common format related stuff.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class IrbisFormat {

    /**
     * Format ALL.
     */
    public static final String ALL = "&uf('+0')";

    /**
     * BRIEF format.
     */
    public static final String BRIEF = "@brief";

    /**
     * IBIS format.
     */
    public static final String IBIS = "@ibiskw_h";

    /**
     * Informational format.
     */
    public static final String INFORMATIONAL = "@info_w";

    /**
     * Optimized (by server) format.
     */
    public static final String OPTIMIZED = "@";

    //=========================================================================

    /**
     * Remove comments from the format.
     * @param text Format to refine.
     * @return Refined format.
     */
    public static String removeComments(@NotNull String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }
        if (!text.contains("/*")) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());
        char state = 0;
        int index = 0, length = text.length();
        while (index < length) {
            char c = text.charAt(index);

            switch (state) {
                case '\'':
                case '"':
                case '|':
                    if (c == state) {
                        state = 0;
                    }
                    result.append(c);
                    break;

                default:
                    if (c == '/') {
                        if (index + 1 < length && text.charAt(index + 1) == '*') {
                            while (index < length) {
                                c = text.charAt(index);
                                if (c == '\r' || c == '\n') {
                                    result.append(c);
                                    break;
                                }
                                index++;
                            }
                        }
                        else {
                            result.append(c);
                        }
                    }
                    else if (c == '\'' || c == '"' || c == '|') {
                        state = c;
                        result.append(c);
                    }
                    else {
                        result.append(c);
                    }
                    break;
            }

            index++;
        }

        return result.toString();
    }

    /**
     * Prepare the dynamic format string.
     * @param text Format to refine.
     * @return Refined format.
     */
    public static String prepareFormat(@NotNull String text) {
        text = removeComments(text);
        int length = text.length();
        if (length == 0) {
            return text;
        }

        boolean flag = false;
        for (int i = 0; i < length; i++) {
            if (text.charAt(i) < ' ') {
                flag = true;
                break;
            }
        }
        if (!flag) {
            return text;
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            if (c >= ' ') {
                result.append(c);
            }
        }

        return result.toString();
    }
}
