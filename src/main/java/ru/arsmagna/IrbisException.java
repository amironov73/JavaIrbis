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
