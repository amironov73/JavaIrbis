package ru.arsmagna.worksheet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Рабочий лист.
 */
public class WsFile
{
    /**
     * Имя рабочего листа.
     */
    public String name;

    /**
     * Страницы рабочего листа.
     */
    public Collection<WorksheetPage> pages;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public WsFile()
    {
        pages = new ArrayList<>();
    }
}
