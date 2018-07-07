package ru.arsmagna;

import org.jetbrains.annotations.*;

/**
 * Информация о базе данных ИРБИС.
 */
public final class DatabaseInfo
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
    @NotNull
    @Contract(pure = true)
    public String toString()
    {
        return name + " - " + description;
    }
}
