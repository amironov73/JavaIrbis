package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.arsmagna.infrastructure.ClientQuery;
import ru.arsmagna.infrastructure.IniFile;
import ru.arsmagna.infrastructure.ServerResponse;
import ru.arsmagna.search.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static ru.arsmagna.Utility.iif;
import static ru.arsmagna.Utility.isNullOrEmpty;
import static ru.arsmagna.infrastructure.CommandCode.*;

/**
 * Подключение к серверу ИРБИС64.
 */
public final class IrbisConnection {

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

    /**
     * Произвольные пользовательские данные.
     */
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    private boolean _isConnected;

    //=========================================================================

    /**
     * Конструктор.
     */
    public IrbisConnection() {
        host = "localhost";
        port = 6666;
        workstation = 'C';
        database = "IBIS";
    }

    //=========================================================================

    /**
     * Актуализация записи с указанным MFN.
     *
     * @param database Имя базы данных.
     * @param mfn      MFN.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final void actualizeRecord(@NotNull String database, int mfn) throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, ACTUALIZE_RECORD);
        query.addAnsi(database);
        query.add(mfn);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
        }
    }

    /**
     * Подключение к серверу ИРБИС64.
     *
     * @return INI-файл пользователя.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    @Nullable
    public final IniFile connect() throws IOException, IrbisException {
        if (_isConnected) {
            return null;
        }

        queryId = 0;
        clientId = 100000 + new Random().nextInt(800000);
        ClientQuery query = new ClientQuery(this, REGISTER_CLIENT);
        query.addAnsi(username);
        query.addAnsiNoLF(password);
        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            response.readAnsi();
            //response.readAnsi();
            _isConnected = true;

            IniFile result = IniFile.parse(response);

            return result;
        }
    }

    /**
     * Создание базы данных.
     *
     * @param databaseName Имя создаваемой базы.
     * @param description  Описание в свободной форме.
     * @param readerAccess Читатель будет иметь доступ?
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final void createDatabase(@NotNull String databaseName, @NotNull String description,
                                     boolean readerAccess) throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, CREATE_DATABASE);
        query.addAnsi(databaseName);
        query.addAnsi(description);
        query.add(readerAccess);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
        }
    }

    /**
     * Создание словаря в базе данных.
     *
     * @param databaseName Имя базы данных.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final void createDictionary(@NotNull String databaseName) throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, CREATE_DICTIONARY);
        query.addAnsi(databaseName);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
        }
    }

    /**
     * Удаление базы данных.
     *
     * @param databaseName Имя удаляемой базы.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final void deleteDatabase(@NotNull String databaseName) throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, DELETE_DATABASE);
        query.addAnsi(databaseName);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
        }
    }

    /**
     * Отключение от сервера.
     *
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void disconnect() throws IOException {
        if (!_isConnected) {
            return;
        }

        ClientQuery query = new ClientQuery(this, UNREGISTER_CLIENT);
        query.addAnsiNoLF(username);
        executeAndForget(query);
    }

    /**
     * Выполнение произвольного запроса.
     *
     * @param query Запрос.
     * @return Ответ сервера.
     * @throws IOException Ошибка ввода-вывода.
     */
    public final ServerResponse execute(@NotNull ClientQuery query) throws IOException {
        if (query == null) {
            throw new IllegalArgumentException();
        }

        Socket socket = new Socket(host, port);
        // socket.setSoTimeout(30_000); // milliseconds
        byte[] outputData = query.encode();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(outputData);
        // closing the OutputStream will close the associated socket!

        ServerResponse result = new ServerResponse(socket);
        // socket will be closed by ServerResponse.close method

        return result;

    }

