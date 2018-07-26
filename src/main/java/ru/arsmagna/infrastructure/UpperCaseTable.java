package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Upper-case character table.
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnnecessaryLocalVariable"})
public final class UpperCaseTable {

    /**
     * Standard file name.
     */
    public static final String FILE_NAME = "isisucw.tab";

    //=========================================================================

    public final Map<Character, Character> mapping = new HashMap<>();

    //=========================================================================

    private static UpperCaseTable instance;

    //=========================================================================

    /**
     * Get the default alphabet table for IRBIS64.
     * @return Default instance of the table.
     */
    public synchronized static UpperCaseTable getDefault() {
        if (instance == null) {
            instance = new UpperCaseTable();
            instance.mapping.put((char)0x0000, (char)0x0000);
            instance.mapping.put((char)0x0001, (char)0x0001);
            instance.mapping.put((char)0x0002, (char)0x0002);
            instance.mapping.put((char)0x0003, (char)0x0003);
            instance.mapping.put((char)0x0004, (char)0x0004);
            instance.mapping.put((char)0x0005, (char)0x0005);
            instance.mapping.put((char)0x0006, (char)0x0006);
            instance.mapping.put((char)0x0007, (char)0x0007);
            instance.mapping.put((char)0x0008, (char)0x0008);
            instance.mapping.put((char)0x0009, (char)0x0009);
            instance.mapping.put((char)0x000A, (char)0x000A);
            instance.mapping.put((char)0x000B, (char)0x000B);
            instance.mapping.put((char)0x000C, (char)0x000C);
            instance.mapping.put((char)0x000D, (char)0x000D);
            instance.mapping.put((char)0x000E, (char)0x000E);
            instance.mapping.put((char)0x000F, (char)0x000F);
            instance.mapping.put((char)0x0010, (char)0x0010);
            instance.mapping.put((char)0x0011, (char)0x0011);
            instance.mapping.put((char)0x0012, (char)0x0012);
            instance.mapping.put((char)0x0013, (char)0x0013);
            instance.mapping.put((char)0x0014, (char)0x0014);
            instance.mapping.put((char)0x0015, (char)0x0015);
            instance.mapping.put((char)0x0016, (char)0x0016);
            instance.mapping.put((char)0x0017, (char)0x0017);
            instance.mapping.put((char)0x0018, (char)0x0018);
            instance.mapping.put((char)0x0019, (char)0x0019);
            instance.mapping.put((char)0x001A, (char)0x001A);
            instance.mapping.put((char)0x001B, (char)0x001B);
            instance.mapping.put((char)0x001C, (char)0x001C);
            instance.mapping.put((char)0x001D, (char)0x001C);
            instance.mapping.put((char)0x001E, (char)0x001E);
            instance.mapping.put((char)0x001F, (char)0x001F);
            instance.mapping.put((char)0x0020, (char)0x0020);
            instance.mapping.put((char)0x0021, (char)0x0021);
            instance.mapping.put((char)0x0022, (char)0x0022);
            instance.mapping.put((char)0x0023, (char)0x0023);
            instance.mapping.put((char)0x0024, (char)0x0024);
            instance.mapping.put((char)0x0025, (char)0x0025);
            instance.mapping.put((char)0x0026, (char)0x0026);
            instance.mapping.put((char)0x0027, (char)0x0027);
            instance.mapping.put((char)0x0028, (char)0x0028);
            instance.mapping.put((char)0x0029, (char)0x0029);
            instance.mapping.put((char)0x002A, (char)0x002A);
            instance.mapping.put((char)0x002B, (char)0x002B);
            instance.mapping.put((char)0x002C, (char)0x002C);
            instance.mapping.put((char)0x002D, (char)0x002D);
            instance.mapping.put((char)0x002E, (char)0x002E);
            instance.mapping.put((char)0x002F, (char)0x002F);
            instance.mapping.put((char)0x0030, (char)0x0030);
            instance.mapping.put((char)0x0031, (char)0x0031);
            instance.mapping.put((char)0x0032, (char)0x0032);
            instance.mapping.put((char)0x0033, (char)0x0033);
            instance.mapping.put((char)0x0034, (char)0x0034);
            instance.mapping.put((char)0x0035, (char)0x0035);
            instance.mapping.put((char)0x0036, (char)0x0036);
            instance.mapping.put((char)0x0037, (char)0x0037);
            instance.mapping.put((char)0x0038, (char)0x0038);
            instance.mapping.put((char)0x0039, (char)0x0039);
            instance.mapping.put((char)0x003A, (char)0x003A);
            instance.mapping.put((char)0x003B, (char)0x003B);
            instance.mapping.put((char)0x003C, (char)0x003C);
            instance.mapping.put((char)0x003D, (char)0x003D);
            instance.mapping.put((char)0x003E, (char)0x003E);
            instance.mapping.put((char)0x003F, (char)0x003F);
            instance.mapping.put((char)0x0040, (char)0x0040);
            instance.mapping.put((char)0x0041, (char)0x0041);
            instance.mapping.put((char)0x0042, (char)0x0042);
            instance.mapping.put((char)0x0043, (char)0x0043);
            instance.mapping.put((char)0x0044, (char)0x0044);
            instance.mapping.put((char)0x0045, (char)0x0045);
            instance.mapping.put((char)0x0046, (char)0x0046);
            instance.mapping.put((char)0x0047, (char)0x0047);
            instance.mapping.put((char)0x0048, (char)0x0048);
            instance.mapping.put((char)0x0049, (char)0x0049);
            instance.mapping.put((char)0x004A, (char)0x004A);
            instance.mapping.put((char)0x004B, (char)0x004B);
            instance.mapping.put((char)0x004C, (char)0x004C);
            instance.mapping.put((char)0x004D, (char)0x004D);
            instance.mapping.put((char)0x004E, (char)0x004E);
            instance.mapping.put((char)0x004F, (char)0x004F);
            instance.mapping.put((char)0x0050, (char)0x0050);
            instance.mapping.put((char)0x0051, (char)0x0051);
            instance.mapping.put((char)0x0052, (char)0x0052);
            instance.mapping.put((char)0x0053, (char)0x0053);
            instance.mapping.put((char)0x0054, (char)0x0054);
            instance.mapping.put((char)0x0055, (char)0x0055);
            instance.mapping.put((char)0x0056, (char)0x0056);
            instance.mapping.put((char)0x0057, (char)0x0057);
            instance.mapping.put((char)0x0058, (char)0x0058);
            instance.mapping.put((char)0x0059, (char)0x0059);
            instance.mapping.put((char)0x005A, (char)0x005A);
            instance.mapping.put((char)0x005B, (char)0x005B);
            instance.mapping.put((char)0x005C, (char)0x005C);
            instance.mapping.put((char)0x005D, (char)0x005D);
            instance.mapping.put((char)0x005E, (char)0x005E);
            instance.mapping.put((char)0x005F, (char)0x005F);
            instance.mapping.put((char)0x0060, (char)0x0060);
            instance.mapping.put((char)0x0061, (char)0x0041);
            instance.mapping.put((char)0x0062, (char)0x0042);
            instance.mapping.put((char)0x0063, (char)0x0043);
            instance.mapping.put((char)0x0064, (char)0x0044);
            instance.mapping.put((char)0x0065, (char)0x0045);
            instance.mapping.put((char)0x0066, (char)0x0046);
            instance.mapping.put((char)0x0067, (char)0x0047);
            instance.mapping.put((char)0x0068, (char)0x0048);
            instance.mapping.put((char)0x0069, (char)0x0049);
            instance.mapping.put((char)0x006A, (char)0x004A);
            instance.mapping.put((char)0x006B, (char)0x004B);
            instance.mapping.put((char)0x006C, (char)0x004C);
            instance.mapping.put((char)0x006D, (char)0x004D);
            instance.mapping.put((char)0x006E, (char)0x004E);
            instance.mapping.put((char)0x006F, (char)0x004F);
            instance.mapping.put((char)0x0070, (char)0x0050);
            instance.mapping.put((char)0x0071, (char)0x0051);
            instance.mapping.put((char)0x0072, (char)0x0052);
            instance.mapping.put((char)0x0073, (char)0x0053);
            instance.mapping.put((char)0x0074, (char)0x0054);
            instance.mapping.put((char)0x0075, (char)0x0055);
            instance.mapping.put((char)0x0076, (char)0x0056);
            instance.mapping.put((char)0x0077, (char)0x0057);
            instance.mapping.put((char)0x0078, (char)0x0058);
            instance.mapping.put((char)0x0079, (char)0x0059);
            instance.mapping.put((char)0x007A, (char)0x005A);
            instance.mapping.put((char)0x007B, (char)0x007B);
            instance.mapping.put((char)0x007C, (char)0x007C);
            instance.mapping.put((char)0x007D, (char)0x007D);
            instance.mapping.put((char)0x007E, (char)0x007E);
            instance.mapping.put((char)0x007F, (char)0x007F);
            instance.mapping.put((char)0x0402, (char)0x0402);
            instance.mapping.put((char)0x0403, (char)0x0403);
            instance.mapping.put((char)0x201A, (char)0x201A);
            instance.mapping.put((char)0x0453, (char)0x0453);
            instance.mapping.put((char)0x201E, (char)0x201E);
            instance.mapping.put((char)0x2026, (char)0x2026);
            instance.mapping.put((char)0x2020, (char)0x2020);
            instance.mapping.put((char)0x2021, (char)0x2021);
            instance.mapping.put((char)0x20AC, (char)0x20AC);
            instance.mapping.put((char)0x2030, (char)0x2030);
            instance.mapping.put((char)0x0409, (char)0x0409);
            instance.mapping.put((char)0x2039, (char)0x2039);
            instance.mapping.put((char)0x040A, (char)0x040A);
            instance.mapping.put((char)0x040C, (char)0x040C);
            instance.mapping.put((char)0x040B, (char)0x040B);
            instance.mapping.put((char)0x040F, (char)0x040F);
            instance.mapping.put((char)0x0452, (char)0x0452);
            instance.mapping.put((char)0x2018, (char)0x2018);
            instance.mapping.put((char)0x2019, (char)0x2019);
            instance.mapping.put((char)0x201C, (char)0x201C);
            instance.mapping.put((char)0x201D, (char)0x201D);
            instance.mapping.put((char)0x2022, (char)0x2022);
            instance.mapping.put((char)0x2013, (char)0x2013);
            instance.mapping.put((char)0x2014, (char)0x2014);
            instance.mapping.put((char)0x0098, (char)0x0098);
            instance.mapping.put((char)0x2122, (char)0x2122);
            instance.mapping.put((char)0x0459, (char)0x0459);
            instance.mapping.put((char)0x203A, (char)0x203A);
            instance.mapping.put((char)0x045A, (char)0x045A);
            instance.mapping.put((char)0x045C, (char)0x045C);
            instance.mapping.put((char)0x045B, (char)0x045B);
            instance.mapping.put((char)0x045F, (char)0x045F);
            instance.mapping.put((char)0x00A0, (char)0x00A0);
            instance.mapping.put((char)0x040E, (char)0x040E);
            instance.mapping.put((char)0x045E, (char)0x040E);
            instance.mapping.put((char)0x0408, (char)0x0408);
            instance.mapping.put((char)0x00A4, (char)0x00A4);
            instance.mapping.put((char)0x0490, (char)0x0490);
            instance.mapping.put((char)0x00A6, (char)0x00A6);
            instance.mapping.put((char)0x00A7, (char)0x00A7);
            instance.mapping.put((char)0x0401, (char)0x0401);
            instance.mapping.put((char)0x00A9, (char)0x00A9);
            instance.mapping.put((char)0x0404, (char)0x0404);
            instance.mapping.put((char)0x00AB, (char)0x00AB);
            instance.mapping.put((char)0x00AC, (char)0x00AC);
            instance.mapping.put((char)0x00AD, (char)0x00AD);
            instance.mapping.put((char)0x00AE, (char)0x00AE);
            instance.mapping.put((char)0x0407, (char)0x0407);
            instance.mapping.put((char)0x00B0, (char)0x00B0);
            instance.mapping.put((char)0x00B1, (char)0x00B1);
            instance.mapping.put((char)0x0406, (char)0x0406);
            instance.mapping.put((char)0x0456, (char)0x0406);
            instance.mapping.put((char)0x0491, (char)0x0490);
            instance.mapping.put((char)0x00B5, (char)0x00B5);
            instance.mapping.put((char)0x00B6, (char)0x00B6);
            instance.mapping.put((char)0x00B7, (char)0x00B7);
            instance.mapping.put((char)0x0451, (char)0x0401);
            instance.mapping.put((char)0x2116, (char)0x2116);
            instance.mapping.put((char)0x0454, (char)0x0404);
            instance.mapping.put((char)0x00BB, (char)0x00BB);
            instance.mapping.put((char)0x0458, (char)0x0408);
            instance.mapping.put((char)0x0405, (char)0x0405);
            instance.mapping.put((char)0x0455, (char)0x0405);
            instance.mapping.put((char)0x0457, (char)0x0407);
            instance.mapping.put((char)0x0410, (char)0x0410);
            instance.mapping.put((char)0x0411, (char)0x0411);
            instance.mapping.put((char)0x0412, (char)0x0412);
            instance.mapping.put((char)0x0413, (char)0x0413);
            instance.mapping.put((char)0x0414, (char)0x0414);
            instance.mapping.put((char)0x0415, (char)0x0415);
            instance.mapping.put((char)0x0416, (char)0x0416);
            instance.mapping.put((char)0x0417, (char)0x0417);
            instance.mapping.put((char)0x0418, (char)0x0418);
            instance.mapping.put((char)0x0419, (char)0x0419);
            instance.mapping.put((char)0x041A, (char)0x041A);
            instance.mapping.put((char)0x041B, (char)0x041B);
            instance.mapping.put((char)0x041C, (char)0x041C);
            instance.mapping.put((char)0x041D, (char)0x041D);
            instance.mapping.put((char)0x041E, (char)0x041E);
            instance.mapping.put((char)0x041F, (char)0x041F);
            instance.mapping.put((char)0x0420, (char)0x0420);
            instance.mapping.put((char)0x0421, (char)0x0421);
            instance.mapping.put((char)0x0422, (char)0x0422);
            instance.mapping.put((char)0x0423, (char)0x0423);
            instance.mapping.put((char)0x0424, (char)0x0424);
            instance.mapping.put((char)0x0425, (char)0x0425);
            instance.mapping.put((char)0x0426, (char)0x0426);
            instance.mapping.put((char)0x0427, (char)0x0427);
            instance.mapping.put((char)0x0428, (char)0x0428);
            instance.mapping.put((char)0x0429, (char)0x0429);
            instance.mapping.put((char)0x042A, (char)0x042A);
            instance.mapping.put((char)0x042B, (char)0x042B);
            instance.mapping.put((char)0x042C, (char)0x042C);
            instance.mapping.put((char)0x042D, (char)0x042D);
            instance.mapping.put((char)0x042E, (char)0x042E);
            instance.mapping.put((char)0x042F, (char)0x042F);
            instance.mapping.put((char)0x0430, (char)0x0410);
            instance.mapping.put((char)0x0431, (char)0x0411);
            instance.mapping.put((char)0x0432, (char)0x0412);
            instance.mapping.put((char)0x0433, (char)0x0413);
            instance.mapping.put((char)0x0434, (char)0x0414);
            instance.mapping.put((char)0x0435, (char)0x0415);
            instance.mapping.put((char)0x0436, (char)0x0416);
            instance.mapping.put((char)0x0437, (char)0x0417);
            instance.mapping.put((char)0x0438, (char)0x0418);
            instance.mapping.put((char)0x0439, (char)0x0419);
            instance.mapping.put((char)0x043A, (char)0x041A);
            instance.mapping.put((char)0x043B, (char)0x041B);
            instance.mapping.put((char)0x043C, (char)0x041C);
            instance.mapping.put((char)0x043D, (char)0x041D);
            instance.mapping.put((char)0x043E, (char)0x041E);
            instance.mapping.put((char)0x043F, (char)0x041F);
            instance.mapping.put((char)0x0440, (char)0x0420);
            instance.mapping.put((char)0x0441, (char)0x0421);
            instance.mapping.put((char)0x0442, (char)0x0422);
            instance.mapping.put((char)0x0443, (char)0x0423);
            instance.mapping.put((char)0x0444, (char)0x0424);
            instance.mapping.put((char)0x0445, (char)0x0425);
            instance.mapping.put((char)0x0446, (char)0x0426);
            instance.mapping.put((char)0x0447, (char)0x0427);
            instance.mapping.put((char)0x0448, (char)0x0428);
            instance.mapping.put((char)0x0449, (char)0x0429);
            instance.mapping.put((char)0x044A, (char)0x042A);
            instance.mapping.put((char)0x044B, (char)0x042B);
            instance.mapping.put((char)0x044C, (char)0x042C);
            instance.mapping.put((char)0x044D, (char)0x042D);
            instance.mapping.put((char)0x044E, (char)0x042E);
            instance.mapping.put((char)0x044F, (char)0x042F);
        }

        return instance;
    }

