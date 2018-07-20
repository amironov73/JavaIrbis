package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;

import ru.arsmagna.IrbisText;

import java.util.ArrayList;
import java.util.Collection;

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
     * @param text Text to scan
     */
    public void parseSingle (@NotNull String[] text) {
        if (text.length == 0) {
            return;
        }

        String[] parts = text[0].split("#", 2);
        mfn = parts[0];
        status = "0";
        if (parts.length == 2) {
            status = parts[1];
        }

        parts = text[1].split("#", 2);
        version = "0";
        if (parts.length == 2) {
            version = parts[1];
        }

        fields.clear();
        for (int i = 2; i < text.length; i++) {
            fields.add(text[i]);
        }
    }

    //=========================================================================

    @Override
    public String toString() {
        return encode("\n");
    }
}
