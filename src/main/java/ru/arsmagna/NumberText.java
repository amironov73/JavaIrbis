package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * String containing numbers separated by non-numeric fragments.
 */
@SuppressWarnings("WeakerAccess")
public class NumberText implements Cloneable, Comparable<NumberText> {

    @SuppressWarnings("WeakerAccess")
    static class Chunk implements Cloneable, Comparable<Chunk> {

        public String prefix;

        public long value;

        public boolean haveValue;

        public int length;

        public boolean havePrefix() {
            return !isNullOrEmpty(prefix);
        }

        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Chunk clone() {
            Chunk result = new Chunk();
            result.prefix = prefix;
            result.value = value;
            result.length = length;

            return result;
        }

        public int compareTo(@NotNull Chunk other) {
            int result = Utility.safeCompare(this.prefix, other.prefix);
            if (result == 0) {
                result = this.haveValue && other.haveValue
                        ? Long.compare(this.value, other.value)
                        : Boolean.compare(this.haveValue, other.haveValue);
            }

            return result;
        }

        public boolean setUp(@NotNull StringBuilder prefix, @NotNull StringBuilder value) {
            boolean result = false;
            if (prefix.length() != 0) {
                this.prefix = prefix.toString();
                result = true;
            }
            if (value.length() != 0) {
                this.haveValue = true;
                this.value = Long.parseLong(value.toString());
                result = true;
            }

            return result;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            if (havePrefix()) {
                result.append(prefix);
            }
            if (haveValue) {
                // TODO handle length
                result.append(value);
            }

            return result.toString();
        }
    }

    //=====================================================================

    private final LinkedList<Chunk> _chunks = new LinkedList<>();

    @Nullable
    @Contract(pure = true)
    private Chunk get(int index) {
        return _chunks.size() <= index ? null : _chunks.get(index);
    }

    @Nullable
    @Contract(pure = true)
    private Chunk last() {
        return _chunks.size() == 0 ? null : _chunks.getLast();
    }

    //=====================================================================

    public NumberText append(@Nullable String prefix, long value, int length) {
        Chunk chunk = new Chunk();
        chunk.prefix = prefix;
        chunk.value = value;
        chunk.haveValue = true;
        chunk.length = length;
        _chunks.add(chunk);

        return this;
    }

    public NumberText append(@Nullable String prefix) {
        Chunk chunk = new Chunk();
        chunk.prefix = prefix;
        _chunks.add(chunk);

        return this;
    }

    public NumberText append(int value) {
        Chunk chunk = new Chunk();
        chunk.value = value;
        chunk.haveValue = true;
        _chunks.add(chunk);

        return this;
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public NumberText clone() {
        NumberText result = new NumberText();
        for (Chunk chunk : _chunks) {
            result._chunks.add(chunk.clone());
        }

        return result;
    }

    public int compareTo(@NotNull NumberText other) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Chunk left = this.get(i);
            Chunk right = other.get(i);
            if (left != null && right != null) {
                int result = left.compareTo(right);
                if (result != 0) {
                    return result;
                }
            } else {
                if (left == null && right == null) {
                    return 0;
                }
                return (left != null) ? 1 : -1;
            }
        }

        return 0;
    }

    public String getPrefix(int index) {
        Chunk chunk = get(index);
        return chunk == null ? null : chunk.prefix;
    }

    public long getValue(int index) {
        Chunk chunk = get(index);
        return chunk == null ? 0 : chunk.value;
    }

    public boolean havePrefix(int index) {
        Chunk chunk = get(index);
        return chunk != null && chunk.havePrefix();
    }

    public boolean haveValue(int index) {
        Chunk chunk = get(index);
        return chunk != null && chunk.haveValue;
    }

    public NumberText increment(int index, int delta) {
        Chunk chunk = get(index);
        if (chunk != null && chunk.haveValue) {
            chunk.value += delta;
        }

        return this;
    }

    public NumberText increment(int delta) {
        Chunk chunk = last();
        if (chunk != null && chunk.haveValue) {
            chunk.value += delta;
        }

        return this;
    }

    public NumberText increment() {
        return increment(1);
    }

    public boolean isEmpty() {
        return _chunks.isEmpty();
    }

    public int length() {
        return _chunks.size();
    }

    public static NumberText parse(@Nullable String text) {
        NumberText result = new NumberText();
        if (Utility.isNullOrEmpty(text)) {
            return result;
        }

        Chunk chunk = new Chunk();
        result._chunks.add(chunk);
        StringBuilder prefix = new StringBuilder();
        StringBuilder value = new StringBuilder();
        boolean inPrefix = true;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (inPrefix) {
                if (Character.isDigit(c)) {
                    value.append(c);
                    inPrefix = false;
                } else {
                    prefix.append(c);
                }
            } else {
                if (Character.isDigit(c)) {
                    value.append(c);
                } else {
                    chunk.setUp(prefix, value);
                    chunk = new Chunk();
                    result._chunks.add(chunk);
                    prefix.setLength(0);
                    prefix.append(c);
                    value.setLength(0);
                    inPrefix = true;
                }
            }
        }
        if (!chunk.setUp(prefix, value)) {
            result._chunks.removeLast();
        }

        return result;
    }

    public NumberText removeValue(int index) {
        Chunk chunk = get(index);
        if (chunk != null) {
            chunk.haveValue = false;
        }

        return this;
    }

    public NumberText setPrefix(int index, @Nullable String prefix) {
        Chunk chunk = get(index);
        if (chunk != null) {
            chunk.prefix = prefix;
        }

        return this;
    }

    public NumberText setValue(int index, long value) {
        Chunk chunk = get(index);
        if (chunk != null) {
            chunk.value = value;
            chunk.haveValue = true;
        }

        return this;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Chunk chunk : _chunks) {
            result.append(chunk);
        }

        return result.toString();
    }
}
