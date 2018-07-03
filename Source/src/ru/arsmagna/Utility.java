package ru.arsmagna;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

public class Utility
{
    /**
     * Максимальное количество записей в пакете.
     */
    public static final int MAX_PACKET = 32758;

    public static byte[] CRLF = { 0x0D, 0x0A };
    public static byte[] LF = { 0x0A };

    /**
     * Коды возврата, допустимые в команде ReadRecord.
     */
    public static int[] READ_RECORD_CODES = { -201, -600, -602, -603 };

    /**
     * Коды возврата, допустимые в команде ReadTerms.
     */
    public static int[] READ_TERMS_CODES = { -202, -203, -204 };

    /**
     * Список баз данных для администратора.
     */
    public static final String ADMINISTRATOR_DATABASE_LIST = "dbnam1.mnu";

    /**
     * Список баз данных для каталогизатора.
     */

    public static final String CATALOGER_DATABASE_LIST = "dbnam2.mnu";
    /**
     * Список баз данных для читателя.
     */
    public static final String READER_DATABASE_LIST = "dbnam3.mnu";

    public static int find(int[] array, int item)
    {
        for (int i=0; i < array.length; i++)
        {
            if (array[i] == item)
            {
                return i;
            }
        }

        return -1;
    }

    public static boolean isNullOrEmpty(String text)
    {
        return text == null || text.length() == 0;
    }

    public static void copyTo(InputStream source, OutputStream destination)
        throws IOException
    {
        byte[] buffer = new byte[1024];
        while (true)
        {
            int readed = source.read(buffer, 0, buffer.length);
            if (readed <= 0)
            {
                break;
            }
            destination.write(buffer, 0, readed);
        }
    }

    public static String ReadTo
        (
            StringReader reader,
            char delimiter
        )
        throws IOException
    {
        StringBuilder result = new StringBuilder();

        while (true)
        {
            int next = reader.read();
            if (next < 0)
            {
                break;
            }
            char c = (char)next;
            if (c == delimiter)
            {
                break;
            }
            result.append(c);
        }

        return result.toString();
    }

}
