package ru.arsmagna;

import ru.arsmagna.infrastructure.ServerResponse;

/**
 * Информация о версии ИРБИС-сервера.
 */
public class IrbisVersion
{
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

    public static IrbisVersion parse
        (
            ServerResponse response
        )
    {
        String[] lines = response.readRemainingAnsiLines();
        IrbisVersion result = new IrbisVersion();
        if (lines.length == 3)
        {
            result.version = lines[0];
            result.connectedClients = Integer.parseInt(lines[1]);
            result.maxClients = Integer.parseInt(lines[2]);
        }
        else
        {
            result.organization = lines[0];
            result.version = lines[1];
            result.connectedClients = Integer.parseInt(lines[2]);
            result.maxClients = Integer.parseInt(lines[3]);
        }

        return result;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "IrbisVersion{" +
            "organization='" + organization + '\'' +
            ", version='" + version + '\'' +
            ", maxClients=" + maxClients +
            ", connectedClients=" + connectedClients +
            '}';
    }
}
