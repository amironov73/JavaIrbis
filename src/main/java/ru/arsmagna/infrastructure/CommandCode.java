package ru.arsmagna.infrastructure;

/**
 * Коды команд.
 */
public final class CommandCode {
    /**
     * Получение признака монопольной блокировки базы данных.
     */
    public static final String EXCLUSIVE_DATABASE_LOCK = "#";

    /**
     * Получение списка удаленных, неактуализированных
     * и заблокированных записей.
     */
    public static final String RECORD_LIST = "0";

    /**
     * Получение версии сервера.
     */
    public static final String SERVER_INFO = "1";

    /**
     * Получение статистики по базе данных.
     */
    public static final String DATABASE_STAT = "2";

    /**
     * IRBIS_FORMAT_ISO_GROUP.
     */
    public static final String FORMAT_ISO_GROUP = "3";

    /**
     * ???
     */
    public static final String UNKNOWN_COMMAND_4 = "4";

    /**
     * Глобальная корректировка.
     */
    public static final String GLOBAL_CORRECTION = "5";

    /**
     * Сохранение группы записей.
     */
    public static final String SAVE_RECORD_GROUP = "6";

    /**
     * Печать.
     */
    public static final String PRINT = "7";

    /**
     * Запись параметров в ini-файл, расположенный на сервере.
     */
    public static final String UPDATE_INI_FILE = "8";

    /**
     * IRBIS_IMPORT_ISO.
     */
    public static final String IMPORT_ISO = "9";

    /**
     * Регистрация клиента на сервере.
     */
    public static final String REGISTER_CLIENT = "A";

    /**
     * Разрегистрация клиента.
     */
    public static final String UNREGISTER_CLIENT = "B";

    /**
     * Чтение записи, ее расформатирование.
     */
    public static final String READ_RECORD = "C";

    /**
     * Сохранение записи.
     */
    public static final String UPDATE_RECORD = "D";

    /**
     * Разблокировка записи.
     */
    public static final String UNLOCK_RECORD = "E";

    /**
     * Актуализация записи.
     */
    public static final String ACTUALIZE_RECORD = "F";

    /**
     * Форматирование записи или группы записей.
     */
    public static final String FORMAT_RECORD = "G";

    /**
     * Получение терминов и ссылок словаря, форматирование записей
     */
    public static final String READ_TERMS = "H";

    /**
     * Получение ссылок для термина (списка терминов).
     */
    public static final String READ_POSTINGS = "I";

    /**
     * Глобальная корректировка виртуальной записи.
     */
    public static final String CORRECT_VIRTUAL_RECORD = "J";

    /**
     * Поиск записей с опциональным форматированием
     */
    public static final String SEARCH = "K";

    /**
     * Получение/сохранение текстового файла, расположенного
     * на сервере (группы текстовых файлов).
     */
    public static final String READ_DOCUMENT = "L";

    /**
     * IRBIS_BACKUP.
     */
    public static final String BACKUP = "M";

    /**
     * Пустая операция. Периодическое подтверждение
     * соединения с сервером.
     */
    public static final String NOP = "N";

    /**
     * Получение максимального MFN для базы данных.
     */
    public static final String GET_MAX_MFN = "O";

    /**
     * Получение терминов и ссылок словаря в обратном порядке.
     */
    public static final String READ_TERMS_REVERSE = "P";

    /**
     * Разблокирование записей.
     */
    public static final String UNLOCK_RECORDS = "Q";

    /**
     * Полнотекстовый поиск.
     */
    public static final String FULL_TEXT_SEARCH = "R";

    /**
     * Опустошение базы данных.
     */
    public static final String EMPTY_DATABASE = "S";

    /**
     * Создание базы данных.
     */
    public static final String CREATE_DATABASE = "T";

    /**
     * Разблокирование базы данных.
     */
    public static final String UNLOCK_DATABASE = "U";

    /**
     * Чтение ссылок для заданного MFN.
     */
    public static final String GET_RECORD_POSTINGS = "V";

    /**
     * Удаление базы данных.
     */
    public static final String DELETE_DATABASE = "W";

    /**
     * Реорганизация мастер-файла.
     */
    public static final String RELOAD_MASTER_FILE = "X";

    /**
     * Реорганизация словаря.
     */
    public static final String RELOAD_DICTIONARY = "Y";

    /**
     * Создание поискового словаря заново.
     */
    public static final String CREATE_DICTIONARY = "Z";

    /**
     * Получение статистики работы сервера.
     */
    public static final String GET_SERVER_STAT = "+1";

    /**
     * ???
     */
    public static final String UNKNOWN_COMMAND_PLUS_2 = "+2";

    /**
     * Получение списка запущенных процессов.
     */
    public static final String GET_PROCESS_LIST = "+3";

    /**
     * ???
     */
    public static final String UNKNOWN_COMMAND_PLUS_4 = "+4";

    /**
     * ???
     */
    public static final String UNKNOWN_COMMAND_PLUS_5 = "+5";

    /**
     * ???
     */
    public static final String UNKNOWN_COMMAND_PLUS_6 = "+6";

    /**
     * Сохранение списка пользователей.
     */
    public static final String SET_USER_LIST = "+7";

    /**
     * Перезапуск сервера.
     */
    public static final String RESTART_SERVER = "+8";

    /**
     * Получение списка пользователей.
     */
    public static final String GET_USER_LIST = "+9";

    /**
     * Получение списка файлов на сервере.
     */
    public static final String LIST_FILES = "!";
}
