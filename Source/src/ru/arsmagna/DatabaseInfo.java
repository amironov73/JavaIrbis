package ru.arsmagna;

/**
 * Информация о базе данных ИРБИС.
 */
public class DatabaseInfo
{
    /**
     * Имя базы данных.
     */
    public String name;

    /**
     * Описание базы данных.
     */
    public String description;

    /**
     * Максимальный MFN.
     */
    public int maxMfn;

    //=========================================================================

    @Override
    public String toString()
    {
        return name + " - " + description;
    }
}
