package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.Nullable;

import ru.arsmagna.Utility;

/**
 * Basic HTML format support.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class HtmlText {

    /**
     * Encode the text.
     *
     * @param text Text to encode.
     * @return Encoded text or null
     */
    @Nullable
    public static String encode(@Nullable String text) {
        if (Utility.isNullOrEmpty(text)) {
            return text;
        }

        int length = text.length();
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);

            switch (c) {
                case '"':
                    result.append("&quot;");
                    break;

                case '#':
                    result.append("&num;");
                    break;

                case '&':
                    result.append("&amp;");
                    break;

                case '\'':
                    result.append("&apos;");
                    break;

                case '<':
                    result.append("&lt;");
                    break;

                case '>':
                    result.append("&gt;");
                    break;

                case '\u00A0':
                    // non-breaking space
                    result.append("&nbsp;");
                    break;

                case '\u00A2':
                    // cent sign
                    result.append("&cent;");
                    break;

                case '\u00A3':
                    // pound sign
                    result.append("&pound;");
                    break;

                case '\u00A5':
                    // yen sign
                    result.append("&yen;");
                    break;

                case '\u00A7':
                    // section sign
                    result.append("&sect;");
                    break;

                case '\u00A9':
                    // copyright sign
                    result.append("&copy;");
                    break;

                case '\u00AD':
                    // soft hyphen
                    result.append("&shy;");
                    break;

                case '\u00AE':
                    // registered sign
                    result.append("&reg;");
                    break;

                case '\u20AC':
                    // euro sign
                    result.append("&euro;");
                    break;

                default:
                    result.append(c);
                    break;
            }
        }

        return result.toString();
    }
}