    public final void executeAndForget(@NotNull ClientQuery query) throws IOException {
        ServerResponse response = null;
        try {
            response = execute(query);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public final void executeAnsi(@NotNull String... commands) throws IOException {
        if (commands == null) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, commands[0]);
        for (int i = 1; i < commands.length; i++) {
            query.addAnsi(commands[i]);
        }
        executeAndForget(query);
    }

    /**
     * Форматирование записи с указанным MFN.
     *
     * @param format Текст формата.
     * @param mfn    MFN записи.
     * @return Результат расформатирования.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final String formatRecord(@NotNull String format, int mfn) throws IOException, IrbisException {
        if (isNullOrEmpty(format)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, FORMAT_RECORD);
        query.addAnsi(database);
        query.addAnsi(format);
        query.add(1);
        query.add(mfn);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            String result = response.readRemainingUtfText();

            return result;
        }
    }

    /**
     * Форматирование виртуальной записи.
     *
     * @param format Текст формата.
     * @param record Запись.
     * @return Результат расформатирования.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final String formatRecord(@NotNull String format, @NotNull MarcRecord record) throws IOException, IrbisException {
        if (isNullOrEmpty(format)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, FORMAT_RECORD);
        query.addAnsi(database);
        query.addAnsi(format);
        query.add(-2);
        query.addUtf(record.toString());

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            String result = response.readRemainingUtfText();

            return result;
        }
    }

    public final ServerStat getServerStat() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, GET_SERVER_STAT);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            ServerStat result = ServerStat.parse(response);

            return result;
        }
    }

    public final IrbisVersion getServerVersion() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, SERVER_INFO);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            IrbisVersion result = IrbisVersion.parse(response);

            return result;
        }
    }

    public final int getMaxMfn(@NotNull String databaseName) throws IOException, IrbisException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, GET_MAX_MFN);
        query.addAnsiNoLF(databaseName);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();

            return response.returnCode;
        }
    }

    @NotNull
    public final UserInfo[] getUserList() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, GET_USER_LIST);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            UserInfo[] result = UserInfo.parse(response);

            return result;
        }
    }

    /**
     * Подключен ли клиент в данный момент?
     *
     * @return true, если подключен.
     */
    public final boolean isConnected() {
        return _isConnected;
    }

    /**
     * Получение списка файлов с сервера.
     *
     * @param specification Спецификация файлов (поддерживаются метасимволы).
     * @return Перечень файлов.
     * @throws IOException Ошибка ввода-вывода.
     */
    @NotNull
    public final String[] listFiles(@NotNull FileSpecification specification) throws IOException {
        if (specification == null) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, LIST_FILES);
        query.addAnsi(specification.toString());
        ArrayList<String> result = new ArrayList<String>();

        try (ServerResponse response = execute(query)) {
            String[] lines = response.readRemainingAnsiLines();
            for (String line : lines) {
                String[] converted = IrbisText.fromFullDelimiter(line);
                for (String one : converted) {
                    result.add(one);
                }
            }
        }

        return result.toArray(new String[0]);
    }

    /**
     * Получение списка файлов с сервера.
     *
     * @param specifications Спецификации файлов (поддерживаются метасимволы).
     * @return Перечень файлов.
     * @throws IOException Ошибка ввода-вывода.
     */
    @NotNull
    public final String[] listFiles(@NotNull FileSpecification[] specifications) throws IOException {
        if (specifications == null) {
            throw new IllegalArgumentException();
        }
        if (specifications.length == 0) {
            return new String[0];
        }

        ClientQuery query = new ClientQuery(this, LIST_FILES);
        for (FileSpecification specification : specifications) {
            query.addAnsi(specification.toString());
        }

        ArrayList<String> result = new ArrayList<>();
        try (ServerResponse response = execute(query)) {
            String[] lines = response.readRemainingAnsiLines();
            for (String line : lines) {
                String[] converted = IrbisText.fromFullDelimiter(line);
                for (String one : converted) {
                    result.add(one);
                }
            }
        }

        return result.toArray(new String[0]);
    }

