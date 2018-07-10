package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * MARC record field.
 */
public final class RecordField implements Cloneable {

    /**
     * Нет метки, т. е. метка ещё не установлена.
     */
    public static final int NO_TAG = 0;

    //=========================================================================

    /**
     * Метка поля.
     */
    public int tag;

    /**
     * Повторение поля.
     */
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
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
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public RecordField() {
        subFields = new ArrayList<>();
    }

    /**
     * Конструктор.
     *
     * @param tag Метка поля.
     */
    public RecordField (int tag) {
        subFields = new ArrayList<>();
        this.tag = tag;
    }

    /**
     * Конструктор.
     *
     * @param tag   Метка поля.
     * @param value Значение поля.
     */
    public RecordField (int tag, @Nullable String value) {
        subFields = new ArrayList<>();
        this.tag = tag;
        this.value = value;
    }

    //=========================================================================

    /**
     * Разбор строки.
     *
     * @param line
     * @return
     * @throws IOException
     */
    @NotNull
    public static RecordField parse (@NotNull String line) throws IOException {
        if (isNullOrEmpty(line)) { throw new IllegalArgumentException(); }

        StringReader reader = new StringReader(line);
        String tagText = Utility.readTo(reader, '#');
        int tag = Integer.parseInt(tagText);
        RecordField result = new RecordField(tag);
        result.value = Utility.readTo(reader, '^');
        while (true) {
            int next = reader.read();
            if (next < 0) {
                break;
            }

            char code = Character.toLowerCase((char) next);
            String value = Utility.readTo(reader, '^');
            SubField subField = new SubField(code, value);
            result.subFields.add(subField);
        }

        return result;
    }

    /**
     * Клонирование поля с подполями.
     *
     * @return Копию поля.
     */
    public final RecordField clone() {
        RecordField result = new RecordField(tag, value);
        for (SubField sub : subFields) {
            result.subFields.add(sub.clone());
        }

        return result;
    }

    //=========================================================================

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        ProtocolText.encodeField(result, this);

        return result.toString();
    }
}
