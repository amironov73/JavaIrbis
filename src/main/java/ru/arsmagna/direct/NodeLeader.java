// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.direct;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

import static ru.arsmagna.Utility.readInt32Network;

/**
 * Лидер записи в файлах L01/N01.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NodeLeader {
    /**
     * Номер записи (начиная с 1; в N01 номер первой записи
     * равен номеру корневой записи дерева.
     */
    public int number;

    /**
     * Номер предыдущей записи (-1, если нет).
     */
    public int previous;

    /**
     * Номер следующей записи (-1, если нет).
     */
    public int next;

    /**
     * Число ключей в записи.
     */
    public int termCount;

    /**
     * Смещение на свободную позицию в записи
     * (от начала записи)
     */
    public int freeOffset;

    //=========================================================================

    /**
     * Считываем лидер из потока.
     *
     * @param stream Поток
     * @return Лидер
     * @throws IOException Ошибка ввода-вывода
     */
    public static NodeLeader read(@NotNull InputStream stream)
            throws IOException {
        NodeLeader result = new NodeLeader();
        result.number = readInt32Network(stream);
        result.previous = readInt32Network(stream);
        result.next = readInt32Network(stream);
        result.termCount = readInt32Network(stream);
        result.freeOffset = readInt32Network(stream);

        return result;
    }

    @Override
    public String toString() {
        return String.format("Number: %d, Previous: %d, "
            + "Next: %d, TermCount: %d, FreeOffset: %d",
                number, previous, next, termCount, freeOffset);
    }
}
