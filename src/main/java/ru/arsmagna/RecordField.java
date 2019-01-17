// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static ru.arsmagna.SubField.compareCodes;
import static ru.arsmagna.Utility.isNullOrEmpty;
import static ru.arsmagna.Utility.nullableToString;

/**
 * MARC record field.
 */
@SuppressWarnings("WeakerAccess")
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
    public int repeat;

    /**
     * Значение поля до первого разделителя.
     */
    public String value;

    /**
     * Подполя.
     */
    public final Collection<SubField> subFields = new ArrayList<>();

    /**
     * Произвольные пользовательские данные.
     */
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    /**
     * Default constructor.
     */
    public RecordField() {
    }

    /**
     * Initializing constructor.
     *
     * @param tag The field tag.
     */
    public RecordField (int tag) {
        this.tag = tag;
    }

    /**
     * Initializing constructor.
     *
     * @param tag   The field tag.
     * @param value The field value.
     */
    public RecordField (int tag, @Nullable String value) {
        this.tag = tag;
        this.value = value;
    }

    /**
     * Initializing constructor.
     * @param tag The field tag.
     * @param subFields Sub-fields.
     */
    public RecordField (int tag, SubField... subFields) {
        this.tag = tag;
        for (SubField subField: subFields) {
            this.subFields.add(subField);
        }
    }

    //=========================================================================

    public RecordField add(char code, @Nullable Object value)
    {
        String text = nullableToString(value);
        subFields.add(new SubField(code, text));

        return this;
    }

    public RecordField add(char code, boolean condition, @Nullable Object value) {
        if (condition) {
            String text = nullableToString(value);
            subFields.add(new SubField(code, text));
        }

        return this;
    }

    public RecordField addNonEmpty(char code, @Nullable Object value) {
        String text = nullableToString(value);
        if (!isNullOrEmpty(text)) {
            subFields.add(new SubField(code, text));
        }

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public RecordField assignFrom(@NotNull RecordField other) {
        clear();
        value = other.value;
        for (SubField subField: other.subFields) {
            subFields.add(subField.clone());
        }

        return this;
    }

    public RecordField assignTo(@NotNull RecordField other) {
        other.assignFrom(this);

        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public RecordField clear() {
        value = null;
        subFields.clear();

        return this;
    }

    /**
     * Клонирование поля с подполями.
     *
     * @return Копию поля.
     */
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public RecordField clone() {
        RecordField result = new RecordField(tag, value);
        for (SubField sub : subFields) {
            result.subFields.add(sub.clone());
        }

        return result;
    }

    @Nullable
    @Contract(pure = true)
    public SubField getFirstSubField(char code) {
        for (SubField subField: subFields) {
            if (compareCodes(code, subField.code) == 0) {
                return subField;
            }
        }

        return null;
    }

    @Nullable
    @Contract(pure = true)
    public String getFirstSubFieldValue(char code) {
        for (SubField subField: subFields) {
            if (compareCodes(code, subField.code) == 0) {
                return subField.value;
            }
        }

        return null;
    }

    public SubField[] getSubField(char code) {
        LinkedList<SubField> result = new LinkedList<>();
        for (SubField subField: subFields) {
            if (compareCodes(code, subField.code) == 0){
                result.add(subField);
            }
        }

        return result.toArray(new SubField[0]);
    }

    @Nullable
    @Contract(pure = true)
    public SubField getSubField(char code, int occurrence) {
        for (SubField subField: subFields) {
            if (compareCodes(code, subField.code) == 0) {
                if (--occurrence < 0) {
                    return subField;
                }
            }
        }

        return null;
    }

    public String[] getSubFieldValue(char code) {
        LinkedList<String> result = new LinkedList<>();
        for (SubField subField: subFields) {
            if (compareCodes(code, subField.code) == 0) {
                String text = subField.value;
                if (!isNullOrEmpty(text)) {
                    result.add(text);
                }
            }
        }

        return result.toArray(new String[0]);
    }

    @Nullable
    @Contract(pure = true)
    public String getSubFieldValue(char code, int occurrence) {
        for (SubField subField: subFields) {
            if (compareCodes(code, subField.code) == 0) {
                if (--occurrence < 0) {
                    return subField.value;
                }
            }
        }

        return null;
    }

    /**
     * Разбор строки.
     *
     * @param line Text to parse.
     * @return Parsed field.
     * @throws IOException Error during input-output.
     */
    @NotNull
    public static RecordField parse (@NotNull String line) throws IOException {
        StringReader reader = new StringReader(line);
        String tagText = Utility.readTo(reader, '#');
        int tag = FastNumber.parseInt32(tagText);
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
     * Разбор строки.
     *
     * @param tag Field tag
     * @param line Text to parse
     * @return Parsed field
     * @throws IOException Error during input-output
     */
    @NotNull
    public static RecordField parse (int tag, @NotNull String line) throws IOException {
        StringReader reader = new StringReader(line);
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

    public String toText() {
        StringBuilder result = new StringBuilder();

        if (!isNullOrEmpty(value)) {
            result.append(value);
        }

        for (SubField subField: subFields) {
            String text = subField.toString();
            if (!isNullOrEmpty(text)) {
                result.append(text);
            }
        }

        return result.toString();
    }

    //=========================================================================

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        ProtocolText.encodeField(result, this);

        return result.toString();
    }
}
