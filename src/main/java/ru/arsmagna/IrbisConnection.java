package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.arsmagna.infrastructure.ClientQuery;
import ru.arsmagna.infrastructure.IniFile;
import ru.arsmagna.infrastructure.RawRecord;
import ru.arsmagna.infrastructure.ServerResponse;
import ru.arsmagna.search.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;

import static ru.arsmagna.Utility.iif;
import static ru.arsmagna.Utility.isNullOrEmpty;
import static ru.arsmagna.infrastructure.CommandCode.*;

/**
 * Подключение к серверу ИРБИС64.
 */
@SuppressWarnings({"WeakerAccess", "UnnecessaryLocalVariable", "unused"})
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

    private Stack<String> _databaseStack;

    //=========================================================================

    /**
     * Конструктор.
     */
    public IrbisConnection() {
        host = "localhost";
        port = 6666;
        workstation = 'C';
        database = "IBIS";

        _databaseStack = new Stack<>();
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
    public final void actualizeRecord(@NotNull String database, int mfn)
            throws IOException, IrbisException {
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
    public final void createDictionary(@NotNull String databaseName)
            throws IOException, IrbisException {
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
    public final void deleteDatabase(@NotNull String databaseName)
            throws IOException, IrbisException {
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
        Socket socket = new Socket(host, port);
        // socket.setSoTimeout(30_000); // milliseconds

        byte[][] outputData = query.encode();
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(outputData[0]);
        outputStream.write(outputData[1]);
        // closing the OutputStream will close the associated socket!

        ServerResponse result = new ServerResponse(socket);
        // socket will be closed by ServerResponse.close method

        return result;

    }

    public final void executeAndForget(@NotNull ClientQuery query) throws IOException {
        try (ServerResponse response = execute(query)) {
            response.nop();
        }
    }

    public final void executeAnsi(@NotNull String... commands) throws IOException {
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

    /**
     * Получение статистики с сервера.
     *
     * @return Полученная статистика.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final ServerStat getServerStat() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, GET_SERVER_STAT);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            ServerStat result = ServerStat.parse(response);

            return result;
        }
    }

    /**
     * Получение версии сервера.
     *
     * @return Версия сервера.
     * @throws IOException    Ошибка ввода-вываода.
     * @throws IrbisException Ошибка протокола.
     */
    public final IrbisVersion getServerVersion() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, SERVER_INFO);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            IrbisVersion result = IrbisVersion.parse(response);

            return result;
        }
    }

    /**
     * Получение максимального MFN для указанной базы данных.
     *
     * @param databaseName База данных.
     * @return MFN, который будет присвоен следующей записи.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final int getMaxMfn(@NotNull String databaseName)
            throws IOException, IrbisException {
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

    /**
     * Получкение списка пользователей с сервера.
     *
     * @return Массив пользователей.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
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
    @Contract(pure = true)
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
    public final String[] listFiles(@NotNull FileSpecification specification)
            throws IOException {
        ClientQuery query = new ClientQuery(this, LIST_FILES);
        query.addAnsi(specification.toString());
        ArrayList<String> result = new ArrayList<>();

        try (ServerResponse response = execute(query)) {
            String[] lines = response.readRemainingAnsiLines();
            for (String line : lines) {
                String[] converted = IrbisText.fromFullDelimiter(line);
                Collections.addAll(result, converted);
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
    public final String[] listFiles(@NotNull FileSpecification[] specifications) throws IOException {
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
                result.addAll(Arrays.asList(converted));
            }
        }

        return result.toArray(new String[0]);
    }

    /**
     * Получение списка серверных процессов.
     *
     * @return Массив процессов.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final IrbisProcessInfo[] listProcesses() throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, GET_PROCESS_LIST);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode();
            IrbisProcessInfo[] result = IrbisProcessInfo.parse(response);

            return result;
        }
    }

    /**
     * Пустая операция (используется для подтверждения подключения клиента).
     *
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void noOp() throws IOException {
        executeAnsi(NOP);
    }

    /**
     * Разбор строки подключения.
     *
     * @param connectionString Строка подключения.
     * @throws IrbisException Ошибка при разборе строки.
     */
    public final void parseConnectionString(@NotNull String connectionString)
            throws IrbisException {
        String[] items = connectionString.split(";");
        for (String item : items) {
            String[] parts = item.split("=", 2);
            if (parts.length != 2) {
                throw new IrbisException();
            }
            String name = parts[0].trim().toLowerCase(), value = parts[1].trim();
            if (isNullOrEmpty(name) || isNullOrEmpty(value)) {
                throw new IrbisException();
            }

            switch (name) {
                case "host":
                case "server":
                case "address":
                    host = value;
                    break;

                case "port":
                    port = Integer.parseInt(value);
                    break;

                case "user":
                case "username":
                case "name":
                case "login":
                    username = value;
                    break;

                case "pwd":
                case "password":
                    password = value;
                    break;

                case "db":
                case "catalog":
                case "database":
                    database = value;
                    break;

                case "arm":
                case "workstation":
                    workstation = value.charAt(0);
                    break;

                case "data":
                    userData = value;
                    break;

                case "debug":
                    // TODO implement
                    break;

                default:
                    throw new IrbisException();
            }
        }
    }

    /**
     * Восстанавливае подключение к прошлой базе данных,
     * запомненной с помощью pushDatabase.
     *
     * @return Прошлая база данных.
     */
    public String popDatabase() {
        String result = database;
        database = _databaseStack.pop();
        return result;
    }

    /**
     * Расформатирование таблицы.
     * @param definition Определение таблицы.
     * @return Результат расформатирования.
     * @throws IOException Ошибка ввода-вывода.
     */
    public String printTable(@NotNull TableDefinition definition) throws IOException {
        ClientQuery query = new ClientQuery(this, PRINT);
        query.addAnsi(definition.database);
        query.addAnsi(definition.table);
        query.addAnsi(""); // instead of headers
        query.addAnsi(definition.mode);
        query.addUtf(definition.searchQuery);
        query.add(definition.minMfn);
        query.add(definition.maxMfn);
        query.addUtf(definition.sequentialQuery);
        query.addAnsi(""); // instead of MFN list

        try (ServerResponse response = execute(query)) {
            return response.readRemainingUtfText();
        }
    }

    /**
     * Устанавливает подключение к новой базе данных, запоминая
     * предыдущую базу.
     *
     * @param newDatabase Новая база данных.
     * @return Предыдущая база данных.
     */
    public String pushDatabase(@NotNull String newDatabase) {
        String result = database;
        _databaseStack.push(database);
        database = newDatabase;
        return result;
    }

    public final InputStream readBinaryFile(@NotNull FileSpecification specification) {
        throw new UnsupportedOperationException();
    }

    /**
     * Считывание постингов из поискового индекса.
     *
     * @param parameters Параметры постингов.
     * @return Массив постингов.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final TermPosting[] readPostings(@NotNull PostingParameters parameters)
            throws IOException, IrbisException {
        String databaseName = iif(parameters.database, database);
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

    public final RawRecord readRawRecord(@NotNull String databaseName, int mfn)
            throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(databaseName);
        query.add(mfn);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_RECORD_CODES);
            String[] lines = response.readRemainingUtfLines();
            RawRecord result = new RawRecord();
            result.parseSingle(lines);

            return result;
        }

    }

    /**
     * Считывание записи с указанным MFN.
     *
     * @param mfn MFN записи.
     * @return Считанная запись.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final MarcRecord readRecord(int mfn) throws IOException, IrbisException {
        return readRecord(database, mfn);
    }

    /**
     * Считывание записи с указанным MFN.
     *
     * @param databaseName База данных.
     * @param mfn          MFN записи.
     * @return Считанная запись.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final MarcRecord readRecord(@NotNull String databaseName, int mfn)
            throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(databaseName);
        query.add(mfn);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_RECORD_CODES);
            String[] lines = response.readRemainingUtfLines();
            MarcRecord result = new MarcRecord();
            result.parseSingle(lines);

            return result;
        }
    }

    /**
     * Считывание указанной версии записи.
     *
     * @param databaseName  База данных.
     * @param mfn           MFN записи.
     * @param versionNumber Номер версии.
     * @return Считанная запись.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public MarcRecord readRecord(@NotNull String databaseName, int mfn, int versionNumber)
            throws IOException, IrbisException {
        ClientQuery query = new ClientQuery(this, READ_RECORD);
        query.addAnsi(databaseName);
        query.add(mfn);
        query.add(versionNumber);

        try (ServerResponse response = execute(query)) {
            response.checkReturnCode(Utility.READ_RECORD_CODES);
            String[] lines = response.readRemainingUtfLines();
            MarcRecord result = new MarcRecord();
            result.parseSingle(lines);

            // unlock the record
            unlockRecords(databaseName, mfn);

            return result;
        }
    }

    /**
     * Получение термов словаря.
     *
     * @param parameters Параметры термов.
     * @return Массив термов.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public TermInfo[] readTerms(@NotNull TermParameters parameters) throws IOException, IrbisException {
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

    /**
     * Получение текстового файла с сервера.
     *
     * @param specification Спецификация файла.
     * @return Текст файла (пустая строка, если файл не найден).
     * @throws IOException Ошибка ввода-вывода.
     */
    public final String readTextFile(@NotNull FileSpecification specification) throws IOException {
        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        query.addAnsi(specification.toString());

        try (ServerResponse response = execute(query)) {
            String result = response.readAnsi();
            result = IrbisText.fromIrbisToDos(result);

            return result;
        }
    }

    /**
     * Получение текстовых файлов с сервера.
     *
     * @param specifications Спецификации файлов.
     * @return Тексты файлов.
     * @throws IOException Ошибка ввода-вывода.
     */
    public final String[] readTextFiles(@NotNull FileSpecification[] specifications)
            throws IOException {
        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        for (FileSpecification specification : specifications) {
            query.addAnsi(specification.toString());
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

    /**
     * Пересоздание словаря.
     *
     * @param databaseName База данных.
     * @throws IOException Ошибка ввода-вывода.
     */
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

    /**
     * Перезапуск сервера (без утери подключенных клиентов).
     *
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void restartServer() throws IOException {
        executeAnsi(RESTART_SERVER);
    }

    /**
     * Поиск записей.
     *
     * @param expression Поисковое выражение.
     * @return Найденные MFN.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
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

    /**
     * Поиск записей.
     *
     * @param parameters Параметры поиска.
     * @return Найденные MFN.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final int[] search(@NotNull SearchParameters parameters)
            throws IOException, IrbisException {
        // TODO handle > MAX_PACKET records

        String databaseName = iif(parameters.database, database);
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

    /**
     * Опустошение базы данных.
     *
     * @param databaseName База данных.
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void truncateDatabase(@NotNull String databaseName) throws IOException {
        executeAnsi(EMPTY_DATABASE, databaseName);
    }

    /**
     * Разблокирование базы данных.
     *
     * @param databaseName База данных.
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void unlockDatabase(@NotNull String databaseName) throws IOException {
        executeAnsi(UNLOCK_DATABASE, databaseName);
    }

    /**
     * Разблокирование записей.
     *
     * @param databaseName База данных.
     * @param mfnList      Список MFN.
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void unlockRecords(@NotNull String databaseName, @NotNull int... mfnList) throws IOException {
        if (mfnList.length == 0) {
            return;
        }

        ClientQuery query = new ClientQuery(this, UNLOCK_RECORDS);
        query.addAnsi(databaseName);
        for (int mfn : mfnList) {
            query.add(mfn);
        }

        executeAndForget(query);
    }

    /**
     * Обновление строк серверного INI-файла.
     *
     * @param lines Измененные строки.
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void updateIniFile(@NotNull String[] lines) throws IOException {
        if (lines.length == 0) {
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
                                 boolean actualize, boolean dontParseResponse)
            throws IOException, IrbisException {
        String databaseName = iif(record.database, database);
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
                    record.parseSingle(lines);
                }
            }

            return response.returnCode;
        }
    }

    /**
     * Сохранение записей на сервере.
     *
     * @param records           Записи для сохранения (новая или ранее считанная).
     * @param lockFlag          Оставить записи заблокированными?
     * @param actualize         Актуализировать записи?
     * @param dontParseResponse Не разбирать ответ сервера?
     * @return Новый максимальный MFN.
     * @throws IOException    Ошибка ввода-вывода.
     * @throws IrbisException Ошибка протокола.
     */
    public final int writeRecords(@NotNull MarcRecord[] records, boolean lockFlag,
                                  boolean actualize, boolean dontParseResponse)
            throws IOException, IrbisException {
        if (records.length == 0) {
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
                    String[] splitted = IrbisText.fromShortDelimiter(lines[i]);
                    record.parseSingle(splitted);
                }
            }

            return response.returnCode;
        }
    }

    /**
     * Сохранить текстовый файл на сервере.
     *
     * @param specification Спецификация (включая текст для сохранения).
     * @throws IOException Ошибка ввода-вывода.
     */
    public final void writeTextFile(@NotNull FileSpecification specification) throws IOException {
        ClientQuery query = new ClientQuery(this, READ_DOCUMENT);
        query.addAnsi(specification.toString());
        try (ServerResponse response = execute(query)) {
            response.getReturnCode();
        }
    }
}