    @NotNull
    public final IrbisProcessInfo[] listProcesses() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, GET_PROCESS_LIST);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            IrbisProcessInfo[] result = IrbisProcessInfo.parse(response);

            return result;
        }
    }

    public final void noOp() throws IOException {
        executeAnsi(NOP);

        //ClientQuery query = new ClientQuery(this, NOP);
        //execute(query);
    }

    public final void parseConnectionString(@NotNull String connectionString) {
        throw new UnsupportedOperationException();
    }

    public final byte[] readBinaryFile(@NotNull FileSpecification specification) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public final TermPosting[] readPostings(@NotNull PostingParameters parameters) throws IOException, IrbisException {
        if (parameters == null) {
            throw new IllegalArgumentException();
        }

        String databaseName = iif(parameters.database, database);
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, READ_POSTINGS);
        query.addAnsi(databaseName);
        query.add(parameters.numberOfPostings);
        query.add(parameters.firstPosting);
        query.addAnsi(parameters.format);
        if (parameters.listOftTerms == null) {
            query.addUtf(parameters.term);
        } else {
            for (String term : parameters.listOftTerms) {
                query.addUtf(term);
            }
        }

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_TERMS_CODES);
            TermPosting[] result = TermPosting.parse(response);

            return result;
        }
    }

    public final MarcRecord readRecord(int mfn) throws IOException, IrbisException {
        return readRecord(database, mfn);
    }

    public final MarcRecord readRecord(@NotNull String databaseName, int mfn) throws IOException, IrbisException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(databaseName);
        query.add(mfn);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_RECORD_CODES);
            String[] lines = response.readRemainingUtfLines();
            MarcRecord result = new MarcRecord();
            MarcRecord.parseSingle(result, lines);

            return result;
        }
    }

    public MarcRecord readRecord(@NotNull String databaseName, int mfn, int versionNumber) throws IOException, IrbisException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(databaseName);
        query.add(mfn);
        query.add(versionNumber);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_RECORD_CODES);
            String[] lines = response.readRemainingUtfLines();
            MarcRecord result = new MarcRecord();
            MarcRecord.parseSingle(result, lines);

            // TODO unlock the record
            unlockRecords(databaseName, mfn);

            return result;
        }
    }

    public TermInfo[] readTerms(@NotNull TermParameters parameters) throws IOException, IrbisException {
        if (parameters == null) {
            throw new IllegalArgumentException();
        }
        String databaseName = iif(parameters.database, database);
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        String command = parameters.reverseOrder ? READ_TERMS_REVERSE : READ_TERMS;
        ClientQuery query = new ClientQuery(this, command);
        query.addAnsi(databaseName);
        query.addUtf(parameters.startTerm);
        query.add(parameters.numberOfTerms);
        query.addAnsi(parameters.format);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_TERMS_CODES);
            TermInfo[] result = TermInfo.parse(response);

            return result;
        }
    }

    public final String readTextFile(@NotNull FileSpecification specification) throws IOException {
        if (specification == null) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        query.addAnsi(specification.toString());

        try (ServerResponse response = execute(query)) {
            String result = response.readAnsi();
            result = IrbisText.fromIrbisToDos(result);

            return result;
        }
    }

    @NotNull
    public final String[] readTextFiles(@NotNull FileSpecification[] specifications) throws IOException {
        if (specifications == null) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        for (FileSpecification specification : specifications) {
            if (specification != null) {
                query.addAnsi(specification.toString());
            }
        }

        try (ServerResponse response = execute(query)) {
            ArrayList<String> result = new ArrayList<>();
            while (true) {
                // TODO FIX ME!

                String text = response.readAnsi();
                if (isNullOrEmpty(text)) {
                    break;
                }

                text = IrbisText.fromIrbisToDos(text);
                result.add(text);
            }

            return result.toArray(new String[0]);
        }
    }

    public final void reloadDictionary(@NotNull String databaseName) throws IOException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        executeAnsi(RELOAD_DICTIONARY, databaseName);
    }

    public final void reloadMasterFile(@NotNull String databaseName) throws IOException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        executeAnsi(RELOAD_MASTER_FILE, databaseName);
    }

    public final void restartServer() throws IOException {
        executeAnsi(RESTART_SERVER);
    }

    public final int[] search(@NotNull String expression) throws IOException, IrbisException {
        if (isNullOrEmpty(expression)) {
            throw new IllegalArgumentException();
        }

        SearchParameters parameters = new SearchParameters();
        parameters.database = database;
        parameters.searchExpression = expression;
        parameters.numberOfRecords = 0;
        parameters.firstRecord = 1;

        return search(parameters);
    }

    public final int[] search(@NotNull SearchParameters parameters) throws IOException, IrbisException {
        // TODO handle > MAX_PACKET records

        if (parameters == null) {
            throw new IllegalArgumentException();
        }
        String databaseName = iif(parameters.database, database);
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, SEARCH);
        query.addAnsi(databaseName);
        query.addUtf(parameters.searchExpression);
        query.add(parameters.numberOfRecords);
        query.add(parameters.firstRecord);
        query.addAnsi(parameters.formatSpecification);
        query.add(parameters.minMfn);
        query.add(parameters.maxMfn);
        query.addAnsi(parameters.sequentialSpecification);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            int howMany = Math.min(response.readInt32(), Utility.MAX_PACKET);
            int[] result = new int[howMany];
            for (int i = 0; i < howMany; i++) {
                String line = response.readAnsi();
                String[] parts = line.split("#", 2);
                int mfn = Integer.parseInt(parts[0]);
                result[i] = mfn;
            }

            return result;
        }
    }

    public final void truncateDatabase(@NotNull String databaseName) throws IOException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        executeAnsi(EMPTY_DATABASE, databaseName);

