package ru.arsmagna.worksheet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Вложенный рабочий лист (для редактирования поля).
 */
public class WssFile {
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
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    /**
     * Рабочий лист по умолчанию.
     */
    public WssFile() {
        items = new ArrayList<>();
    }
}