    /**
     * Parse the text representation of table.
     * @param scanner Text scanner.
     * @return Parsed table.
     */
    public static UpperCaseTable parse(@NotNull Scanner scanner) {
        ByteBuffer firstBytes = ByteBuffer.allocate(256);
        for (int i = 0; i < 256; i++) {
            int next = i;
            if (next >= 128) {
                next = next - 256;
            }
            firstBytes.put((byte)next);
        }
        firstBytes.limit(firstBytes.position());
        firstBytes.position(0);
        char[] firstChars = IrbisEncoding.ansi().decode(firstBytes).array();

        ByteBuffer secondBytes = ByteBuffer.allocate(256);
        while (scanner.hasNextInt()) {
            int next = scanner.nextInt();
            if (next >= 128) {
                next = next - 256;
            }
            secondBytes.put((byte)next);
        }
        secondBytes.limit(secondBytes.position());
        secondBytes.position(0);
        char[] secondChars = IrbisEncoding.ansi().decode(secondBytes).array();

        UpperCaseTable result = new UpperCaseTable();
        for (int i = 0; i < secondChars.length; i++) {
            result.mapping.put(firstChars[i], secondChars[i]);
        }

        return result;
    }

    /**
     * Parse the local file for upper-case table.
     * @param file File to parse.
     * @return Read table.
     * @throws IOException Error during input-output.
     */
    public static UpperCaseTable parse(@NotNull File file) throws IOException {
        try(FileInputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                UpperCaseTable result = parse(scanner);

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
    public static UpperCaseTable read(@NotNull IrbisConnection connection) throws IOException {
        FileSpecification specification = new FileSpecification(IrbisPath.SYSTEM, null, FILE_NAME);
        String text = connection.readTextContent(specification);
        UpperCaseTable result;
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
     * Convert the character to upper case.
     * @param c Character to convert.
     * @return Uppercased character.
     */
    public char upperCase(char c) {
        if (mapping.containsKey(c)) {
            return mapping.get(c);
        }

        return c;
    }

    /**
     * Convert the text to upper case.
     * @param text Text to convert.
     * @return Uppercased text.
     */
    public String upperCase(@Nullable String text) {
        if (Utility.isNullOrEmpty(text)) {
            return text;
        }

        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            result.append(upperCase(text.charAt(i)));
        }

        return result.toString();
    }
}
