package ru.arsmagna.menus;

import org.jetbrains.annotations.*;

import ru.arsmagna.*;
import ru.arsmagna.infrastructure.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 * Файл меню.
 */
public final class MenuFile
{
    /**
     * Признак конца меню.
     */
    public static final String STOP_MARKER = "*****";

    /**
     * Разделители меню.
     */
    public static final String MENU_SEPARATORS = "[\\s\\-=:]";

    //=========================================================================

    /**
     * Имя файла
     */
    public String fileName;

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

    /**
     * Добавление строчек в меню.
     * @return Собственно меню.
     */
    @NotNull
    public MenuFile add
        (
            @NotNull String code,
            @Nullable String comment
        )
    {
        MenuEntry entry = new MenuEntry(code, comment);
        entries.add(entry);

        return this;
    }

    /**
     * Отрезание лишних символов в коде.
     * @return Очищенный код
     */
    public static String trimCode
        (
            @NotNull String code
        )
    {
        code = code.trim();
        String[] parts = code.split(MENU_SEPARATORS);
        if (parts.length != 0)
        {
            code = parts[0];
        }

        return code;
    }

    /**
     * Отыскивает запись, соответствующую данному коду.
     * @return Запись либо null.
     */
    @Nullable
    public MenuEntry getEntry
        (
            @NotNull String code
        )
    {
        for (MenuEntry entry: entries)
        {
            if (code.equalsIgnoreCase(entry.code))
            {
                return entry;
            }
        }

        code = code.trim();
        for (MenuEntry entry: entries)
        {
            if (code.equalsIgnoreCase(entry.code))
            {
                return entry;
            }
        }

        code = trimCode(code);
        for (MenuEntry entry: entries)
        {
            if (code.equalsIgnoreCase(entry.code))
            {
                return entry;
            }
        }

        return null;
    }

    /**
     * Выдает значение, соответствующее коду.
     * @param code Код.
     * @param defaultValue Значение по умолчанию.
     * @return Найденное значение либо null.
     */
    @Nullable
    public String getValue
        (
            @NotNull String code,
            @Nullable String defaultValue
        )
    {
        MenuEntry found = getEntry(code);
        String result = found == null ? null : found.comment;

        return result;
    }

    /**
     * Разбор потока.
     * @param scanner Поток.
     * @return Меню.
     */
    @NotNull
    public static MenuFile parse
        (
            @NotNull Scanner scanner
        )
    {
        MenuFile result = new MenuFile();
        while (true)
        {
            String code = scanner.nextLine();
            if (Utility.isNullOrEmpty(code))
            {
                break;
            }
            if (code.startsWith(STOP_MARKER))
            {
                break;
            }

            String comment = scanner.nextLine();
            result.add(code, comment);
        }

        return result;
    }

    /**
     * Загрузка меню из файла.
     * @param fileName Имя файла.
     * @return Меню.
     */
    @NotNull
    public static MenuFile parse
        (
            @NotNull String fileName
        )
        throws IOException
    {
        try(FileInputStream stream = new FileInputStream(fileName))
        {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name()))
            {
                MenuFile result = parse(scanner);
                result.fileName = fileName;

                return result;
            }
        }
    }

    /**
     * Парсинг ответа сервера.
     * @return Меню.
     */
    @NotNull
    public static MenuFile parse
        (
            @NotNull ServerResponse response
        )
        throws IOException
    {
        String text = response.readRemainingAnsiText();
        StringReader reader = new StringReader(text);
        Scanner scanner = new Scanner(reader);
        MenuFile result = parse(scanner);

        return result;
    }

    /**
     * Чтение меню с сервера.
     * @param connection Подключение.
     * @param specification Имя файла.
     * @return Меню.
     */
    @NotNull
    public static MenuFile read
        (
            @NotNull IrbisConnection connection,
            @NotNull FileSpecification specification
        )
        throws IOException
    {
        MenuFile result;
        String text = connection.readTextFile(specification);
        if (Utility.isNullOrEmpty(text))
        {
            result = new MenuFile();
        }
        else
        {
            StringReader reader = new StringReader(text);
            Scanner scanner = new Scanner(reader);
            result = parse(scanner);
        }

        result.fileName = specification.fileName;

        return result;
    }

    //=========================================================================

    @NotNull
    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        for (MenuEntry entry: entries)
        {
            result.append(entry.code);
            result.append(IrbisText.MSDOS_DELIMITER);
            result.append(entry.comment);
            result.append(IrbisText.MSDOS_DELIMITER);
        }
        result.append(STOP_MARKER);

        return result.toString();
    }
}
