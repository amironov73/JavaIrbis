package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    class Chunk implements Cloneable, Comparable<Chunk> {

        public boolean havePrefix() {
            return !isNullOrEmpty(prefix);
        }

        public String prefix;
        public boolean haveValue;
        public long value;
        public long length;

        public boolean setup(@NotNull StringBuilder str,
                             @NotNull StringBuilder number) {
            boolean result = false;
            if (str.length() != 0) {
                result = true;
                prefix = str.toString();
            }
            if (number.length() != 0) {
                result = true;
                haveValue = true;
                length = number.length();
                value = Long.parseLong(number.toString());
            }

            return result;
        }

        public int compareTo(@NotNull Chunk other) {
            int result = Boolean.compare(havePrefix(), other.havePrefix());

            if (result == 0 && havePrefix()) {
                result = prefix.compareTo(other.prefix);
            }

            if (result == 0) {
                result = haveValue && other.haveValue
                        ? Long.signum(value - other.value)
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
            Chunk c1 = get(i);
            Chunk c2 = other.get(i);
            if (c1 != null && c2 != null) {
                int result = c1.compareTo(c2);
                if (result != 0) {
                    return result;
                }
            }
            else {
                if (c1 == null && c2 == null) {
                    return 0;
                }

                return c1 != null ? 1 : -1;
            }
        }

        return 0;
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

        if (!chunk.setup(str, number)) {
            _chunks.removeLast();
        }
    }

    public int size() {
        return _chunks.size();
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
}
