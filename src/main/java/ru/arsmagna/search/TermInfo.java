package ru.arsmagna.search;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import ru.arsmagna.infrastructure.ServerResponse;

import java.util.ArrayList;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Информация о поисковом терме.
 */
@SuppressWarnings("WeakerAccess")
public final class TermInfo implements Cloneable {

    /**
     * Количество ссылок.
     */
    public int count;

    /**
     * Поисковый термин.
     */
    public String text;

    //=========================================================================

    /**
     * Разбор ответа сервера.
     *
     * @param response Ответ сервера.
     * @return Прочитанные термы.
     */
    @NotNull
    public static TermInfo[] parse (@NotNull ServerResponse response) {
        if (response == null) { throw new IllegalArgumentException(); }

        ArrayList<TermInfo> result = new ArrayList<>();
        while (true) {
            String line = response.readUtf();
            if (isNullOrEmpty(line)) {
                break;
            }
            String[] parts = line.split("#", 2);
            TermInfo item = new TermInfo();
            item.count = Integer.parseInt(parts[0]);
            if (parts.length > 1) {
                item.text = parts[1];
            }
            result.add(item);
        }

        return result.toArray(new TermInfo[0]);
    }

    /**
     * Клонирование.
     *
     * @return Копию.
     */
    public TermInfo clone() {
        TermInfo result = new TermInfo();
        result.count = count;
        result.text = text;

        return result;
    }

    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        return count + "#" + text;
    }
}
