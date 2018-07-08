package ru.arsmagna;

import org.jetbrains.annotations.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * MARC record field.
 */
public final class RecordField
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
        subFields = new ArrayList<>();
    }

    /**
     * Конструктор.
     * @param tag Метка поля.
     */
    public RecordField
        (
            int tag
        )
    {
        subFields = new ArrayList<>();
        this.tag = tag;
    }

    /**
     * Конструктор.
     * @param tag Метка поля.
     * @param value Значение поля.
     */
    public RecordField
        (
            int tag,
            @Nullable String value
        )
    {
        subFields = new ArrayList<>();
        this.tag = tag;
        this.value = value;
    }

    //=========================================================================

    /**
     * Клонирование поля с подполями.
     * @return Копию поля.
     */
    @NotNull
    public final RecordField clone()
    {
        RecordField result = new RecordField(tag, value);
        for (SubField sub: subFields)
        {
            result.subFields.add(sub.clone());
        }

        return result;
    }

    /**
     * Разбор строки.
     * @param line
     * @return
     * @throws IOException
     */
    public static RecordField parse
        (
            @NotNull String line
        )
        throws IOException
    {
        StringReader reader = new StringReader(line);
        String tagText = Utility.readTo(reader, '#');
        int tag = Integer.parseInt(tagText);
        RecordField result = new RecordField(tag);
        result.value = Utility.readTo(reader, '^');
        while (true)
        {
            int next = reader.read();
            if (next < 0)
            {
                break;
            }

            char code = Character.toLowerCase((char)next);
            String value = Utility.readTo(reader, '^');
            SubField subField = new SubField(code, value);
            result.subFields.add(subField);
        }

        return result;
    }

    //=========================================================================

    @NotNull
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        ProtocolText.EncodeField(result, this);

        return result.toString();
    }
}
