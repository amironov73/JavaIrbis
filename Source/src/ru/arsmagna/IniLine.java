package ru.arsmagna;

/**
 * Строчка в INI-файле.
 */
public class IniLine
{
    /**
     * Ключ.
     */
    public String key;

    /**
     * Значение.
     */
    public String value;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public IniLine()
    {
    }

    /**
     * Конструктор.
     * @param key Ключ.
     * @param value Значение.
     */
    public IniLine
        (
            String key,
            String value
        )
    {
        this.key = key;
        this.value = value;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return key + "=" + value;
    }
}
