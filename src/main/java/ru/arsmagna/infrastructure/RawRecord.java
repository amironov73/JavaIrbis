package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import ru.arsmagna.IrbisText;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Raw record (from the server).
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class RawRecord {

    /**
     * Database name.
     */
    public String database;

    /**
     * MFN.
     */
    public String mfn;

    /**
     * Record status.
     */
    public String status;

    /**
     * Record version.
     */
    public String version;

    /**
     * Unparsed fields.
     */
    public final Collection<String> fields = new ArrayList<>();

    //=========================================================================

    /**
     * Build protocol representation of the record.
     * @return Record as text.
     */
    public String encode() {
        return encode(IrbisText.IRBIS_DELIMITER);
    }

    /**
     * Build text representation of the record.
     * @param delimiter Line delimiter to use.
     * @return Record as text.
     */
    public String encode(@NotNull String delimiter) {
        StringBuilder result = new StringBuilder();
        result.append(mfn).append('#').append(status).append(delimiter);
        result.append("0#").append(version).append(delimiter);

        for (String field: fields) {
            result.append(field).append(delimiter);

        }

        return result.toString();
    }

    /**
     * Parse the text.
     * @param scanner Scanner to use.
     * @return Parsed record.
     */
    public static RawRecord parse (@NotNull Scanner scanner) {
        RawRecord result = new RawRecord();

        String line = scanner.nextLine();
        String[] parts = line.split("#", 2);
        result.mfn = parts[0];
        result.status = "0";
        if (parts.length == 2) {
            result.status = parts[1];
        }

        line = scanner.nextLine();
        parts = line.split("#", 2);
        result.version = "0";
        if (parts.length == 2) {
            result.version = parts[1];
        }

        while (scanner.hasNext()) {
            line = scanner.nextLine();
            result.fields.add(line);
        }

        return result;
    }

    //=========================================================================

    @Override
    public String toString() {
        return encode("\n");
    }
}
