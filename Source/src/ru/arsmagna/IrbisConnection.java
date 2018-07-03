package ru.arsmagna;

import ru.arsmagna.infrastructure.*;

import static ru.arsmagna.infrastructure.CommandCode.*;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

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
        host = "localhost";
        port = 6666;
        workstation = 'C';
        database = "IBIS";
    }

    //=========================================================================

    public void actualizeRecord
        (
            String database,
            int mfn
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, ACTUALIZE_RECORD);
        query.addAnsi(database);
        query.add(mfn);
        ServerResponse response = execute(query);
        response.checkReturnCode();
    }

    public String[] connect() throws IOException, IrbisException
    {
        queryId = 0;
        clientId = 100000 + new Random().nextInt(800000);
        ClientQuery query = new ClientQuery(this, REGISTER_CLIENT);
        query.addAnsi(username);
        query.addAnsiNoLF(password);
        ServerResponse response = execute(query);
        response.checkReturnCode();
        response.readAnsi();
        response.readAnsi();
        return response.readRemainingAnsiLines();
    }

    public void createDatabase
        (
            String databaseName,
            String description,
            boolean readerAccess,
            String template
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, CREATE_DATABASE);
        query.addAnsi(databaseName);
        query.addAnsi(description);
        query.add(readerAccess);
        ServerResponse response = execute(query);
        response.checkReturnCode();
    }

    public void createDictionary
        (
            String databaseName
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, CREATE_DICTIONARY);
        query.addAnsi(databaseName);
        ServerResponse response = execute(query);
        response.checkReturnCode();
    }

    public void deleteDatabase
        (
            String databaseName
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, DELETE_DATABASE);
        query.addAnsi(databaseName);
        ServerResponse response = execute(query);
        response.checkReturnCode();
    }

    public void disconnect() throws IOException
    {
        ClientQuery query = new ClientQuery(this, UNREGISTER_CLIENT);
        query.addAnsiNoLF(username);
        execute(query);
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
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, "G");
        query.addAnsi(database);
        query.addAnsi(format);
        query.add(1);
        query.add(mfn);
        ServerResponse response = execute(query);
        response.checkReturnCode();
        String result = response.readRemainingUtfText();

        return result;
    }

    public String formatRecord
        (
            String format,
            MarcRecord record
        ) throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, "G");
        query.addAnsi(database);
        query.addAnsi(format);
        query.add(-2);
        query.addUtf(record.toString());
        ServerResponse response = execute(query);
        response.checkReturnCode();
        String result = response.readRemainingUtfText();

        return result;
    }

    public int getMaxMfn() throws IOException, IrbisException
    {
        return getMaxMfn(database);
    }

    public ServerStat getServerStat() throws IOException
    {
        ClientQuery query = new ClientQuery(this, GET_SERVER_STAT);
        ServerResponse response = execute(query);
        response.getReturnCode();
        ServerStat result = ServerStat.parse(response);

        return result;
    }

    public IrbisVersion getServerVersion() throws IOException
    {
        ClientQuery query = new ClientQuery(this, SERVER_INFO);
        ServerResponse response = execute(query);
        response.getReturnCode();
        IrbisVersion result = IrbisVersion.parse(response);

        return result;
    }

    public int getMaxMfn
        (
            String databaseName
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, "O");
        query.addAnsiNoLF(databaseName);
        ServerResponse response = execute(query);
        response.checkReturnCode();

        return response.returnCode;
    }

    public String[] listFiles
        (
            FileSpecification specification
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, LIST_FILES);
        query.addAnsi(specification.toString());
        ServerResponse response = execute(query);
        ArrayList<String> result = new ArrayList<String>();
        String[] lines = response.readRemainingAnsiLines();
        for (String line : lines)
        {
            String[] converted = IrbisText.fromIrbisToManyLines(line);
            for (String one : converted)
            {
                result.add(one);
            }
        }

        return result.toArray(new String[0]);
    }

    public String[] listFiles
        (
            FileSpecification[] specifications
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, LIST_FILES);
        for (FileSpecification specification : specifications)
        {
            query.addAnsi(specification.toString());
        }
        ServerResponse response = execute(query);
        ArrayList<String> result = new ArrayList<String>();
        String[] lines = response.readRemainingAnsiLines();
        for (String line : lines)
        {
            String[] converted = IrbisText.fromIrbisToManyLines(line);
            for (String one : converted)
            {
                result.add(one);
            }
        }

        return result.toArray(new String[0]);
    }

    public void noOp() throws IOException
    {
        ClientQuery query = new ClientQuery(this, NOP);
        execute(query);
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
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, READ_POSTINGS);
        query.addAnsi(parameters.database);
        query.add(parameters.numberOfPostings);
        query.add(parameters.numberOfPostings);
        query.addAnsi(parameters.format);
        if (parameters.listOftTerms == null)
        {
            query.addUtf(parameters.term);
        } else
        {
            for (String term : parameters.listOftTerms)
            {
                query.addUtf(term);
            }
        }
        ServerResponse response = execute(query);
        response.checkReturnCode(Utility.READ_TERMS_CODES);
        TermPosting[] result = TermPosting.parse(response);

        return result;
    }

    public MarcRecord readRecord
        (
            String database,
            int mfn
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(database);
        query.add(mfn);
        ServerResponse response = execute(query);
        response.checkReturnCode(Utility.READ_RECORD_CODES);
        String[] lines = response.readRemainingUtfLines();
        MarcRecord result = new MarcRecord();
        MarcRecord.ParseSingle(result, lines);

        return result;
    }

    public MarcRecord readRecord
        (
            String database,
            int mfn,
            int versionNumber
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(database);
        query.add(mfn);
        query.add(versionNumber);
        ServerResponse response = execute(query);
        response.checkReturnCode(Utility.READ_RECORD_CODES);
        String[] lines = response.readRemainingUtfLines();
        MarcRecord result = new MarcRecord();
        MarcRecord.ParseSingle(result, lines);

        return result;
    }

    public TermInfo[] readTerms
        (
            TermParameters parameters
        )
        throws IOException, IrbisException
    {
        String command = parameters.reverseOrder ? READ_TERMS_REVERSE : READ_TERMS;
        ClientQuery query = new ClientQuery(this, command);
        query.addAnsi(parameters.database);
        query.addUtf(parameters.startTerm);
        query.add(parameters.numberOfTerms);
        query.addAnsi(parameters.format);
        ServerResponse response = execute(query);
        response.checkReturnCode(Utility.READ_TERMS_CODES);
        TermInfo[] result = TermInfo.parse(response);

        return result;
    }

    public String readTextFile
        (
            FileSpecification specification
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        query.addAnsi(specification.toString());
        ServerResponse response = execute(query);
        String text = response.readAnsi();
        text = IrbisText.fromIrbisToSingleLine(text);
        return text;
    }

    public String[] readTextFiles
        (
            FileSpecification[] specifications
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        for (FileSpecification specification : specifications)
        {
            query.addAnsi(specification.toString());
        }
        ServerResponse response = execute(query);
        ArrayList<String> result = new ArrayList<>();
        while (true)
        {
            // TODO FIX ME!

            String text = response.readAnsi();
            if (Utility.isNullOrEmpty(text))
            {
                break;
            }

            text = IrbisText.fromIrbisToSingleLine(text);
            result.add(text);
        }

        return result.toArray(new String[0]);
    }

    public void reloadDictionary
        (
            String databaseName
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, RELOAD_DICTIONARY);
        query.addAnsi(databaseName);
        execute(query);
    }

    public void reloadMasterFile
        (
            String databaseName
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, RELOAD_MASTER_FILE);
        query.addAnsi(databaseName);
        execute(query);
    }

    public void restartServer() throws IOException
    {
        ClientQuery query = new ClientQuery(this, RESTART_SERVER);
        execute(query);
    }

    public int[] search
        (
            String expression
        )
        throws IOException, IrbisException
    {
        ClientQuery query = new ClientQuery(this, SEARCH);
        query.addAnsi(database);
        query.addUtf(expression);
        query.add(0); // number of records - MAX_PACKET
        query.add(1); // first record
        ServerResponse response = execute(query);
        response.checkReturnCode();
        int howMany = Math.min(response.readInt32(), Utility.MAX_PACKET);
        int[] result = new int[howMany];
        for (int i = 0; i < howMany; i++)
        {
            String line = response.readAnsi();
            String[] parts = line.split("#", 2);
            int mfn = Integer.parseInt(parts[0]);
            result[i] = mfn;
        }

        return result;
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
            String databaseName
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, EMPTY_DATABASE);
        query.addAnsi(databaseName);
        execute(query);
    }

    public void unlockDatabase
        (
            String databaseName
        )
        throws IOException
    {
        ClientQuery query = new ClientQuery(this, UNLOCK_DATABASE);
        query.addAnsi(databaseName);
        execute(query);
    }

    public void unlockRecords
        (
            String databaseName,
            int[] mfnList
        )
        throws IOException
    {
        if (mfnList.length == 0)
        {
            return;
        }

        ClientQuery query = new ClientQuery(this, UNLOCK_RECORDS);
        query.addAnsi(databaseName);
        for (int mfn : mfnList)
        {
            query.add(mfn);
        }
        execute(query);
    }

    public void updateIniFile
        (
            String[] lines
        )
        throws IOException
    {
        if (lines.length == 0)
        {
            return;
        }

        ClientQuery query = new ClientQuery(this, UPDATE_INI_FILE);
        for (String line : lines)
        {
            query.addAnsi(line);
        }
        execute(query);
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
