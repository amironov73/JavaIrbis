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

    /**
     * Сравнение подполей для сортировки.
     * @param subField1 Первое подполе.
     * @param subField2 Второе подполе.
     * @return Результат сравнения.
     */
    public static int compare
        (
            SubField subField1,
            SubField subField2
        )
    {
        int result = compareCodes(subField1.code, subField2.code);
        if (result != 0)
        {
            return result;
        }

        result = subField1.value.compareTo(subField2.value);

        return result;
    }

    /**
     * Сравнение кодов подполей.
     * @param code1 Первый код.
     * @param code2 Второй код.
     * @return Результат сравнения.
     */
    public static int compareCodes
        (
            char code1,
            char code2
        )
    {
        return new Character(normalize(code1)).compareTo(normalize(code2));
    }

    /**
     * Нормализация кода подполя.
     * @param code Исходный код.
     * @return Нормализованный код.
     */
    public static char normalize
        (
            char code
        )
    {
        return Character.toLowerCase(code);
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "^" + code + value;
    }
}
