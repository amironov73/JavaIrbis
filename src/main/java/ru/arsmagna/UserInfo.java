// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.infrastructure.ServerResponse;

import java.util.ArrayList;

import static ru.arsmagna.Utility.emptyToNull;
import static ru.arsmagna.Utility.sameString;

/**
 * Информация о зарегистрированном пользователе системы
 * (по данным client_m.mnu).
 */
@SuppressWarnings("WeakerAccess")
public final class UserInfo {

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
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    private static String formatPair(@NotNull String prefix,
                                     @Nullable String value,
                                     @NotNull String defaultValue) {
        if (sameString(value, defaultValue)) {
            return "";
        }

        return prefix + "=" + value + ";";
    }

    //=========================================================================

    /**
     * Encode the reader.
     *
     * @return Encoded text.
     */
    public String encode() {
        return name + "\r\n"
                + password + "\r\n"
                + formatPair("C", cataloger,     "irbisc.ini")
                + formatPair("R", reader,        "irbisr.ini")
                + formatPair("B", circulation,   "irbisb.ini")
                + formatPair("M", acquisitions,  "irbism.ini")
                + formatPair("K", provision,     "irbisk.ini")
                + formatPair("A", administrator, "irbisa.ini");
    }

    /**
     * Разбор ответа сервера.
     *
     * @param response Ответ сервера.
     * @return Перечень пользователей.
     */
    @NotNull
    @SuppressWarnings("ConstantConditions")
    public static UserInfo[] parse (@NotNull ServerResponse response) {
        if (response == null) { throw new IllegalArgumentException(); }

        ArrayList<UserInfo> result = new ArrayList<>();

        int userCount = response.readInt32();
        int linesPerUser = response.readInt32();
        if (userCount == 0 || linesPerUser == 0) {
            return new UserInfo[0];
        }

        for (int i = 0; i < userCount; i++) {
            String[] lines = response.readAnsi(linesPerUser + 1);
            if (lines == null) {
                break;
            }

            if (lines.length == 0) {
                break;
            }

            UserInfo user = new UserInfo();
            user.number = emptyToNull(lines[0]);

            if (lines.length > 1) {
                user.name = emptyToNull(lines[1]);
            }
            if (lines.length > 2) {
                user.password = emptyToNull(lines[2]);
            }
            if (lines.length > 3) {
                user.cataloger = emptyToNull(lines[3]);
            }
            if (lines.length > 4) {
                user.reader = emptyToNull(lines[4]);
            }
            if (lines.length > 5) {
                user.circulation = emptyToNull(lines[5]);
            }
            if (lines.length > 6) {
                user.acquisitions = emptyToNull(lines[6]);
            }
            if (lines.length > 7) {
                user.provision = emptyToNull(lines[7]);
            }
            if (lines.length > 8) {
                user.administrator = emptyToNull(lines[8]);
            }
            result.add(user);
        }

        return result.toArray(new UserInfo[0]);
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
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
                '}';
    }
}
