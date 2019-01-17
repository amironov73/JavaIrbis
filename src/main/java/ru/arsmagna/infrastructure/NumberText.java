// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.arsmagna.Utility;

import java.io.StringReader;
import java.util.LinkedList;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * String containing numbers separated
 * by non-numeric fragments.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class NumberText implements Cloneable, Comparable<NumberText> {

    /**
     * Fragment: a prefix plus a number.
     */
    private static class Chunk implements Cloneable, Comparable<Chunk> {

        public String prefix;
        public boolean haveValue;
        public long value;
        public long length;

        public boolean havePrefix() { return !isNullOrEmpty(prefix); }

        public void setup(@NotNull StringBuilder str,
                             @NotNull StringBuilder number) {
            if (str.length() != 0) {
                prefix = str.toString();
            }

            if (number.length() != 0) {
                haveValue = true;
                length = number.length();
                value = Long.parseLong(number.toString());
            }
        }

        public int compareTo(@NotNull Chunk other) {
            int result = Utility.safeCompare(prefix, other.prefix);

            if (result == 0) {
                result = haveValue && other.haveValue
                        ? Long.compare(value, other.value)
                        : Boolean.compare(haveValue, other.haveValue);
            }

            return result;
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Chunk clone() {
            Chunk result = new Chunk();
            result.prefix = prefix;
            result.haveValue = haveValue;
            result.value = value;
            result.length = length;

            return result;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            if (prefix != null) {
                result.append(prefix);
            }

            if (haveValue) {
                if (length > 0) {
                    String format = String.format("%%0%dd", length);
                    String text = String.format(format, value);
                    result.append(text);
                }
                else {
                    result.append(value);
                }
            }

            return result.toString();
        }

        @Override
        public int hashCode() {
            int result = prefix == null ? 0: prefix.hashCode();
            if (haveValue) {
                result = result * 137 + Long.hashCode(value);
            }

            return result;
        }
    }

    //=========================================================================

    public NumberText() {
    }

    public NumberText(@Nullable String text) {
        parse(text);
    }

    //=========================================================================

    private final LinkedList<Chunk> _chunks = new LinkedList<>();

    @Nullable
    private Chunk get(int index) {
        if (index < 0 || index >= _chunks.size()) {
            return null;
        }

        return _chunks.get(index);
    }

    //=========================================================================

    public NumberText append(String prefix, long value, int length) {
        Chunk chunk = new Chunk();
        chunk.prefix = prefix;
        chunk.haveValue = true;
        chunk.value = value;
        chunk.length = length;
        _chunks.addLast(chunk);

        return this;
    }

    public NumberText append(String prefix) {
        Chunk chunk = new Chunk();
        chunk.prefix = prefix;
        _chunks.addLast(chunk);

        return this;
    }

    public NumberText append(long value) {
        Chunk chunk = new Chunk();
        chunk.haveValue = true;
        chunk.value = value;
        _chunks.addLast(chunk);

        return this;
    }

    public int compareTo(@NotNull NumberText other) {
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Chunk left = get(i);
            Chunk right = other.get(i);

            if (left != null && right != null) {
                int result = left.compareTo(right);
                if (result != 0) {
                    return result;
                }
            }
            else {
                if (left == null && right == null) {
                    return 0;
                }

                return left != null ? 1 : -1;
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
        return increment(lastIndex(), delta);
    }

    public NumberText increment() {
        return increment(1);
    }

    public boolean isEmpty() {
        return _chunks.isEmpty();
    }

    public int lastIndex() {
        return size() - 1;
    }

    public void parse(@Nullable String text) {
        _chunks.clear();
        if (isNullOrEmpty(text)) {
            return;
        }

        StringReader reader = new StringReader(text);
        Chunk chunk = new Chunk();
        _chunks.addLast(chunk);
        boolean textPart = true;
        StringBuilder str = new StringBuilder();
        StringBuilder number = new StringBuilder();
        int code = -1;
        while (true) {
            try {
                code = reader.read();
            }
            catch (Exception exception) {
                // Do nothing
            }
            if (code < 0) {
                break;
            }
            char c = (char)code;
            if (textPart) {
                if (Character.isDigit(c)) {
                    number.append(c);
                    textPart = false;
                }
                else {
                    str.append(c);
                }
            }
            else {
                if (Character.isDigit(c)) {
                    number.append(c);
                }
                else {
                    chunk.setup(str, number);
                    chunk = new Chunk();
                    _chunks.addLast(chunk);
                    str = new StringBuilder();
                    str.append(c);
                    number = new StringBuilder();
                    textPart = true;
                }
            }
        }

        chunk.setup(str, number);
    }

    public int size() {
        return _chunks.size();
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
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public NumberText clone() {
        NumberText result = new NumberText();
        for (Chunk chunk: _chunks) {
            result._chunks.addLast(chunk.clone());
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Chunk chunk: _chunks) {
            result.append(chunk.toString());
        }

        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NumberText that = (NumberText) o;

        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (Chunk chunk: _chunks) {
            result = result * 137 + result;
        }

        return result;
    }
}
