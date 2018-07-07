package ru.arsmagna;

/**
 * Данные для команды TableCommand.
 */
public final class TableDefinition
{
    /**
     * Имя базы данных.
     */
    public String database;

    /**
     * Имя таблицы.
     */
    public String table;

    /**
     * Заголовки таблицы.
     */
    public String[] headers;

    /**
     * Режим таблицы.
     */
    public String mode;

    /**
     * Поисковый запрос.
     */
    public String searchQuery;

    /**
     * Минимальный MFN.
     */
    public int minMfn;

    /**
     * Максимальный MFN.
     */
    public int maxMfn;

    /**
     * Запрос для последовательного поиска.
     */
    public String sequentialQuery;

    /**
     * Список MFN, по которым строить таблицу.
     */
    public int[] mfnList;
}
