package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static ru.arsmagna.Utility.nullToEmpty;

/**
 * INI-file line with key and value.
 */
public final class IniLine {

    /**
     * Key (case insensitive).
     */
    public String key;

    /**
     * Value (may be null).
     */
    public String value;

    //=========================================================================

    /**
     * Default constructor.
     */
    public IniLine() {
    }

    /**
     * Initializing constructor.
     *
     * @param key   The key (can't be null).
     * @param value Value (can be null).
     */
    public IniLine (@NotNull String key, @Nullable String value) {
        if (key == null) {
            throw new IllegalArgumentException();
        }

        this.key = key;
        this.value = value;
    }

    //=========================================================================

    /**
     * Whether two given keys are the same?
     * @param first First key.
     * @param second Second key.
     * @return true for same keys.
     */
    public static boolean sameKey(@NotNull String first, @NotNull String second) {
        if (first == null || second == null) throw new IllegalArgumentException();

        return first.equalsIgnoreCase(second);
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return Utility.toVisible(key) + "=" + nullToEmpty(value);
    }
}
