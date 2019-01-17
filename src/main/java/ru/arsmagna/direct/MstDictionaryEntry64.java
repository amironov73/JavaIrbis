// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.direct;

/**
 * Элемент справочника MST-файла, описывающий поле переменной длины.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MstDictionaryEntry64 {

    /**
     * Длина элемента справочника MST-файла.
     */
    public static final int ENTRY_SIZE = 12;

    //=========================================================================

    /**
     * Field tag.
     */
    public int tag;

    /**
     * Data offset.
     */
    public int position;

    /**
     * Data length.
     */
    public int length;

    /**
     * Raw data.
     */
    public byte[] bytes;

    /**
     * Decoded data.
     */
    public String text;

    //=========================================================================

    @Override
    public String toString() {
        return String.format("tag=%d, position=%d, length=%d, text=%s",
            tag, position, length, text );
    }
}
