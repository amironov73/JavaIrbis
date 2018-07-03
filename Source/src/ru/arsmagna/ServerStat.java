package ru.arsmagna;

import ru.arsmagna.infrastructure.ServerResponse;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Статистика работы ИРБИС-сервера.
 */
public class ServerStat
{
    /**
     * Подключенные клиенты.
     */
    public ClientInfo[] runningClients;

    /**
     * Число клиентов, подключенных в настоящее время.
     */
    public int clientCount;

    /**
     * Неизвестное поле.
     */
    public int unknown;

    /**
     * Общее количество команд, исполненных сервером
     * с момента его запуска.
     */
    public int totalCommandCount;

    //=========================================================================

    public static ServerStat parse
        (
            ServerResponse response
        )
    {
        ServerStat result = new ServerStat();
        result.totalCommandCount = response.readInt32();
        result.clientCount = response.readInt32();
        result.unknown = response.readInt32();

        ArrayList<ClientInfo> clients = new ArrayList<>();
        while (true)
        {
            String number = response.readAnsi();
            String ipAddress = response.readAnsi();
            if (Utility.isNullOrEmpty(number)
                || Utility.isNullOrEmpty(ipAddress))
            {
                break;
            }

            ClientInfo client = new ClientInfo();
            client.number = number;
            client.ipAddress = ipAddress;
            client.port = response.readAnsi();
            client.name = response.readAnsi();
            client.id = response.readAnsi();
            client.workstation = response.readAnsi();
            client.registered = response.readAnsi();
            client.acknowledged = response.readAnsi();
            client.lastCommand = response.readAnsi();
            clients.add(client);
        }
        result.runningClients = clients.toArray(new ClientInfo[0]);

        return result;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "ServerStat{" +
            "runningClients=" + Arrays.toString(runningClients) +
            ", clientCount=" + clientCount +
            ", unknown=" + unknown +
            ", totalCommandCount=" + totalCommandCount +
            '}';
    }
}
