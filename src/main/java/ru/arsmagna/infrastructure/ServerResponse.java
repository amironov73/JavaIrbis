package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.*;

import ru.arsmagna.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Ответ сервера.
 */
public final class ServerResponse
{
    /**
     * Код команды.
     */
    public String command;

    /**
     * Идентификатор клиента.
     */
    public int clientId;

    /**
     * Номер запроса.
     */
    public int queryId;

    /**
     * Код возврата (формируется не всегда!).
     */
    public int returnCode;

    //=========================================================================

    /**
     * Конструктор.
     * @param networkStream Поток с ответом сервера.
     * @throws IOException Ошибка ввода-вывода.
     */
    public ServerResponse
        (
            @NotNull InputStream networkStream
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

    /**
     * Достигнут конец?
     * @return Флаг конца.
     */
    public boolean isEof()
    {
        return stream.available() == 0;
    }

    /**
     * Проверка кода возврата.
     * @param allowed Разрешенные коды.
     * @throws IrbisException
     */
    public void checkReturnCode
        (
            int[] allowed
        )
        throws IrbisException
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

    /**
     * Считывание не менее указанного количества строк.
     * @param count
     * @return
     */
    public String[] readAnsi
        (
            int count
        )
    {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            String line = readAnsi();
            if (line == null)
            {
                return null;
            }
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public int readInt32()
    {
        String text = readAnsi();
        int result = Integer.parseInt(text);

        return result;
    }

    public final String[] readRemainingAnsiLines()
    {
        ArrayList<String> result = new ArrayList<String>();
        while (!isEof())
        {
            String line = readAnsi();
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public final String[] readRemainingUtfLines()
    {
        ArrayList<String> result = new ArrayList<String>();
        while (!isEof())
        {
            String line = readUtf();
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public final String readRemainingAnsiText() throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utility.copyTo(stream, buffer);
        byte[] bytes = buffer.toByteArray();
        String result = new String(bytes, IrbisEncoding.ansi());

        return result;
    }

    public final String readRemainingUtfText() throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utility.copyTo(stream, buffer);
        byte[] bytes = buffer.toByteArray();
        String result = new String(bytes, IrbisEncoding.utf());

        return result;
    }

    @NotNull
    public final String readUtf()
    {
        byte[] line = getLine();
        String result = new String(line, IrbisEncoding.utf());

        return result;
    }

    /**
     * Считывание не менее указанного количества строк.
     * @param count
     * @return
     */
    @Nullable
    public final String[] readUtf
        (
            int count
        )
    {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            String line = readUtf();
            if (line == null)
            {
                return null;
            }
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    //=========================================================================
}
