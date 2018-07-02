package ru.arsmagna;

/**
 * Пара строк в меню.
 */
public class MenuEntry
{
    /**
     * Код.
     */
    public String code;

    /**
     * Комментарий
     */
    public String comment;

    //=========================================================================

    @Override
    public String toString()
    {
        return code + " - " + comment;
    }
}
