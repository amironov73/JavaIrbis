// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.worksheet;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Страница (вкладка) в рабочем листе.
 */
@SuppressWarnings("WeakerAccess")
public class WorksheetPage {

    /**
     * Имя страницы.
     */
    public String name;

    /**
     * Элементы страницы.
     */
    public final Collection<WorksheetItem> items;

    /**
     * Произвольные пользовательские данные.
     */
    @SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
    public Object userData;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public WorksheetPage() {
        items = new ArrayList<>();
    }
}
