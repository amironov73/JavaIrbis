package ru.arsmagna;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Файл меню.
 */
public class MenuFile
{
    /**
     * Строки меню.
     */
    public Collection<MenuEntry> entries;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public MenuFile()
    {
        entries = new ArrayList<MenuEntry>();
    }

    //=========================================================================
}
