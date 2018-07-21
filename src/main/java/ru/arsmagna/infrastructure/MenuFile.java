package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * Файл меню.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
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
     * Добавление строчек в меню.
     *
     * @return Собственно меню.
     */
    @SuppressWarnings("UnusedReturnValue")
    public MenuFile add(@NotNull String code, @Nullable String comment) {
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
    public MenuEntry getEntry(@NotNull String code) {
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
    @SuppressWarnings("UnnecessaryLocalVariable")
    public String getValue(@NotNull String code, @Nullable String defaultValue) {
        if (isNullOrEmpty(code)) {
            throw new IllegalArgumentException();
        }

        MenuEntry found = getEntry(code);
        String result = found == null ? defaultValue : found.comment;

        return result;
    }

    /**
     * Разбор потока.
     *
     * @param scanner Поток.
     * @return Меню.
     */
    public static MenuFile parse(@NotNull Scanner scanner) {
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
    public static MenuFile parse(@NotNull File file) throws IOException {
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
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static MenuFile parse(@NotNull ServerResponse response) throws IOException {
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
    public static MenuFile read(@NotNull IrbisConnection connection,
                                @NotNull FileSpecification specification)
            throws IOException {
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
     * Отрезание лишних символов в коде.
     *
     * @return Очищенный код
     */
    public static String trimCode(@NotNull String code) {
        code = code.trim();
        String[] parts = code.split(MENU_SEPARATORS);
        if (parts.length != 0) {
            code = parts[0];
        }

        return code;
    }

    /**
     * Вывод меню в текстовый поток.
     *
     * @param writer Текстовый поток.
     */
    public void write(@NotNull PrintWriter writer) {
        for (MenuEntry entry : entries) {
            writer.println(entry.code);
            writer.println(entry.comment);
        }
        writer.print(STOP_MARKER);
    }

    /**
     * Запись меню в файл.
     *
     * @param file Файл, в который надо записать меню.
     * @throws IOException Ошибка ввода-вывода.
     */
    public void write(@NotNull File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file, IrbisEncoding.ansi().name())) {
            write(writer);
        }
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
