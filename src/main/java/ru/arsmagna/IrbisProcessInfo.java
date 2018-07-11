package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.arsmagna.infrastructure.ServerResponse;

import java.util.ArrayList;

import static ru.arsmagna.Utility.emptyToNull;

/**
 * Информация о запущенном на сервере процессе.
 */
public final class IrbisProcessInfo {

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
     *
     * @param response Ответ сервера.
     * @return Список процессов.
     */
    @NotNull
    public static IrbisProcessInfo[] parse (@NotNull ServerResponse response) {
        if (response == null) { throw new IllegalArgumentException(); }

        ArrayList<IrbisProcessInfo> result = new ArrayList<>();
        int processCount = response.readInt32();
        int linesPerProcess = response.readInt32();
        if (processCount == 0 || linesPerProcess == 0) {
            return new IrbisProcessInfo[0];
        }

        for (int i = 0; i < processCount; i++) {
            String[] lines = response.readAnsi(linesPerProcess + 1);
            if (lines == null) {
                break;
            }

            IrbisProcessInfo process = new IrbisProcessInfo();
            if (lines.length != 0) {
                process.number = emptyToNull(lines[0]);
            }
            if (lines.length > 1) {
                process.ipAddress = emptyToNull(lines[1]);
            }
            if (lines.length > 2) {
                process.name = emptyToNull(lines[2]);
            }
            if (lines.length > 3) {
                process.clientId = emptyToNull(lines[3]);
            }
            if (lines.length > 4) {
                process.workstation = emptyToNull(lines[4]);
            }
            if (lines.length > 5) {
                process.started = emptyToNull(lines[5]);
            }
            if (lines.length > 6) {
                process.lastCommand = emptyToNull(lines[6]);
            }
            if (lines.length > 7) {
                process.commandNumber = emptyToNull(lines[7]);
            }
            if (lines.length > 8) {
                process.processId = emptyToNull(lines[8]);
            }
            if (lines.length > 9) {
                process.state = emptyToNull(lines[9]);
            }
            result.add(process);
        }

        return result.toArray(new IrbisProcessInfo[0]);
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
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
                '}';
    }
}
