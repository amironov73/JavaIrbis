package ru.arsmagna;

/**
 * Параметры для команды SearchCommand.
 */
public class SearchParameters
{
    /**
     * Имя базы данных.
     */
    public String database;

    /**
     * Номер первой записи.
     */
    public int firstRecord;

    /**
     * Формат.
     */
    public String formatSpecification;

    /**
     * Максимальный MFN.
     */
    public int maxMfn;

    /**
     * Минимальный MFN.
     */
    public int minMfn;

    /**
     * Число записей.
     */
    public int numberOfRecords;

    /**
     * Выражение для поиска по словарю.
     */
    public String searchExpression;

    /**
     * Выражение для последовательного поиска.
     */
    public String sequentialSpecification;

    /**
     * Выражение для локальной фильтрации.
     */
    public String filterSpecification;

    /**
     * Признак кодировки UTF8.
     */
    public boolean uftFormat;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public SearchParameters()
    {
        firstRecord = 1;
    }

    //=========================================================================

    /**
     * Клонирование.
     * @return Копию.
     */
    public SearchParameters clone()
    {
        SearchParameters result = new SearchParameters();
        result.database = database;
        result.firstRecord = firstRecord;
        result.formatSpecification = formatSpecification;
        result.maxMfn = maxMfn;
        result.minMfn = minMfn;
        result.numberOfRecords = numberOfRecords;
        result.searchExpression = searchExpression;
        result.sequentialSpecification = sequentialSpecification;
        result.filterSpecification = filterSpecification;
        result.uftFormat = uftFormat;

        return result;
    }

    //=========================================================================
}
