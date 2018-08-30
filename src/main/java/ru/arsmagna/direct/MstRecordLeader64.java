package ru.arsmagna.direct;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static ru.arsmagna.Utility.readInt32Network;
import static ru.arsmagna.Utility.readInt64Network;
import static ru.arsmagna.Utility.writeInt32Network;
import static ru.arsmagna.Utility.writeInt64Network;

/**
 * Leader for MST record.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class MstRecordLeader64 {

    /**
     * Fixed size of the leader record.
     */
    public static final int LEADER_SIZE = 32;

    //=========================================================================

    /**
     * Номер записи в файле документов.
     */
    public int mfn;

    /**
     * Длина записи (в документации сказано, что всегда
     * четное число, но по факту это не так).
     */
    public int length;

    /**
     * Ссылка на предыдущую версию записи.
     */
    public long previous;

    /**
     * Смещение (базовый адрес) полей переменной длины
     * (это общая часть лидера и справочника записи в байтах).
     */
    public int base;

    /**
     * Число полей в записи (т. е. число входов в справочнике).
     */
    public int nvf;

    /**
     * Индикатор записи (логически удаленная и т. п.).
     */
    public int status;

    /**
     * Номер версии записи.
     */
    public int version;

    //=========================================================================

    public static MstRecordLeader64 read(@NotNull InputStream stream) throws IOException {
        MstRecordLeader64 result = new MstRecordLeader64();
        result.mfn = readInt32Network(stream);
        result.length = readInt32Network(stream);
        result.previous = readInt64Network(stream);
        result.base = readInt32Network(stream);
        result.nvf = readInt32Network(stream);
        result.version = readInt32Network(stream);
        result.status = readInt32Network(stream);

        return result;
    }

    public void write(@NotNull OutputStream stream) throws IOException {
        writeInt32Network(stream, mfn);
        writeInt32Network(stream, length);
        writeInt64Network(stream, previous);
        writeInt32Network(stream, base);
        writeInt32Network(stream, nvf);
        writeInt32Network(stream, version);
        writeInt32Network(stream, status);
    }

    @Override
    public String toString() {
        return String.format("mfn=%d, length=%d, previous=%d, "
            + "base=%d, nvf=%d, status=%d, version=%d",
                mfn, length, previous, base, nvf, status, version);
    }
}
