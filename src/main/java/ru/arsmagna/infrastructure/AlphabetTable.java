// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.FileSpecification;
import ru.arsmagna.IrbisConnection;
import ru.arsmagna.IrbisEncoding;
import ru.arsmagna.IrbisPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Alphabet character table.
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnnecessaryLocalVariable"})
public final class AlphabetTable {

    /**
     * Standard file name.
     */
    public static final String FILE_NAME = "isisacw.tab";

    //=========================================================================

    /**
     * Array of characters.
     */
    public char[] characters;

    //=========================================================================

    private static AlphabetTable instance;

    private void buildTable(@NotNull ByteBuffer table) {
        characters = IrbisEncoding.ansi().decode(table).array();
        Arrays.sort(characters);
    }

    //=========================================================================

    /**
     * Get the default alphabet table for IRBIS64.
     * @return Default instance of the table.
     */
    public synchronized static AlphabetTable getDefault() {
        if (instance == null) {
            instance = new AlphabetTable();
            instance.characters = new char[] {
                    '\u0026', '\u0040', '\u0041', '\u0042', '\u0043', '\u0044', '\u0045',
                    '\u0046', '\u0047', '\u0048', '\u0049', '\u004A', '\u004B', '\u004C',
                    '\u004D', '\u004E', '\u004F', '\u0050', '\u0051', '\u0052', '\u0053',
                    '\u0054', '\u0055', '\u0056', '\u0057', '\u0058', '\u0059', '\u005A',
                    '\u0061', '\u0062', '\u0063', '\u0064', '\u0065', '\u0066', '\u0067',
                    '\u0068', '\u0069', '\u006A', '\u006B', '\u006C', '\u006D', '\u006E',
                    '\u006F', '\u0070', '\u0071', '\u0072', '\u0073', '\u0074', '\u0075',
                    '\u0076', '\u0077', '\u0078', '\u0079', '\u007A', '\u0098', '\u00A0',
                    '\u00A4', '\u00A6', '\u00A7', '\u00A9', '\u00AB', '\u00AC', '\u00AD',
                    '\u00AE', '\u00B0', '\u00B1', '\u00B5', '\u00B6', '\u00B7', '\u00BB',
                    '\u0401', '\u0402', '\u0403', '\u0404', '\u0405', '\u0406', '\u0407',
                    '\u0408', '\u0409', '\u040A', '\u040B', '\u040C', '\u040E', '\u040F',
                    '\u0410', '\u0411', '\u0412', '\u0413', '\u0414', '\u0415', '\u0416',
                    '\u0417', '\u0418', '\u0419', '\u041A', '\u041B', '\u041C', '\u041D',
                    '\u041E', '\u041F', '\u0420', '\u0421', '\u0422', '\u0423', '\u0424',
                    '\u0425', '\u0426', '\u0427', '\u0428', '\u0429', '\u042A', '\u042B',
                    '\u042C', '\u042D', '\u042E', '\u042F', '\u0430', '\u0431', '\u0432',
                    '\u0433', '\u0434', '\u0435', '\u0436', '\u0437', '\u0438', '\u0439',
                    '\u043A', '\u043B', '\u043C', '\u043D', '\u043E', '\u043F', '\u0440',
                    '\u0441', '\u0442', '\u0443', '\u0444', '\u0445', '\u0446', '\u0447',
                    '\u0448', '\u0449', '\u044A', '\u044B', '\u044C', '\u044D', '\u044E',
                    '\u044F', '\u0451', '\u0452', '\u0453', '\u0454', '\u0455', '\u0456',
                    '\u0457', '\u0458', '\u0459', '\u045A', '\u045B', '\u045C', '\u045E',
                    '\u045F', '\u0490', '\u0491', '\u2013', '\u2014', '\u2018', '\u2019',
                    '\u201A', '\u201C', '\u201D', '\u201E', '\u2020', '\u2021', '\u2022',
                    '\u2026', '\u2030', '\u2039', '\u203A', '\u20AC', '\u2116', '\u2122'
            };
        }

        return instance;
    }

    /**
     * Whether the character is alphanumeric?
     * @param c Character to test.
     * @return true if yes.
     */
    @Contract(pure = true)
    public boolean isAlpha(char c) {
        int found = Arrays.binarySearch(characters, c);

        return found >= 0;
    }

    /**
     * Parse the text representation of table.
     * @param scanner Text scanner.
     * @return Parsed table.
     */
    public static AlphabetTable parse(@NotNull Scanner scanner) {
        ByteBuffer table = ByteBuffer.allocate(256);
        while (scanner.hasNextInt()) {
            int next = scanner.nextInt();
            if (next >= 128) {
                next = next - 256;
            }
            table.put((byte)next);
        }

        table.limit(table.position());
        table.position(0);
        AlphabetTable result = new AlphabetTable();
        result.buildTable(table);

        return result;
    }

    /**
     * Parse the local file for alphabet table.
     * @param file File to parse.
     * @return Read table.
     * @throws IOException Error during input-output.
     */
    public static AlphabetTable parse(@NotNull File file) throws IOException {
        try(FileInputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                AlphabetTable result = parse(scanner);

                return result;
            }
        }
    }

    /**
     * Read the table from the server.
     * @param connection Connection to use.
     * @return Read table or default instance.
     * @throws IOException Error during input-output.
     */
    public static AlphabetTable read(@NotNull IrbisConnection connection) throws IOException {
        FileSpecification specification = new FileSpecification(IrbisPath.SYSTEM, null, FILE_NAME);
        String text = connection.readTextContent(specification);
        AlphabetTable result;
        if (isNullOrEmpty(text)) {
            result = getDefault();
        }
        else {
            Scanner scanner = new Scanner(text);
            result = parse(scanner);
        }

        return result;
    }

    /**
     * Split the text to words by non-alphabet symbols.
     * @param text Text to split.
     * @return Array of words.
     */
    public String[] splitWords(@Nullable String text) {
        if (isNullOrEmpty(text)) {
            return new String[0];
        }

        ArrayList<String> result = new ArrayList<>();
        StringBuilder accumulator = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (isAlpha(c)) {
                accumulator.append(c);
            }
            else {
                if (accumulator.length() != 0) {
                    result.add(accumulator.toString());
                    accumulator.setLength(0);
                }
            }
        }
        if (accumulator.length() != 0) {
            result.add(accumulator.toString());
        }

        return result.toArray(new String[0]);
    }

    /**
     * Trim non-alphabet symbols from beginning and end of the text.
     * @param text Text to trim.
     * @return Trimmed text.
     */
    public String trim(@Nullable String text) {
        if (isNullOrEmpty(text)) {
            return text;
        }

        if (isAlpha(text.charAt(0)) && isAlpha(text.charAt(text.length() - 1))) {
            return text;
        }

        StringBuilder result = new StringBuilder(text);
        while (result.length() != 0
                && !isAlpha(result.charAt(0))) {
            result.deleteCharAt(0);
        }

        while (result.length() != 0
            && !isAlpha(result.charAt(result.length() - 1))) {
            result.deleteCharAt(result.length() - 1);
        }

        return result.toString();
    }
}
