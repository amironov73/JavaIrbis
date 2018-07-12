package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.IrbisEncoding;
import ru.arsmagna.IrbisException;
import ru.arsmagna.Utility;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Ответ сервера.
 */
@SuppressWarnings({"WeakerAccess", "unused", "UnnecessaryLocalVariable"})
public final class ServerResponse implements AutoCloseable {

    /**
     * Код команды.
     */
    public String command;

    /**
     * Идентификатор клиента.
     */
    public int clientId;

    /**
     * Номер запроса.
     */
    public int queryId;

    /**
     * Код возврата (формируется не всегда!).
     */
    public int returnCode;

    //=========================================================================

    //private ByteArrayInputStream stream;
    private Socket socket;
    private InputStream stream;

    private int savedSymbol;

    //=========================================================================

    /**
     * Конструктор.
     *
     * @param socket Сокет
     * @throws IOException Ошибка ввода-вывода.
     */
    public ServerResponse(@NotNull Socket socket) throws IOException {
        this.socket = socket;
        stream = new BufferedInputStream(socket.getInputStream());
        savedSymbol = -1;

        command = readAnsi();
        clientId = readInt32();
        queryId = readInt32();
        for (int i = 0; i < 7; i++) {
            readAnsi();
        }
    }

    //=========================================================================

    @Override
    public void close() throws IOException {
        socket.close();
    }

    /**
     * Проверка кода возврата.
     *
     * @param allowed Разрешенные коды.
     * @throws IrbisException Bad return code.
     */
    public void checkReturnCode(int[] allowed) throws IrbisException {
        if (getReturnCode() < 0) {
            if (Utility.find(allowed, returnCode) < 0) {
                throw new IrbisException(returnCode);
            }
        }
    }

    public void checkReturnCode() throws IrbisException {
        if (getReturnCode() < 0) {
            throw new IrbisException(returnCode);
        }
    }

    public byte[] getLine() {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        try {
            while (true) {
                int one;

                if (savedSymbol >= 0) {
                    one = savedSymbol;
                    savedSymbol = -1;
                } else {
                    one = stream.read();
                }

                if (one < 0) {
                    break;
                }
                if (one == 0x0D) {
                    one = stream.read();
                    if (one == 0x0A) {
                        break;
                    } else {
                        savedSymbol = one;
                    }
                } else {
                    result.write(one);
                }
            }
        } catch (IOException ex) {
            return new byte[0];
        }

        return result.toByteArray();
    }

    public int getReturnCode() {
        returnCode = readInt32();

        return returnCode;
    }

    public void nop() {
    }

    public String readAnsi() {
        byte[] line = getLine();

        return new String(line, IrbisEncoding.ansi());
    }

    /**
     * Считывание не менее указанного количества строк.
     *
     * @param count How many strings to read.
     * @return Read string or null.
     */
    @Nullable
    public String[] readAnsi(int count) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String line = readAnsi();
            if (isNullOrEmpty(line)) {
                return null;
            }
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public int readInt32() {
        String text = readAnsi();
        int result = Integer.parseInt(text);

        return result;
    }

    @NotNull
    public final String[] readRemainingAnsiLines() {
        ArrayList<String> result = new ArrayList<>();
        while (true) {
            String line = readAnsi();
            if (isNullOrEmpty(line)){
                break;
            }
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    @NotNull
    public final String[] readRemainingUtfLines() {
        ArrayList<String> result = new ArrayList<>();
        while (true) {
            String line = readUtf();
            if (isNullOrEmpty(line)){
                break;
            }
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    public final String readRemainingAnsiText() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utility.copyTo(stream, buffer);
        byte[] bytes = buffer.toByteArray();
        String result = new String(bytes, IrbisEncoding.ansi());

        return result;
    }

    public final String readRemainingUtfText() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        Utility.copyTo(stream, buffer);
        byte[] bytes = buffer.toByteArray();
        String result = new String(bytes, IrbisEncoding.utf());

        return result;
    }

    public final String readUtf() {
        byte[] line = getLine();
        String result = new String(line, IrbisEncoding.utf());

        return result;
    }

    /**
     * Считывание не менее указанного количества строк.
     *
     * @param count How many lines to read.
     * @return Read lines or null.
     */
    @Nullable
    public final String[] readUtf(int count) {
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            String line = readUtf();
            if (isNullOrEmpty(line)) {
                return null;
            }
            result.add(line);
        }

        return result.toArray(new String[0]);
    }

    //=========================================================================
}
