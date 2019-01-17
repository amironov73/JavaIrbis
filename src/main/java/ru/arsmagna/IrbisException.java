// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

/**
 * Исключение, связанное с ИРБИС.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class IrbisException extends Exception {
    public int errorCode;

    public IrbisException() {
    }

    public IrbisException (int errorCode) {
        this.errorCode = errorCode;
    }

    public IrbisException (String message) {
        super(message);
    }

    public IrbisException (Throwable cause) {
        super(cause);
    }
}
