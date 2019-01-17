// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.direct;

import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.io.IOException;

/**
 * Файл перекрестных ссылок XRF представляет собой
 * таблицу ссылок на записи файла документов.
 * Первая ссылка соответствует записи файла документов
 * с номером 1, вторая - 2 и т. д.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class XrfFile64 implements Closeable {

    /**
     * Name of the file.
     */
    public String fileName;

    /**
     * Access mode.
     */
    public int mode;

    //=========================================================================

    public XrfFile64(@NotNull String fileName, int mode) {
        this.fileName = fileName;
        this.mode = mode;
    }

    //=========================================================================

    /**
     * Lock the record.
     *
     * @param mfn MFN
     * @param flag true or false
     */
    public void lockRecord(int mfn, boolean flag)
        throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Read the record.
     *
     * @param mfn MFN
     * @return Record
     */
    public XrfRecord64 readRecord(int mfn)
        throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Reopen the file.
     * @param mode Mode to use
     */
    public void reopenFile(int mode)
        throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Write the record.
     *
     * @param record Record to write
     */
    public void writeRecord(@NotNull XrfRecord64 record)
        throws IOException {
        throw new UnsupportedOperationException();
    }

    //=========================================================================

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException();
    }
}
