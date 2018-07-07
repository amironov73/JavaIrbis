package ru.arsmagna;

import org.jetbrains.annotations.*;

/**
 * Текстовое представление записи, используемое
 * в протоколе ИРБИС64-сервер.
 */
public final class ProtocolText
{
    /**
     * Разделитель текста, используемый ИРБИС64.
     */
    public static final String DELIMITER = "\u001F\u001E";

    //=========================================================================

    /**
     * Encode the subfield.
     * @param builder
     * @param subField
     */
    public static void EncodeSubField
        (
            @NotNull StringBuilder builder,
            @NotNull SubField subField
        )
    {
        builder.append(SubField.DELIMITER);
        builder.append(subField.code);
        builder.append(subField.value);
    }

    /**
     * Encode the field.
     * @param builder
     * @param field
     */
    public static void EncodeField
        (
            @NotNull StringBuilder builder,
            @NotNull RecordField field
        )
    {
        builder.append(field.tag);
        builder.append('#');
        builder.append(field.value);
        for (SubField subField : field.subFields)
        {
            EncodeSubField(builder, subField);
        }
    }

    /**
     * Кодирование записи в коиентское представление.
     * @param record
     * @return Закодированная запись.
     */
    @NotNull
    public static String EncodeRecord
        (
            @NotNull MarcRecord record
        )
    {
        StringBuilder result = new StringBuilder();

        result
            .append(record.mfn)
            .append('#')
            .append(record.status)
            .append(DELIMITER);
        result
            .append(0)
            .append('#')
            .append(record.version)
            .append(DELIMITER);
        for (RecordField field: record.fields)
        {
            EncodeField(result, field);
        }

        return result.toString();
    }
}
