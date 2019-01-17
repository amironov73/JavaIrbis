// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

/**
 * Range of the Unicode characters.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class UnicodeRange {

    /**
     * Name of the range.
     */
    public String name;

    /**
     * From the character.
     */
    public char from;

    /**
     * To the character (including).
     */
    public char to;

    //=========================================================================

    /**
     * Control characters.
     */
    public static final UnicodeRange controlCharacters = new UnicodeRange("Control characters", '\u0000', '\u001F');

    /**
     * Basic Latin.
     */
    public static final UnicodeRange basicLatin = new UnicodeRange("Basic Latin", '\u0020', '\u007F');

    /**
     * Latin1 supplement.
     */
    public static final UnicodeRange latin1Supplement = new UnicodeRange("Latin Supplement", '\u0080', '\u00FF');

    /**
     * Latin extended.
     */
    public static final UnicodeRange latinExtended = new UnicodeRange("Latin Extended", '\u0100', '\u024F');

    /**
     * Cyrillic.
     */
    public static final UnicodeRange cyrillic = new UnicodeRange("Cyrillic", '\u0400', '\u04FF');

    /**
     * Cyrillic supplement.
     */
    public static final UnicodeRange cyrillicSupplement = new UnicodeRange("Cyrillic Supplement", '\u0500', '\u052F');

    /**
     * Russian.
     */
    public static final UnicodeRange russian = new UnicodeRange("Russian", '\u0410', '\u0451');

    //=========================================================================

    /**
     * Default constructor.
     */
    public UnicodeRange() {
    }

    /**
     * Initializing constructor.
     *
     * @param name Name of the range.
     * @param from From the character.
     * @param to To the character.
     */
    public UnicodeRange(String name, char from, char to) {
        this.name = name;
        this.from = from;
        this.to = to;
    }

    //=========================================================================

    @Override
    public String toString() {
        return "UnicodeRange{" +
                "name='" + name + '\'' +
                ", from=" + from +
                ", to=" + to +
                '}';
    }
}
