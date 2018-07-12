package ru.arsmagna.worksheet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Рабочий лист.
 */
public class WsFile {

    /**
     * Имя рабочего листа.
     */
    public String name;

    /**
     * Страницы рабочего листа.
     */
    public final Collection<WorksheetPage> pages;

    /**
     * Произвольные пользовательские данные.
     */
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public WsFile() {
        pages = new ArrayList<>();
    }
}
