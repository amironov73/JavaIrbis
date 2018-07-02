package ru.arsmagna;

import ru.arsmagna.infrastructure.ClientQuery;
import ru.arsmagna.infrastructure.ServerResponse;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;

public class IrbisConnection
{
    /**
     * Адрес сервера.
     */
    public String host;

    /**
     * Порт сервера.
     */
    public int port;

    /**
     * Имя пользователя (логин).
     */
    public String username;

    /**
     * Пароль.
     */
    public String password;

    /**
     * Имя текущей базы данных.
     */
    public String database;

    /**
     * Тип АРМ.
     */
    public char workstation;

    /**
     * Идентификатор клиента.
     */
    public int clientId;

    /**
     * Номер команды.
     */
    public int queryId;

    //=========================================================================

    /**
     * Конструктор.
     */
    public IrbisConnection()
    {
        host = "127.0.0.1";
        port = 6666;
        workstation = 'C';
    }

    //=========================================================================

    public void actualizeRecord
        (
            String database,
            int mfn
        )
    {
        throw new UnsupportedOperationException();
    }

    public void connect()
    {
        throw new UnsupportedOperationException();
    }

    public void createDatabase
        (
            String databaseName,
            String description,
            boolean readerAccess,
            String template
        )
    {
        throw new UnsupportedOperationException();
    }

    public void createDictionary
        (
            String databaseName
        )
    {
        throw new UnsupportedOperationException();
    }

    public void deleteDatabase
        (
            String databaseName
        )
    {
        throw new UnsupportedOperationException();
    }

    public void disconnect()
    {
        throw new UnsupportedOperationException();
    }

    public ServerResponse execute
        (
            ClientQuery query
        )
        throws IOException
    {
        Socket socket = new Socket(host, port);
        byte[] outputData = query.encode();
        socket.getOutputStream().write(outputData);
        ServerResponse result = new ServerResponse(socket.getInputStream());
        socket.close();

        return result;
    }

    public String formatRecord
        (
            String format,
            int mfn
        )
    {
        throw new UnsupportedOperationException();
    }

    public String formatRecord
        (
            String format,
            MarcRecord record
        )
    {
        throw new UnsupportedOperationException();
    }

    public int getMaxMfn()
    {
        throw new UnsupportedOperationException();
    }

    public int getMaxMfn
        (
            String databaseName
        )
    {
        throw new UnsupportedOperationException();
    }

    public String[] listFiles
        (
            FileSpecification specification
        )
    {
        throw new UnsupportedOperationException();
    }

    public String[] listFiles
        (
            FileSpecification[] specifications
        )
    {
        throw new UnsupportedOperationException();
    }

    public void noOp()
    {
        throw new UnsupportedOperationException();
    }

    public void parseConnectionString
        (
            String connectionString
        )
    {
        throw new UnsupportedOperationException();
    }

    public byte[] readBinaryFile
        (
            FileSpecification specification
        )
    {
        throw new UnsupportedOperationException();
    }

    public TermPosting[] readPostings
        (
            PostingParameters parameters
        )
    {
        throw new UnsupportedOperationException();
    }

    public MarcRecord readRecord
        (
            String database,
            int mfn,
            boolean lockFlag,
            String format
        )
    {
        throw new UnsupportedOperationException();
    }

    public MarcRecord readRecord
        (
            String database,
            int mfn,
            int versionNumber,
            String format
        )
    {
        throw new UnsupportedOperationException();
    }

    public TermInfo[] readTerms
        (
            TermParameters parameters
        )
    {
        throw new UnsupportedOperationException();
    }

    public String readTextFile
        (
            FileSpecification specification
        )
    {
        throw new UnsupportedOperationException();
    }

    public String[] readTextFiles
        (
            FileSpecification[] specifications
        )
    {
        throw new UnsupportedOperationException();
    }

    public void reloadDictionary
        (
            String database
        )
    {
        throw new UnsupportedOperationException();
    }

    public void reloadMasterFile
        (
            String database
        )
    {
        throw new UnsupportedOperationException();
    }

    public void restartServer()
    {
        throw new UnsupportedOperationException();
    }

    public int[] search
        (
            String expression
        )
    {
        throw new UnsupportedOperationException();
    }

    public int[] sequentialSearch
        (
            SearchParameters parameters
        )
    {
        throw new UnsupportedOperationException();
    }

    public void truncateDatabase
        (
            String database
        )
    {
        throw new UnsupportedOperationException();
    }

    public void unlockDatabase
        (
            String database
        )
    {
        throw new UnsupportedOperationException();
    }

    public void unlockRecords
        (
            String database,
            int[] mfnList
        )
    {
        throw new UnsupportedOperationException();
    }

    public void updateIniFile
        (
            String[] lines
        )
    {
        throw new UnsupportedOperationException();
    }

    public MarcRecord writeRecord
        (
            MarcRecord record,
            boolean lockFlag,
            boolean actualize,
            boolean dontParseResponse
        )
    {
        throw new UnsupportedOperationException();
    }

    public MarcRecord[] writeRecords
        (
            MarcRecord[] records,
            boolean lockFlag,
            boolean actualize
        )
    {
        throw new UnsupportedOperationException();
    }

    public void writeTextFile
        (
            FileSpecification specification
        )
    {
        throw new UnsupportedOperationException();
    }
}
