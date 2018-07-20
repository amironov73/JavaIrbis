package ru.arsmagna;

import org.jetbrains.annotations.Contract;

/**
 * Информация о базе данных ИРБИС.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class DatabaseInfo {

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
    @Contract(pure = true)
    public String toString() {
        return name + " - " + description;
    }
}
