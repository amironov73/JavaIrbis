package ru.arsmagna.worksheet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Вложенный рабочий лист (для редактирования поля).
 */
public class WssFile
{
    /**
     * Имя рабочего листа.
     */
    public String name;

    /**
     * Элементы рабочего листа.
     */
    public Collection<WorksheetItem> items;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Рабочий лист по умолчанию.
     */
    public WssFile()
    {
        items = new ArrayList<>();
    }
}
