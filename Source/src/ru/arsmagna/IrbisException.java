package ru.arsmagna;

/**
 * Исключение, связанное с ИРБИС.
 */
public class IrbisException extends Exception
{
    public int errorCode;

    public IrbisException()
    {
    }

    public IrbisException(int errorCode)
    {
        this.errorCode = errorCode;
    }
}
