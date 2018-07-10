package ru.arsmagna.direct;

/**
 * Первая запись в файле документов – управляющая
 * запись, которая формируется (в момент определения
 * базы данных или при ее инициализации) и поддерживается
 * автоматически.
 */
public final class MstControlRecord {

    /**
     * Размер управляющей записи.
     */
    public static final int RECORD_SIZE = 36;

    /**
     * Позиция индикатора блокировки базы данных
     * в управляющей записи.
     */
    public static final long LOCK_FLAG_POSITION = 32;

    //=========================================================================

    /**
     * Резерв.
     */
    public int ctlMfn;

    /**
     * Номер записи файла документов, назначаемый
     * для следующей записи, создаваемой в базе данных.
     */
    public int nextMfn;

    /**
     * Смещение свободного места в файле.
     */
    public long nextPosition;

    /**
     * Резерв.
     */
    public int mftType;

    /**
     * Резерв.
     */
    public int recCnt;

    /**
     * Резерв.
     */
    public int reserved1;

    /**
     * Резерв.
     */
    public int reserved2;

    /**
     * Индикатор блокировки базы данных.
     */
    public int blocked;
}
