package ru.arsmagna;

import ru.arsmagna.infrastructure.ServerResponse;

import java.util.ArrayList;

/**
 * Информация о запущенном на сервере процессе.
 */
public class IrbisProcessInfo
{
    /**
     * Просто порядковый номер в списке.
     */
    public String number;

    /**
     * С каким клиентом взаимодействует.
     */
    public String ipAddress;

    /**
     * Логин оператора.
     */
    public String name;

    /**
     * Идентификатор клиента.
     */
    public String clientId;

    /**
     * Тип АРМ.
     */
    public String workstation;

    /**
     * Время запуска.
     */
    public String started;

    /**
     * Последняя выполненная (или выполняемая) команда.
     */
    public String lastCommand;

    /**
     * Порядковый номер последней команды.
     */
    public String commandNumber;

    /**
     * Идентификатор процесса.
     */
    public String processId;

    /**
     * Состояние.
     */
    public String state;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Разбор ответа сервера.
     * @param response Ответ сервера.
     * @return Список процессов.
     */
    public static IrbisProcessInfo[] parse
        (
            ServerResponse response
        )
    {
        ArrayList<IrbisProcessInfo> result = new ArrayList<>();
        while (true)
        {
            String[] lines = response.readAnsi(10);
            if (lines == null)
            {
                break;
            }

            IrbisProcessInfo process = new IrbisProcessInfo();
            process.processId = lines[0];
            process.state = lines[1];
            process.number = lines[2];
            process.ipAddress = lines[3];
            process.name = lines[4];
            process.clientId = lines[5];
            process.workstation = lines[6];
            process.started = lines[7];
            process.lastCommand = lines[8];
            process.commandNumber = lines[9];
            result.add(process);
        }

        return result.toArray(new IrbisProcessInfo[0]);
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "IrbisProcessInfo{" +
            "number='" + number + '\'' +
            ", ipAddress='" + ipAddress + '\'' +
            ", name='" + name + '\'' +
            ", clientId='" + clientId + '\'' +
            ", workstation='" + workstation + '\'' +
            ", started='" + started + '\'' +
            ", lastCommand='" + lastCommand + '\'' +
            ", commandNumber='" + commandNumber + '\'' +
            ", processId='" + processId + '\'' +
            ", state='" + state + '\'' +
            ", userData=" + userData +
            '}';
    }
}
