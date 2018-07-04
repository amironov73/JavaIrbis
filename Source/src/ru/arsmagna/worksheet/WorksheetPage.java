package ru.arsmagna.worksheet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Страница (вкладка) в рабочем листе.
 */
public class WorksheetPage
{
    /**
     * Имя страницы.
     */
    public String name;

    /**
     * Элементы страницы.
     */
    public Collection<WorksheetItem> items;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public WorksheetPage()
    {
        items = new ArrayList<>();
    }
}
