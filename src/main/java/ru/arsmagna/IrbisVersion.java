package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.arsmagna.infrastructure.ServerResponse;

/**
 * Информация о версии ИРБИС-сервера.
 */
public final class IrbisVersion {

    /**
     * На кого приобретен.
     */
    public String organization;

    /**
     * Собственно версия. Например, 64.2008.1.
     */
    public String version;

    /**
     * Максимальное количество подключений.
     */
    public int maxClients;

    /**
     * Текущее количество подключений.
     */
    public int connectedClients;

    //=========================================================================

    /**
     * Разбор ответа сервера.
     *
     * @param response Ответ сервера.
     * @return Версия сервера.
     */
    @NotNull
    public static IrbisVersion parse (@NotNull ServerResponse response) {
        if (response == null) { throw new IllegalArgumentException(); }

        String[] lines = response.readRemainingAnsiLines();
        IrbisVersion result = new IrbisVersion();
        if (lines.length == 3) {
            result.version = lines[0];
            result.connectedClients = Integer.parseInt(lines[1]);
            result.maxClients = Integer.parseInt(lines[2]);
        } else {
            result.organization = lines[0];
            result.version = lines[1];
            result.connectedClients = Integer.parseInt(lines[2]);
            result.maxClients = Integer.parseInt(lines[3]);
        }

        return result;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return "IrbisVersion{" +
                "organization='" + organization + '\'' +
                ", version='" + version + '\'' +
                ", maxClients=" + maxClients +
                ", connectedClients=" + connectedClients +
                '}';
    }
}
