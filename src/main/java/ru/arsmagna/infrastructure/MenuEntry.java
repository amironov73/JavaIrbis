package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Пара строк в меню.
 */
public final class MenuEntry {

    /**
     * Код (первая строка).
     */
    public String code;

    /**
     * Комментарий (вторая строка).
     */
    public String comment;

    //=========================================================================

    /**
     * Конструктор.
     */
    public MenuEntry() {
    }

    /**
     * Конструктор.
     *
     * @param code    Код.
     * @param comment Комментарий.
     */
    public MenuEntry
    (
            @NotNull String code,
            @Nullable String comment
    ) {
        this.code = code;
        this.comment = comment;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return code + " - " + comment;
    }
}
