// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.direct;

import ru.arsmagna.RecordStatus;

/**
 * Contains information about record offset and status.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class XrfRecord64 {

    /**
     * Fixed record size.
     */
    public static final int RECORD_SIZE = 12;

    //=========================================================================

    /**
     * MFN of the record.
     */
    public int mfn;

    /**
     * Offset of the record.
     */
    public long offset;

    /**
     * Status of the record.
     */
    public int status;

    //=========================================================================

    /**
     * Whether the record is locked?
     *
     * @return true or false
     */
    public boolean is_locked() {
        return (status & RecordStatus.LOCKED) != 0;
    }

    /**
     * Whether the record is deleted?
     *
     * @return true or false
     */
    public boolean is_deleted() {
        return (status & (RecordStatus.LOGICALLY_DELETED|RecordStatus.PHYSICALLY_DELETED)) != 0;
    }

    //=========================================================================

    @Override
    public String toString() {
        return String.format("MFN=%d, Offset=%d, Status=%d",
                mfn, offset, status);
    }
}
