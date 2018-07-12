package ru.arsmagna.menus;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.*;
import ru.arsmagna.infrastructure.ServerResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Файл меню.
 */
public final class MenuFile {

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
    public final Collection<MenuEntry> entries;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public MenuFile() {
        entries = new ArrayList<>();
    }

    //=========================================================================

    /**
     * Отрезание лишних символов в коде.
     *
     * @return Очищенный код
     */
    public static String trimCode (@NotNull String code) {
        if (code == null) { throw new IllegalArgumentException(); }

        code = code.trim();
        String[] parts = code.split(MENU_SEPARATORS);
        if (parts.length != 0) { code = parts[0]; }

        return code;
    }

    /**
     * Разбор потока.
     *
     * @param scanner Поток.
     * @return Меню.
     */
    public static MenuFile parse (@NotNull Scanner scanner) {
        if (scanner == null) { throw new IllegalArgumentException(); }

        MenuFile result = new MenuFile();
        while (scanner.hasNext()) {
            String code = scanner.nextLine();
            if (isNullOrEmpty(code) || !scanner.hasNext()) {
                break;
            }
            if (code.startsWith(STOP_MARKER)) {
                break;
            }

            String comment = scanner.nextLine();
            result.add(code, comment);
        }

        return result;
    }

    /**
     * Load the menu from the file.
     *
     * @param file File to read.
     * @return Loaded menu.
     */
    public static MenuFile parse (@NotNull File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException();
        }

        try (FileInputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                MenuFile result = parse(scanner);
                result.fileName = file.getAbsolutePath();

                return result;
            }
        }
    }

    /**
     * Парсинг ответа сервера.
     *
     * @return Меню.
     */
    public static MenuFile parse (@NotNull ServerResponse response) throws IOException {
        if (response == null) { throw new IllegalArgumentException(); }

        String text = response.readRemainingAnsiText();
        StringReader reader = new StringReader(text);
        Scanner scanner = new Scanner(reader);
        MenuFile result = parse(scanner);

        return result;
    }

    /**
     * Чтение меню с сервера.
     *
     * @param connection    Подключение.
     * @param specification Имя файла.
     * @return Меню.
     */
    @NotNull
    public static MenuFile read (@NotNull IrbisConnection connection, @NotNull FileSpecification specification) throws IOException {
        if (connection == null) { throw new IllegalArgumentException(); }

        MenuFile result;
        String text = connection.readTextFile(specification);
        if (isNullOrEmpty(text)) {
            result = new MenuFile();
        } else {
            StringReader reader = new StringReader(text);
            Scanner scanner = new Scanner(reader);
            result = parse(scanner);
        }

        result.fileName = specification.fileName;

        return result;
    }

    /**
     * Добавление строчек в меню.
     *
     * @return Собственно меню.
     */
    @NotNull
    @SuppressWarnings("UnusedReturnValue")
    public MenuFile add (@NotNull String code, @Nullable String comment) {
        MenuEntry entry = new MenuEntry(code, comment);
        entries.add(entry);

        return this;
    }

    /**
     * Отыскивает запись, соответствующую данному коду.
     *
     * @return Запись либо null.
     */
    @Nullable
    public MenuEntry getEntry (@NotNull String code) {
        if (code == null) { throw new IllegalArgumentException(); }

        for (MenuEntry entry : entries) {
            if (code.equalsIgnoreCase(entry.code)) {
                return entry;
            }
        }

        code = code.trim();
        for (MenuEntry entry : entries) {
            if (code.equalsIgnoreCase(entry.code)) {
                return entry;
            }
        }

        code = trimCode(code);
        for (MenuEntry entry : entries) {
            if (code.equalsIgnoreCase(entry.code)) {
                return entry;
            }
        }

        return null;
    }

    @Nullable
    public String getValue(@NotNull String code) {
        return getValue(code, null);
    }

    /**
     * Выдает значение, соответствующее коду.
     *
     * @param code         Код.
     * @param defaultValue Значение по умолчанию.
     * @return Найденное значение либо null.
     */
    @Nullable
    public String getValue (@NotNull String code, @Nullable String defaultValue) {
        if (isNullOrEmpty(code)) {
            throw new IllegalArgumentException();
        }

        MenuEntry found = getEntry(code);
        String result = found == null ? null : found.comment;

        return result;
    }

    //=========================================================================

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (MenuEntry entry : entries) {
            result.append(entry.code);
            result.append(IrbisText.MSDOS_DELIMITER);
            result.append(entry.comment);
            result.append(IrbisText.MSDOS_DELIMITER);
        }
        result.append(STOP_MARKER);

        return result.toString();
    }
}