//        ClientQuery query = new ClientQuery(this, EMPTY_DATABASE);
//        query.addAnsi(databaseName);
//
//        ServerResponse response = null;
//        try {
//            response = execute(query);
//        } finally {
//            if (response != null) {
//                response.close();
//            }
//        }
    }

    public final void unlockDatabase(@NotNull String databaseName) throws IOException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        executeAnsi(UNLOCK_DATABASE, databaseName);
//
//        ClientQuery query = new ClientQuery(this, UNLOCK_DATABASE);
//        query.addAnsi(databaseName);
//
//        executeAndForget(query);
    }

    public final void unlockRecords(@NotNull String databaseName, @NotNull int... mfnList) throws IOException {
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }
        if (mfnList == null || mfnList.length == 0) {
            return;
        }

        ClientQuery query = new ClientQuery(this, UNLOCK_RECORDS);
        query.addAnsi(databaseName);
        for (int mfn : mfnList) {
            query.add(mfn);
        }

        executeAndForget(query);
    }

    public final void updateIniFile(@NotNull String[] lines) throws IOException {
        if (lines == null || lines.length == 0) {
            return;
        }

        ClientQuery query = new ClientQuery(this, UPDATE_INI_FILE);
        for (String line : lines) {
            query.addAnsi(line);
        }

        executeAndForget(query);
    }

    /**
     * Сохранение записи на сервере.
     *
     * @param record            Запись для сохранения (новая или ранее считанная).
     * @param lockFlag          Оставить запись заблокированной?
     * @param actualize         Актуализировать запись?
     * @param dontParseResponse Не разбирать ответ сервера?
     * @return Новый максимальный MFN.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final int writeRecord(@NotNull MarcRecord record, boolean lockFlag,
                                 boolean actualize, boolean dontParseResponse) throws IOException, IrbisException {
        if (record == null) {
            throw new IllegalArgumentException();
        }
        String databaseName = iif(record.database, database);
        if (isNullOrEmpty(databaseName)) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, UPDATE_RECORD);
        query.addAnsi(databaseName);
        query.add(lockFlag);
        query.add(actualize);
        query.addUtf(record.toString());

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();

            if (!dontParseResponse) {
                record.fields.clear();

                String[] lines = response.readRemainingUtfLines();
                if (lines.length > 1) {
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(lines[0]);
                    temp.addAll(Arrays.asList(IrbisText.fromShortDelimiter(lines[1])));
                    lines = temp.toArray(new String[0]);
                    MarcRecord.parseSingle(record, lines);
                }
            }

            return response.returnCode;
        }
    }

    public final int writeRecords(@NotNull MarcRecord[] records, boolean lockFlag,
                                  boolean actualize, boolean dontParseResponse) throws IOException, IrbisException {
        if (records == null || records.length == 0) {
            return getMaxMfn(database);
        }
        if (records.length == 1) {
            return writeRecord(records[0], lockFlag, actualize, dontParseResponse);
        }

        ClientQuery query = new ClientQuery(this, SAVE_RECORD_GROUP);
        query.add(lockFlag);
        query.add(actualize);
        for (MarcRecord record : records) {
            String line = iif(record.database, database)
                    + IrbisText.IRBIS_DELIMITER
                    + record.toString();
            query.addUtf(line);
        }

        try (ServerResponse response = execute(query)) {
            response.getReturnCode();

            if (!dontParseResponse) {
                String[] lines = response.readRemainingUtfLines();
                for (int i = 0; i < records.length; i++) {
                    MarcRecord record = records[i];
                    record.fields.clear();
                    String[] splitted = IrbisText.fromShortDelimiter(lines[i]);
                    MarcRecord.parseSingle(record, splitted);
                }
            }

            return response.returnCode;
        }
    }

    public final void writeTextFile(@NotNull FileSpecification specification) throws IOException {
        if (specification == null) {
            throw new IllegalArgumentException();
        }

        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        query.addAnsi(specification.toString());
        try (ServerResponse response = execute(query)) {
            response.getReturnCode();
        }
    }
}
