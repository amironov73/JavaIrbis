package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Строчка в INI-файле.
 */
public final class IniLine {
    /**
     * Ключ.
     */
    public String key;

    /**
     * Значение.
     */
    public String value;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public IniLine() {
    }

    /**
     * Конструктор.
     *
     * @param key   Ключ.
     * @param value Значение.
     */
    public IniLine
    (
            @NotNull String key,
            @Nullable String value
    ) {
        this.key = key;
        this.value = value;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return key + "=" + value;
    }
}
