package ru.arsmagna;

public class IrbisText
{
    public static final String IRBIS_DELIMITER = "\u001F\u001E";
    public static final String SHORT_DELIMITER = "\u001E";
    public static final String MSDOS_DELIMITER = "\r\n";

    public static final char[] SEARCH_DELIMITERS = { '#' };

    public static String fromIrbisToSingleLine
        (
            String text
        )
    {
        if (!Utility.isNullOrEmpty(text))
        {
            text = text.replaceAll(IRBIS_DELIMITER, MSDOS_DELIMITER);
        }

        return text;
    }

    public static String[] fromIrbisToManyLines
        (
            String text
        )
    {
        if (!Utility.isNullOrEmpty(text))
        {
            return text.split(IRBIS_DELIMITER);
        }

        return new String[0];
    }

    public static String[] fromWriteRecord
        (
            String text
        )
    {
        if (!Utility.isNullOrEmpty(text))
        {
            return text.split(SHORT_DELIMITER);
        }

        return new String[0];
    }
}
