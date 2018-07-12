package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.IrbisConnection;
import ru.arsmagna.IrbisEncoding;
import ru.arsmagna.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Клиентский запрос.
 */
@SuppressWarnings({"WeakerAccess", "UnnecessaryLocalVariable"})
public final class ClientQuery {

    private ByteArrayOutputStream stream;

    //=========================================================================

    /**
     * Конструктор.
     *
     * @param connection  Подключение.
     * @param commandCode Код команды.
     */
    public ClientQuery (@NotNull IrbisConnection connection, @NotNull String commandCode)
            throws IOException {
        stream = new ByteArrayOutputStream();
        addAnsi(commandCode);
        addAnsi(Character.valueOf(connection.workstation).toString());
        addAnsi(commandCode);
        add(connection.clientId);
        connection.queryId++;
        add(connection.queryId);
        addAnsi(connection.password);
        addAnsi(connection.username);
        addLineFeed();
        addLineFeed();
        addLineFeed();
    }

    //=========================================================================

    public final void add (boolean value) throws IOException {
        String text = value ? "1" : "0";
        addAnsi(text);
    }

    public final void add (int value) throws IOException {
        String text = Integer.toString(value);
        addAnsi(text);
    }

    public final void addAnsi (@Nullable String text) throws IOException {
        addAnsiNoLF(text);
        addLineFeed();
    }

    public final void addAnsiNoLF (@Nullable String text) throws IOException {
        if (!isNullOrEmpty(text)) {
            byte[] bytes = text.getBytes(IrbisEncoding.ansi());
            stream.write(bytes);
        }
    }

    public final void addLineFeed() throws IOException {
        stream.write(Utility.LF);
    }

    public final void addUtf (@Nullable String text) throws IOException {
        if (!isNullOrEmpty(text)) {
            byte[] bytes = text.getBytes(IrbisEncoding.utf());
            stream.write(bytes);
        }
        addLineFeed();
    }

    @NotNull
    public final byte[][] encode() {
        byte[] buffer = stream.toByteArray();
        byte[] prefix = IrbisEncoding.ansi().encode(buffer.length + "\n").array();
        byte[][] result = { prefix, buffer };

        return result;
    }
}
