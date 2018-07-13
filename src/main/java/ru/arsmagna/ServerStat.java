package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import ru.arsmagna.infrastructure.ServerResponse;

import java.util.ArrayList;
import java.util.Arrays;

import static ru.arsmagna.Utility.emptyToNull;

/**
 * Статистика работы ИРБИС-сервера.
 */
@SuppressWarnings("WeakerAccess")
public final class ServerStat {
    /**
     * Подключенные клиенты.
     */
    public ClientInfo[] runningClients;

    /**
     * Число клиентов, подключенных в настоящее время.
     */
    public int clientCount;

    /**
     * Общее количество команд, исполненных сервером
     * с момента его запуска.
     */
    public int totalCommandCount;

    //=========================================================================

    /**
     * Разбор ответа сервера.
     *
     * @param response Ответ сервера.
     * @return Статистика.
     */
    public static ServerStat parse (@NotNull ServerResponse response) {
        if (response == null) { throw new IllegalArgumentException(); }

        ServerStat result = new ServerStat();
        result.totalCommandCount = response.readInt32();
        result.clientCount = response.readInt32();
        int linesPerClient = response.readInt32();
        if (linesPerClient == 0) {
            return result;
        }

        ArrayList<ClientInfo> clients = new ArrayList<>();
        for (int i = 0; i < result.clientCount; i++) {
            String[] lines = response.readAnsi(linesPerClient + 1);
            if (lines == null) {
                break;
            }

            ClientInfo client = new ClientInfo();
            if (lines.length != 0) {
                client.number = emptyToNull(lines[0]);
            }
            if (lines.length > 1) {
                client.ipAddress = emptyToNull(lines[1]);
            }
            if (lines.length > 2) {
                client.port = emptyToNull(lines[2]);
            }
            if (lines.length > 3) {
                client.name = emptyToNull(lines[3]);
            }
            if (lines.length > 4) {
                client.id = emptyToNull(lines[4]);
            }
            if (lines.length > 5) {
                client.workstation = emptyToNull(lines[5]);
            }
            if (lines.length > 6) {
                client.registered = emptyToNull(lines[6]);
            }
            if (lines.length > 7) {
                client.acknowledged = emptyToNull(lines[7]);
            }
            if (lines.length > 8) {
                client.lastCommand = emptyToNull(lines[8]);
            }
            if (lines.length > 9) {
                client.commandNumber = emptyToNull(lines[9]);
            }
            clients.add(client);
        }
        result.runningClients = clients.toArray(new ClientInfo[0]);

        return result;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return "ServerStat{" +
                "runningClients=" + Arrays.toString(runningClients) +
                ", clientCount=" + clientCount +
                ", totalCommandCount=" + totalCommandCount +
                '}';
    }
}
