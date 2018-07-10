package ru.arsmagna;

import org.jetbrains.annotations.Contract;

/**
 * Информация о клиенте, подключенном к серверу ИРБИС
 * (не обязательно о текущем).
 */
public final class ClientInfo {
    /**
     * Порядковый номер.
     */
    public String number;

    /**
     * Адрес клиента.
     */
    public String ipAddress;

    /**
     * Порт клиента.
     */
    public String port;

    /**
     * Логин.
     */
    public String name;

    /**
     * Идентификатор клиентской программы
     * (просто уникальное число).
     */
    public String id;

    /**
     * Клиентский АРМ.
     */
    public String workstation;

    /**
     * Время подключения к серверу.
     */
    public String registered;

    /**
     * Последнее подтверждение, посланное серверу.
     */
    public String acknowledged;

    /**
     * Последняя команда, посланная серверу.
     */
    public String lastCommand;

    /**
     * Номер последней команды.
     */
    public String commandNumber;

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return "ClientInfo{" +
                "number='" + number + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", port='" + port + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", workstation='" + workstation + '\'' +
                ", registered='" + registered + '\'' +
                ", acknowledged='" + acknowledged + '\'' +
                ", lastCommand='" + lastCommand + '\'' +
                ", commandNumber='" + commandNumber + '\'' +
                '}';
    }
}
