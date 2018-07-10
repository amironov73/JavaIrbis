package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * MARC record subfield.
 */
public final class SubField implements Cloneable {

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
    public SubField() {
    }

    /**
     * Конструктор.
     *
     * @param code Код подполя.
     */
    public SubField(char code) {
        this.code = code;
    }

    /**
     * Конструктор.
     *
     * @param code  Код подполя.
     * @param value Значение подполя.
     */
    public SubField (char code, @Nullable String value) {
        this.code = code;
        this.value = value;
    }

    //=========================================================================

    /**
     * Сравнение подполей для сортировки.
     *
     * @param subField1 Первое подполе.
     * @param subField2 Второе подполе.
     * @return Результат сравнения.
     */
    @Contract(pure = true)
    public static int compare (@NotNull SubField subField1, @NotNull SubField subField2) {
        if (subField1 == null || subField2 == null) { throw new IllegalArgumentException(); }

        int result = compareCodes(subField1.code, subField2.code);
        if (result != 0) {
            return result;
        }

        result = subField1.value.compareTo(subField2.value);

        return result;
    }

    /**
     * Сравнение кодов подполей.
     *
     * @param code1 Первый код.
     * @param code2 Второй код.
     * @return Результат сравнения.
     */
    @Contract(pure = true)
    public static int compareCodes (char code1,char code2) {
        return Character.compare(normalize(code1), normalize(code2));
    }

    /**
     * Нормализация кода подполя.
     *
     * @param code Исходный код.
     * @return Нормализованный код.
     */
    @Contract(pure = true)
    public static char normalize (char code) {
        return Character.toLowerCase(code);
    }

    /**
     * Клонирование.
     *
     * @return Копию подполя.
     */
    @NotNull
    public final SubField clone() {
        SubField result = new SubField(code, value);

        return result;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return "^" + code + value;
    }
}
