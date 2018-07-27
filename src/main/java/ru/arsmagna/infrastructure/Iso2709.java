package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.FastNumber;
import ru.arsmagna.MarcRecord;
import ru.arsmagna.RecordField;
import ru.arsmagna.SubField;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Импорт и экспорт записей в формате ISO2709.
 */
@SuppressWarnings("WeakerAccess")
public class Iso2709 {

    /**
     * Length of the record marker.
     */
    public static final int MARKER_LENGTH = 24;

    /**
     * Record delimiter.
     */
    public static final byte RECORD_DELIMITER = 0x1D;

    /**
     * Field delimiter.
     */
    public static final byte FIELD_DELIMITER = 0x1E;

    /**
     * Subfield delimiter.
     */
    public static final byte SUBFIELD_DELIMITER = 0x1F;

    //=========================================================================

    /**
     * Parse the stream for the record.
     *
     * @param stream Stream to parse.
     * @param charset Charset to use.
     * @return Parsed record or null if end-of-stream detected.
     */
    @Nullable
    public static MarcRecord readRecord(@NotNull InputStream stream,
                                        @NotNull Charset charset)
            throws IOException {
        MarcRecord result = new MarcRecord();

        // Считываем длину записи
        byte[] marker = new byte[5];
        if (stream.read(marker) != marker.length) {
            return null;
        }

        // а затем и ее остаток
        int recordLength = FastNumber.parseInt32(marker, 0, marker.length);
        byte[] record = new byte[recordLength];
        int need = recordLength - marker.length;
        if (stream.read(record, marker.length, need) != need) {
            return null;
        }

        // Простая проверка, что мы имеем дело
        // с нормальной ISO-записью
        if (record[recordLength-1] != RECORD_DELIMITER) {
            return null;
        }

        // Превращаем запись в Unicode
        char[] chars = charset.decode(ByteBuffer.wrap(record)).array();
        int indicatorLength = FastNumber.parseInt32(record, 10, 1);
        int baseAddress = FastNumber.parseInt32(record, 12, 5);

        // Пошли по полям при помощи справочника
        for (int directory = MARKER_LENGTH; ; directory += 12) {
            // Переходим к следующему полю.
            // Если нарвались на разделитель, значит, справочник закончился
            if (record[directory] == FIELD_DELIMITER) {
                break;
            }

            int tag = FastNumber.parseInt32(record, directory, 3);
            int fieldLength = FastNumber.parseInt32(record, directory+3, 4);
            int fieldOffset = baseAddress
                    + FastNumber.parseInt32(record, directory+7, 5);
            RecordField field = new RecordField(tag);
            result.fields.add(field);
            if (tag < 10) {
                // Фиксированное поле
                // не может содержать подполей и индикаторов
                field.value = new String(chars, fieldOffset, fieldLength-1);
            }
            else {
                // Поле переменной длины
                // Содержит два однобайтных индикатора
                // может содерджать подполя

                // пропускаем индикаторы
                int start = fieldOffset + indicatorLength;
                int stop = fieldOffset + fieldLength - indicatorLength + 1;
                int position = start;

                // Ищем значение поля до первого разделителя
                while (position < stop) {
                    if (record[start] == SUBFIELD_DELIMITER) {
                        break;
                    }
                    position++;
                }

                // Если есть текст до первого разделителя, запоминаем его
                if (position != start) {
                    field.value = new String(chars, start, position - start);
                }

                // Просматриваем подполя
                start = position;
                while (start < stop) {
                    position = start + 1;
                    while (position < stop) {
                        if (record[position] == SUBFIELD_DELIMITER) {
                            break;
                        }
                        position++;
                    }
                    SubField subField = new SubField(chars[start+1],
                            new String(chars, start + 2, position - start - 2));
                    field.subFields.add(subField);
                    start = position;
                }
            }
        }

        return result;
    }

}
