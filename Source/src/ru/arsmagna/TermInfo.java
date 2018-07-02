package ru.arsmagna;

/**
 * Информация о поисковом терме.
 */
public class TermInfo
{
    /**
     * Количество ссылок.
     */
    public int count;

    /**
     * Поисковый термин.
     */
    public String text;

    //=========================================================================

    /**
     * Клонирование.
     * @return Копию.
     */
    public TermInfo clone()
    {
        TermInfo result = new TermInfo();
        result.count = count;
        result.text = text;

        return result;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return count + "#" + text;
    }
}
