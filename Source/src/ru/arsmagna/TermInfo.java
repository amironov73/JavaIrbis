package ru.arsmagna;

import ru.arsmagna.infrastructure.*;

import java.util.ArrayList;

/**
 * Информация о поисковом терме.
 */
public class TermInfo
{
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
     * Клонирование.
     * @return Копию.
     */
    public TermInfo clone()
    {
        TermInfo result = new TermInfo();
        result.count = count;
        result.text = text;

        return result;
    }

    /**
     * Разбор ответа сервера.
     * @return Прочитанные термы.
     */
    public static TermInfo[] parse
        (
            ServerResponse response
        )
    {
        ArrayList<TermInfo> result = new ArrayList<>();
        while (true)
        {
            String line = response.readUtf();
            if (Utility.isNullOrEmpty(line))
            {
                break;
            }
            String[] parts = line.split("#", 2);
            TermInfo item = new TermInfo();
            item.count = Integer.parseInt(parts[0]);
            if (parts.length > 1)
            {
                item.text = parts[1];
            }
            result.add(item);
        }

        return result.toArray(new TermInfo[0]);
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return count + "#" + text;
    }
}
