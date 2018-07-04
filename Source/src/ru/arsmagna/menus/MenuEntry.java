package ru.arsmagna.menus;

/**
 * Пара строк в меню.
 */
public class MenuEntry
{
    /**
     * Код (первая строка).
     */
    public String code;

    /**
     * Комментарий (вторая строка).
     */
    public String comment;

    //=========================================================================

    /**
     * Конструктор.
     */
    public MenuEntry()
    {
    }

    /**
     * Конструктор.
     * @param code Код.
     * @param comment Комментарий.
     */
    public MenuEntry
        (
            String code,
            String comment
        )
    {
        this.code = code;
        this.comment = comment;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return code + " - " + comment;
    }
}
