package ru.arsmagna.direct;

/**
 * Лидер записи в файлах L01/N01.
 */
public class NodeLeader
{
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
}
