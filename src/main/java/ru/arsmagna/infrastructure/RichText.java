// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.Nullable;

import ru.arsmagna.TextNavigator;
import ru.arsmagna.UnicodeRange;
import ru.arsmagna.Utility;

import static ru.arsmagna.TextNavigator.EOT;

/**
 * Basic rich text format support.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class RichText {

    // Prologues

    /**
     * Central European prologue for RTF file.
     */
    public static final String CentralEuropeanPrologue
            = "{\\rtf1\\ansi\\ansicpg1250\\deff0\\deflang1033"
            + "{\\fonttbl{\\f0\\fnil\\fcharset238 MS Sans Serif;}}"
            + "\\viewkind4\\uc1\\pard\\f0\\fs16 ";

    /**
     * Common prologue for RTF file.
     */
    public static final String CommonPrologue
            = "{\\rtf1\\ansi\\deff0"
            + "{\\fonttbl{\\f0\\fnil\\fcharset0 MS Sans Serif;}}"
            + "\\viewkind4\\uc1\\pard\\f0\\fs16 ";


    /**
     * Western European prologue for RTF file.
     */
    public static final String WesternEuropeanPrologue
            = "{\\rtf1\\ansi\\ansicpg1252\\deff0\\deflang1033"
            + "{\\fonttbl{\\f0\\fnil\\fcharset0 MS Sans Serif;}}"
            + "\\viewkind4\\uc1\\pard\\f0\\fs16 ";

    /**
     * Russian prologue for RTF file.
     */
    public static final String RussianPrologue
            = "{\\rtf1\\ansi\\ansicpg1251\\deff0\\deflang1049"
            + "{\\fonttbl{\\f0\\fnil\\fcharset204 Times New Roman;}"
            + "{\\f1\\fnil\\fcharset238 Times New Roman;}}"
            + "{\\stylesheet{\\s0\\f0\\fs24\\snext0 Normal;}"
            + "{\\s1\\f1\\fs40\\b\\snext0 Heading;}}"
            + "\\viewkind4\\uc1\\pard\\f0\\fs16 ";

    //=========================================================================

    /**
     * Decode the text.
     *
     * @param text Text to decode.
     * @return Decoded text.
     */
    public static String decode(@Nullable String text) {
        if (Utility.isNullOrEmpty(text)) {
            return text;
        }

        int length = text.length();

        StringBuilder result = new StringBuilder(length);
        TextNavigator navigator = new TextNavigator(text);
        while (!navigator.eot()) {
            String chunk = navigator.readUntil('\\');
            result.append(chunk);
            char prefix = navigator.readChar();
            if (prefix != '\\') {
                break;
            }

            char c = navigator.readChar();
            if (c == EOT) {
                result.append(prefix);
                break;
            }

            if (c != 'u') {
                result.append(prefix);
                result.append(c);
                continue;
            }

            StringBuilder buffer = new StringBuilder();
            while (!navigator.eot()) {
                c = navigator.readChar();
                if (!Character.isDigit(c)) {
                    break;
                }
                buffer.append(c);
            }
            if (buffer.length() != 0) {
                c = (char) Integer.parseInt(buffer.toString());
                result.append(c);
            }
        }

        return result.toString();
    }

    /**
     * Encode the text.
     *
     * @param text Text to encode.
     * @param goodRange Character range to pass through (can be null).
     * @return Encoded text.
     */
    @Nullable
    public static String encode(@Nullable String text,
                                @Nullable UnicodeRange goodRange) {
        if (Utility.isNullOrEmpty(text)) {
            return text;
        }

        int length = text.length();

        StringBuilder result = new StringBuilder(length);
        for(int i = 0; i < length; i++)
        {
            char c = text.charAt(i);

            if (c < 0x20) {
                result.append(String.format("\\'%02x", (int)c));
            }
            else if (c < 0x80) {
                switch (c) {
                    case '{':
                        result.append("\\{");
                        break;

                    case '}':
                        result.append("\\}");
                        break;

                    case '\\':
                        result.append("\\\\");
                        break;

                    default:
                        result.append(c);
                        break;
                }
            }
            else if (c < 0x100) {
                result.append(String.format("\\'%02x", (int)c));
            }
            else {
                boolean simple = false;
                if (goodRange != null) {
                    if (c >= goodRange.from && c <= goodRange.to) {
                        simple = true;
                    }
                }
                if (simple) {
                    result.append(c);
                }
                else {
                    // После "u" следующий символ съедается
                    // поэтому подсовываем знак вопроса
                    result.append(String.format("\\u%d?", (int)c));
                }
            }
        }

        return result.toString();
    }
}
