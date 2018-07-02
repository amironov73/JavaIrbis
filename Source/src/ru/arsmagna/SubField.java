package ru.arsmagna;

/**
 * MARC record subfield.
 */
public class SubField
{
    /**
     * Нет кода подполя, т. е. код пока не задан.
     */
    public static final char NO_CODE = '\0';

    /**
     * Разделитель подполей.
     */
    public static final char DELIMITER = '^';

    /**
     * Код подполя.
     */
    public char code;

    /**
     * Значение подполя.
     */
    public String value;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public SubField()
    {
    }

    /**
     * Конструктор.
     * @param code Код подполя.
     */
    public SubField(char code)
    {
        this.code = code;
    }

    /**
     * Конструктор.
     * @param code Код подполя.
     * @param value Значение подполя.
     */
    public SubField(char code, String value)
    {
        this.code = code;
        this.value = value;
    }

    //=========================================================================

    /**
     * Клонирование.
     * @return Копию подполя.
     */
    public SubField clone()
    {
        SubField result = new SubField(code, value);

        return result;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "^" + code + value;
    }
}
