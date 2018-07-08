package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import ru.arsmagna.infrastructure.*;

import static ru.arsmagna.Utility.*;

import java.util.ArrayList;

/**
 * Информация о зарегистрированном пользователе системы
 * (по данным client_m.mnu).
 */
public final class UserInfo
{
    /**
     * Номер по порядку в списке.
     */
    public String number;

    /**
     * Логин.
     */
    public String name;

    /**
     * Пароль.
     */
    public String password;

    /**
     * Доступность АРМ Каталогизатор.
     */
    public String cataloger;

    /**
     * АРМ Читатель.
     */
    public String reader;

    /**
     * АРМ Книговыдача.
     */
    public String circulation;

    /**
     * АРМ Комплектатор.
     */
    public String acquisitions;

    /**
     * АРМ Книгообеспеченность.
     */
    public String provision;

    /**
     * АРМ Администратор.
     */
    public String administrator;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Разбор ответа сервера.
     *
     * @param response Ответ сервера.
     * @return Перечень пользователей.
     */
    @NotNull
    public static UserInfo[] parse
        (
            @NotNull ServerResponse response
        )
    {
        ArrayList<UserInfo> result = new ArrayList<>();

        int userCount = response.readInt32();
        int linesPerUser = response.readInt32();
        if (userCount == 0 || linesPerUser == 0)
        {
            return new UserInfo[0];
        }

        for (int i = 0; i < userCount; i++)
        {
            String[] lines = response.readAnsi(linesPerUser + 1);
            if (lines.length == 0)
            {
                break;
            }

            UserInfo user = new UserInfo();
            if (lines.length != 0)
            {
                user.number = emptyToNull(lines[0]);
            }
            if (lines.length > 1)
            {
                user.name = emptyToNull(lines[1]);
            }
            if (lines.length > 2)
            {
                user.password = emptyToNull(lines[2]);
            }
            if (lines.length > 3)
            {
                user.cataloger = emptyToNull(lines[3]);
            }
            if (lines.length > 4)
            {
                user.reader = emptyToNull(lines[4]);
            }
            if (lines.length > 5)
            {
                user.circulation = emptyToNull(lines[5]);
            }
            if (lines.length > 6)
            {
                user.acquisitions = emptyToNull(lines[6]);
            }
            if (lines.length > 7)
            {
                user.provision = emptyToNull(lines[7]);
            }
            if (lines.length > 8)
            {
                user.administrator = emptyToNull(lines[8]);
            }
            result.add(user);
        }

        return result.toArray(new UserInfo[0]);
    }

    //=========================================================================

    @NotNull
    @Override
    @Contract(pure = true)
    public String toString()
    {
        return "UserInfo{" +
            "number='" + number + '\'' +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", cataloger='" + cataloger + '\'' +
            ", reader='" + reader + '\'' +
            ", circulation='" + circulation + '\'' +
            ", acquisitions='" + acquisitions + '\'' +
            ", provision='" + provision + '\'' +
            ", administrator='" + administrator + '\'' +
            ", userData=" + userData +
            '}';
    }
}
