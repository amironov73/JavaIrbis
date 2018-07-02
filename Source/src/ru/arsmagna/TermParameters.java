package ru.arsmagna;

/**
 * Параметры для команды ReadTermsCommand.
 */
public class TermParameters
{
    /**
     * Имя базы данных.
     */
    public String database;

    /**
     * Число считываемых термов.
     */
    public int numberOfTerms;

    /**
     * Возвращать в обратном порядке.
     */
    public boolean reverseOrder;

    /**
     * Начальный терм.
     */
    public String startTerm;

    /**
     * Формат.
     */
    public String format;

    //=========================================================================

    /**
     * Клонирование.
     * @return Копию.
     */
    public TermParameters clone()
    {
        TermParameters result = new TermParameters();
        result.database = database;
        result.numberOfTerms = numberOfTerms;
        result.reverseOrder = reverseOrder;
        result.startTerm = startTerm;
        result.format = format;

        return result;
    }

    //=========================================================================
}
