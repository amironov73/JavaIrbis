// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.infrastructure.MenuEntry;
import ru.arsmagna.infrastructure.MenuFile;
import ru.arsmagna.infrastructure.ServerResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;
import static ru.arsmagna.Utility.itemAt;

/**
 * Информация о базе данных ИРБИС.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class DatabaseInfo {

    /**
     * Имя базы данных.
     */
    public String name;

    /**
     * Описание базы данных.
     */
    public String description;

    /**
     * Максимальный MFN.
     */
    public int maxMfn;

    /**
     * Логически удаленные записи.
     */
    public int[] logicallyDeletedRecords;

    /**
     * Физически удаленные записи.
     */
    public int[] physicallyDeletedRecords;

    /**
     * Неактуализированные записи.
     */
    public int[] nonActualizedRecords;

    /**
     * Заблокированные записи.
     */
    public int[] lockedRecords;

    /**
     * Признак блокировки базы данных в целом.
     */
    public boolean databaseLocked;

    /**
     * База только для чтения.
     */
    public boolean readOnly;

    //=========================================================================

    private static int[] parse(@Nullable String line) {
        if (Utility.isNullOrEmpty(line)) {
            return new int[0];
        }

        String[] items = line.split(IrbisText.SHORT_DELIMITER);
        int[] result = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            result[i] = FastNumber.parseInt32(items[i]);
        }

        return result;
    }

    //=========================================================================

    /**
     * Разбор ответа сервера.
     *
     * @param response Ответ сервера.
     * @return Информация о базе данных.
     */
    public static DatabaseInfo parse(@NotNull ServerResponse response) {
        DatabaseInfo result = new DatabaseInfo();
        result.logicallyDeletedRecords = parse(response.readAnsi());
        result.physicallyDeletedRecords = parse(response.readAnsi());
        result.nonActualizedRecords = parse(response.readAnsi());
        result.lockedRecords = parse(response.readAnsi());
        result.maxMfn = itemAt(parse(response.readAnsi()), 0);
        result.databaseLocked = itemAt(parse(response.readAnsi()), 0) != 0;

        return result;
    }

    /**
     * Считывание списка баз из файла меню.
     *
     * @param menuFile Файл меню.
     * @return Список баз данных.
     * @throws IOException Ошибка ввода-вывода.
     */
    public static DatabaseInfo[] parse(@NotNull File menuFile)
            throws IOException {
        ArrayList<DatabaseInfo> result = new ArrayList<>();
        try(FileInputStream stream = new FileInputStream(menuFile)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                while (scanner.hasNext()) {
                    String name = scanner.nextLine();
                    if (name.startsWith("*") || !scanner.hasNext()) {
                        break;
                    }
                    boolean readOnly = false;
                    if (name.startsWith("-")) {
                        name = name.substring(1);
                        readOnly = true;
                    }
                    String description = scanner.nextLine();
                    DatabaseInfo info = new DatabaseInfo();
                    info.name = name;
                    info.description = description;
                    info.readOnly = readOnly;
                    result.add(info);
                }
            }
        }
        return result.toArray(new DatabaseInfo[0]);
    }

    /**
     * Разбор MNU-файла с сервера.
     *
     * @param menuFile MNU-файл
     * @return Найденные базы данных
     */
    public static DatabaseInfo[] parse(@NotNull MenuFile menuFile) {
        ArrayList<DatabaseInfo> result = new ArrayList<>();
        for (MenuEntry entry: menuFile.entries) {
            String name = entry.code;
            String description = entry.comment;
            boolean readOnly = false;
            if (name.startsWith("-")) {
                name = name.substring(1);
                readOnly = true;
            }
            DatabaseInfo info = new DatabaseInfo();
            info.name = name;
            info.description = description;
            info.readOnly = readOnly;
            result.add(info);
        }

        return result.toArray(new DatabaseInfo[0]);
    }
    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        if (isNullOrEmpty(description)) {
            return name;
        }
        return name + " - " + description;
    }
}
