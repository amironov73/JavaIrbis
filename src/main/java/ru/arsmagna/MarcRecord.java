package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import static ru.arsmagna.Utility.isNullOrEmpty;
import static ru.arsmagna.Utility.nullableToString;

/**
 * MARC record.
 */
public final class MarcRecord implements Cloneable {
    /**
     * Database that contains the record.
     */
    public String database;

    /**
     * Master file number.
     */
    public int mfn;

    /**
     * The record status.
     */
    public int status;

    /**
     * Версия записи. Нумеруется с нуля.
     */
    public int version;

    public Collection<RecordField> fields;

    /**
     * Библиографическое описание.
     */
    public String description;

    /**
     * Используется при сортировке записей.
     */
    public String sortKey;

    /**
     * Индекс документа.
     */
    public String index;

    /**
     * Произвольные пользовательские данные.
     */
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    /**
     * Default constructor.
     */
    public MarcRecord() {
        fields = new ArrayList<>();
    }

    //=========================================================================

    public MarcRecord addField(int tag, Object value) {
        RecordField field = new RecordField(tag, nullableToString(value));
        fields.add(field);

        return this;
    }

    public MarcRecord addField(int tag, boolean condition, Object value) {
        if (condition) {
            RecordField field = new RecordField(tag, nullableToString(value));
            fields.add(field);
        }

        return this;
    }

    public MarcRecord addField(int tag, SubField... subFields) {
        RecordField field = new RecordField(tag);
        for (SubField subField: subFields) {
            field.subFields.add(subField);
        }

        return this;
    }

    public MarcRecord addField(int tag, boolean condition, SubField... subFields) {
        if (condition) {
            RecordField field = new RecordField(tag);
            for (SubField subField : subFields) {
                field.subFields.add(subField);
            }
        }

        return this;
    }

    public MarcRecord addNonEmptyField(int tag, Object value) {
        String text = nullableToString(value);
        if (!isNullOrEmpty(text)) {
            RecordField field = new RecordField(tag, text);
            fields.add(field);
        }

        return this;
    }

    public MarcRecord clear() {
        fields.clear();

        return this;
    }

    /**
     * Клонирование записи со всеми полями.
     *
     * @return Копию записи.
     */
    public MarcRecord clone() {
        MarcRecord result = new MarcRecord();
        result.database = database;
        result.mfn = mfn;
        result.status = status;
        result.description = description;
        result.sortKey = sortKey;
        result.index = index;
        for (RecordField field : fields) {
            result.fields.add(field.clone());
        }

        return result;
    }

    @Nullable
    public String fm(int tag) {
        for (RecordField field: fields) {
            if (field.tag == tag) {
                return field.value;
            }
        }

        return null;
    }

    @Nullable
    @Contract(pure = true)
    public String fm(int tag, char code) {
        for (RecordField field: fields) {
            if (field.tag == tag) {
                return field.getFirstSubFieldValue(code);
            }
        }

        return null;
    }

    @NotNull
    public String[] fma(int tag) {
        LinkedList<String> result = new LinkedList<>();
        for (RecordField field: fields) {
            if (field.tag == tag) {
                if (!isNullOrEmpty(field.value)) {
                    result.add(field.value);
                }
            }
        }

        return result.toArray(new String[0]);
    }

    @NotNull
    public String[] fma(int tag, char code) {
        LinkedList<String> result = new LinkedList<>();
        for (RecordField field: fields) {
            if (field.tag == tag) {
                String text = field.getFirstSubFieldValue(code);
                if (!isNullOrEmpty(text)) {
                    result.add(text);
                }
            }
        }

        return result.toArray(new String[0]);
    }

    @NotNull
    public RecordField[] getField(int tag) {
        LinkedList<RecordField> result = new LinkedList<>();
        for (RecordField field: fields) {
            if (field.tag == tag) {
                result.add(field);
            }
        }

        return result.toArray(new RecordField[0]);
    }

    @Nullable
    @Contract(pure = true)
    public RecordField getField(int tag, int occurrence) {
        for (RecordField field: fields) {
            if (field.tag == tag && --occurrence < 0) {
                return field;
            }
        }

        return null;
    }

    @Nullable
    @Contract(pure = true)
    public RecordField getFirstField(int tag) {
        for (RecordField field: fields) {
            if (field.tag == tag) {
                return field;
            }
        }

        return null;
    }

    /**
     * Разбор строк, возвращаемых сервером.
     *
     * @param record Запись, в которую помещать результат.
     * @param text   Строки, содержащие запись.
     * @throws IOException Ошибка ввода-вывода.
     */
    public static void parseSingle (@NotNull MarcRecord record, @NotNull String[] text) throws IOException {
        if (record == null || text == null) {
            throw new IllegalArgumentException();
        }
        if (text.length == 0) {
            return;
        }

        String line = text[0];
        String[] parts = line.split("#");
        record.mfn = Integer.parseInt(parts[0]);
        if (parts.length != 1) {
            record.status = Integer.parseInt(parts[1]);
        }
        line = text[1];
        parts = line.split("#");
        record.version = Integer.parseInt(parts[1]);
        for (int i = 2; i < text.length; i++) {
            RecordField field = RecordField.parse(text[i]);
            record.fields.add(field);
        }
    }

    //=========================================================================

    @Override
    public String toString() {
        return ProtocolText.encodeRecord(this);
    }
}
