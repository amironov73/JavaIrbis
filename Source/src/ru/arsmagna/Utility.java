package ru.arsmagna;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utility
{
    public static byte[] CRLF = { 0x0D, 0x0A };
    public static byte[] LF = { 0x0A };

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
}
