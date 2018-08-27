package ru.arsmagna.direct;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static ru.arsmagna.Utility.*;

/**
 * Управляющая запись IFP-файла в ИРБИС64.
 */
public class IfpControlRecord64 {

    /**
     * Размер управляющей записи (байты).
     */
    public static final int RECORD_SIZE = 20;

    //=========================================================================

    /**
     * Ссылка на свободное место в IFP-файле.
     */
    public long nextOffset;

    /**
     * Количество блоков в N01 файле.
     */
    public int nodeBlockCount;

    /**
     * Количество блоков в L01 файле.
     */
    public int leafBlockCount;

    /**
     * Резерв.
     */
    public int reserved;

    //=========================================================================

    /**
     * Чтение управляющей записи из потока.
     *
     * @param stream Поток
     * @return Управляющая запись
     * @throws IOException Ошибка ввода-вывода
     */
    public static IfpControlRecord64 read(@NotNull InputStream stream)
        throws IOException {
        IfpControlRecord64 result = new IfpControlRecord64();
        result.nextOffset = readInt64Network(stream);
        result.nodeBlockCount = readInt32Network(stream);
        result.leafBlockCount = readInt32Network(stream);
        result.reserved = readInt32Network(stream);

        return result;
    }

    /**
     * Сохранение управляющей записи в поток.
     *
     * @param stream Поток
     * @throws IOException Ошибка ввода-вывода
     */
    public void write(@NotNull OutputStream stream)
        throws IOException {
        writeInt64Network(stream, nextOffset);
        writeInt32Network(stream, nodeBlockCount);
        writeInt32Network(stream, leafBlockCount);
        writeInt32Network(stream, reserved);
    }

    //=========================================================================

    @Override
    public String toString() {
        return String.format("NextOffset=%ld, NodeBlockCount=%d, "
            + "LeafBlockCount=%d, Reserved=%d", nextOffset,
                nodeBlockCount, leafBlockCount, reserved);
    }
}
