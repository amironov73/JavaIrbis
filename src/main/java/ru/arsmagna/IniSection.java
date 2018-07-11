package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

import static ru.arsmagna.IniLine.sameKey;
import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * INI-file section with name and lines.
 */
public final class IniSection {
    /**
     * The section name. Can be null.
     */
    public String name;

    /**
     * Lines collection.
     */
    public Collection<IniLine> lines;

    //=========================================================================

    /**
     * Default constructor.
     */
    public IniSection() {
        lines = new ArrayList<>();
    }

    /**
     * Initializing constructor.
     *
     * @param name Name for the section.
     */
    public IniSection(String name) {
        this.name = name;
        lines = new ArrayList<>();
    }

    //=========================================================================

    /**
     * Find the line by the key.
     *
     * @param key Key to search for.
     * @return Found line or null.
     */
    @Nullable
    public IniLine find(@NotNull String key) {
        if (isNullOrEmpty(key)) {
            throw new IllegalArgumentException();
        }

        for (IniLine line : lines) {
            if (sameKey(line.key, key)) {
                return line;
            }
        }

        return null;
    }

    /**
     * Get the value for the specified key.
     * @param key Key to search for.
     * @return Found value or null.
     */
    @Nullable
    public String getValue(@NotNull String key) {
        return getValue(key, null);
    }

    /**
     * Get the value for the specified key.
     * @param key Key to search for.
     * @param defaultValue Default value to use if no lines found.
     * @return Found value or default value.
     */
    @Nullable
    public String getValue(@NotNull String key, @Nullable String defaultValue) {
        if (isNullOrEmpty(key)) {
            throw new IllegalArgumentException();
        }

        IniLine found = find(key);

        return found == null ? defaultValue : found.value;
    }

    /**
     * Remove the line with the specified key.
     * @param key
     */
    public void remove(@NotNull String key) {
        if (isNullOrEmpty(key)) {
            throw new IllegalArgumentException();
        }

        IniLine found = find(key);
        if (found != null) {
            lines.remove(found);
        }
    }

    /**
     * Set the line value by the key.
     *
     * @param key   Key to search for.
     * @param value Value to set (can be null).
     */
    public IniSection setValue(@NotNull String key, @Nullable String value) {
        if (isNullOrEmpty(key)) {
            throw new IllegalArgumentException();
        }

        IniLine line = find(key);
        if (line == null) {
            line = new IniLine(key, value);
            lines.add(line);
        } else {
            line.value = value;
        }

        return this;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (!isNullOrEmpty(name)) {
            result.append("[" + name + "]\n");
        }
        for (IniLine line: lines) {
            result.append(line);
            result.append("\n");
        }

        return result.toString();
    }
}
