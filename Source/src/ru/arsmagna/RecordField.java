package ru.arsmagna;

import java.util.ArrayList;
import java.util.Collection;

/**
 * MARC record field.
 */
public class RecordField
{
    /**
     * Нет метки, т. е. метка ещё не установлена.
     */
    public static final int NO_TAG = 0;

    /**
     * Метка поля.
     */
    public int tag;

    /**
     * Повторение поля.
     */
    public int repeat;

    /**
     * Значение поля до первого разделителя.
     */
    public String value;

    /**
     * Подполя.
     */
    public Collection<SubField> subFields;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public RecordField()
    {
        subFields = new ArrayList<SubField>();
    }

    /**
     * Конструктор.
     * @param tag Метка поля.
     */
    public RecordField(int tag)
    {
        subFields = new ArrayList<SubField>();
        this.tag = tag;
    }

    /**
     * Конструктор.
     * @param tag Метка поля.
     * @param value Значение поля.
     */
    public RecordField(int tag, String value)
    {
        subFields = new ArrayList<SubField>();
        this.tag = tag;
        this.value = value;
    }

    //=========================================================================

    /**
     * Клонирование поля с подполями.
     * @return Копию поля.
     */
    public RecordField Clone()
    {
        RecordField result = new RecordField(tag, value);
        for (SubField sub: subFields)
        {
            result.subFields.add(sub.clone());
        }

        return result;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        ProtocolText.EncodeField(result, this);

        return result.toString();
    }
}
