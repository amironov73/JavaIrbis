package ru.arsmagna.infrastructure;

import ru.arsmagna.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Ответ сервера.
 */
public class ServerResponse
{
    public String command;
    public int clientId;
    public int queryId;
    public int returnCode;

    //=========================================================================

    public ServerResponse
        (
            InputStream networkStream
        )
        throws IOException
    {
        ByteArrayOutputStream memory = new ByteArrayOutputStream();
        byte[] buffer = new byte[32 * 1024];
        while (true)
        {
            int read = networkStream.read(buffer);
            if (read <= 0)
            {
                break;
            }
            memory.write(buffer, 0, read);
        }
        stream = new ByteArrayInputStream(memory.toByteArray());
        command = readAnsi();
        clientId = readInt32();
        queryId = readInt32();
        for (int i = 0; i < 7; i++)
        {
            readAnsi();
        }
    }

    //=========================================================================

    private ByteArrayInputStream stream;

    //=========================================================================

    public boolean isEof()
    {
        return stream.available() == 0;
    }

    public void checkReturnCode(int[] allowed) throws IrbisException
    {
        if (getReturnCode() < 0)
        {
            if (Utility.find(allowed, returnCode) < 0)
            {
                throw new IrbisException(returnCode);
            }
        }
    }

    public void checkReturnCode() throws IrbisException
    {
        if (getReturnCode() < 0)
        {
            throw new IrbisException(returnCode);
        }
    }

    public byte[] getLine()
    {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        while (true)
        {
            int one = stream.read();
            if (one < 0)
            {
                break;
            }
            if (one == 0x0D)
            {
                one = stream.read();
                if (one == 0x0A)
                {
                    break;
                }
                else
                {
                    // stream.pushBack();
                }
            }
            else
            {
                result.write(one);
            }
        }

        return result.toByteArray();
    }

    public int getReturnCode()
    {
        returnCode = readInt32();

        return returnCode;
    }

    public String readAnsi()
    {
        byte[] line = getLine();

        return new String(line, IrbisEncoding.ansi());
    }

    public int readInt32()
    {
        String text = readAnsi();
        int result = Integer.parseInt(text);

        return result;
    }

    public String[] readRemainingAnsiLines()
    {
        ArrayList<String> result = new ArrayList<String>();
        while (!isEof())
        {
            String line = readAnsi();
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public String[] readRemainingUtfLines()
    {
        ArrayList<String> result = new ArrayList<String>();
        while (!isEof())
        {
            String line = readUtf();
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public String readRemainingUtfText() throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utility.copyTo(stream, buffer);
        byte[] bytes = buffer.toByteArray();
        String result = new String(bytes, IrbisEncoding.utf());

        return result;
    }

    public String readUtf()
    {
        byte[] line = getLine();
        String result = new String(line, IrbisEncoding.utf());

        return result;
    }

    //=========================================================================
}
