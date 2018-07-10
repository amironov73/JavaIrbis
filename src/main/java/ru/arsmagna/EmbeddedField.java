package ru.arsmagna;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Работа со встроенными полями.
 */
public final class EmbeddedField {
    /**
     * Код по умолчанию, используемый для встраивания полей.
     */
    public static final char DEFAULT_CODE = '1';

    /**
     * Получение встроенных полей.
     *
     * @param field Поле для извлечения встроенных полей.
     * @param sign  Признак встроенного поля.
     * @return Массив встроенных полей.
     */
    @NotNull
    public static RecordField[] getEmbeddedFields (@NotNull RecordField field, char sign) {
        if (field == null) { throw new IllegalArgumentException(); }

        ArrayList<RecordField> result = new ArrayList<>();
        RecordField found = null;
        for (SubField subField : field.subFields) {
            if (subField.code == sign) {
                if (found != null) {
                    result.add(found);
                }
                String value = subField.value;
                String tagValue = value.substring(0, 3);
                int tag = Integer.parseInt(tagValue);
                found = new RecordField(tag);
                if (tagValue.startsWith("00") && value.length() > 3) {
                    found.value = value.substring(3);
                }
            } else {
                if (found != null) {
                    found.subFields.add(subField.clone());
                }
            }
        }

        if (found != null) {
            result.add(found);
        }

        return result.toArray(new RecordField[0]);
    }

    /**
     * Получение встроенных полей.
     *
     * @param field Поле для извлечения встроенных полей.
     * @return Массив встроенных полей.
     */
    @NotNull
    public static RecordField[] getEmbeddedFields (@NotNull RecordField field) {
        return getEmbeddedFields(field, DEFAULT_CODE);
    }
}
