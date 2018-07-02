package ru.arsmagna;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Секция INI-файла.
 */
public class IniSection
{
    /**
     * Имя секции.
     */
    public String name;

    /**
     * Строки.
     */
    public Collection<IniLine> lines;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public IniSection()
    {
        lines = new ArrayList<IniLine>();
    }

    /**
     * Конструктор.
     * @param name Имя секции.
     */
    public IniSection
        (
            String name
        )
    {
        this.name = name;
        lines = new ArrayList<IniLine>();
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }
}
