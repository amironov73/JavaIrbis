package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;

import static ru.arsmagna.Utility.toVisible;

public final class OptLine {
    public String pattern;

    public String worksheet;

    //=========================================================================

    public static OptLine parse(@NotNull String text) {
        String[] parts = text.trim().split("\\s+");
        OptLine result = new OptLine();
        result.pattern = parts[0];
        result.worksheet = parts[1];

        return result;
    }

    //=========================================================================

    @Override
    public String toString() {
        return toVisible(pattern) + " " + toVisible(worksheet);
    }
}
