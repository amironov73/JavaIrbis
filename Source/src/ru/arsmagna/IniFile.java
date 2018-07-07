package ru.arsmagna;

import java.util.ArrayList;
import java.util.Collection;

/**
 * INI-файл.
 */
public final class IniFile
{
    /**
     * Секции.
     */
    public Collection<IniSection> sections;

    //=========================================================================

    /**
     * Конструктор
     */
    public IniFile()
    {
        sections = new ArrayList<IniSection>();
    }
}
