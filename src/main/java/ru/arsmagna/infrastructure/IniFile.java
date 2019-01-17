// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.IrbisEncoding;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static ru.arsmagna.infrastructure.IniLine.sameKey;

/**
 * INI-файл.
 */
@SuppressWarnings({"UnnecessaryLocalVariable", "unused", "WeakerAccess"})
public final class IniFile {

    /**
     * Name of the file (can be null).
     */
    public String fileName;

    /**
     * Секции.
     */
    public final Collection<IniSection> sections;

    //=========================================================================

    /**
     * Конструктор
     */
    public IniFile() {
        sections = new ArrayList<>();
    }

    //=========================================================================

    @Nullable
    public IniSection findSection(@Nullable String name) {
        for (IniSection section: sections) {
            if (name == null) {
                if (section.name == null) {
                    return section;
                }
            }
            else if (section.name != null && sameKey(section.name, name)) {
                    return section;
                }
        }

        return null;
    }

    public IniSection getOrCreateSection(@Nullable String name) {
        IniSection result = findSection(name);
        if (result == null) {
            result = new IniSection(name);
            sections.add(result);
        }

        return result;
    }

    @Nullable
    public String getValue(@Nullable String sectionName, @NotNull String key) {
        return getValue(sectionName, key, null);
    }

    @Nullable
    public String getValue(@Nullable String sectionName, @NotNull String key, @Nullable String defaultValue) {
        IniSection section = findSection(sectionName);
        if (section == null) {
            return defaultValue;
        }

        return section.getValue(key, defaultValue);
    }

    public static IniFile parse(@NotNull Scanner scanner) {
        IniFile result = new IniFile();
        IniSection section = null;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            line = line.trim();
            if (line.equals("")){
                continue;
            }
            if (line.startsWith("[")) {
                line = line.substring(1, line.length() - 1);
                section = new IniSection(line);
                result.sections.add(section);
            }
            else if (section != null) {
                String[] parts = line.split("=", 2);
                String key = parts[0];
                String value = parts.length == 2 ? parts[1] : "";               
                IniLine item = new IniLine(key, value);
                if (section == null) {
                   section = new IniSection(null);
                   result.sections.add(section);
                }
                section.lines.add(item);
            }
        }
        
        return result;
    }

    public static IniFile parse(@NotNull File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                IniFile result = parse(scanner);
                result.fileName = file.getAbsolutePath();

                return result;
            }
        }

    }

    public static IniFile parse (@NotNull ServerResponse response) throws IOException {
        String text = response.readRemainingAnsiText();
        StringReader reader = new StringReader(text);
        Scanner scanner = new Scanner(reader);
        IniFile result = parse(scanner);

        return result;
    }

    public IniFile setValue(@Nullable String sectionName, @NotNull String key,
                            @Nullable String value) {
        IniSection section = getOrCreateSection(sectionName);
        section.setValue(key, value);

        return this;
    }

    //=========================================================================

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (IniSection section : sections) {
            if (!first) {
                result.append("\n");
            }
            first = false;
            result.append(section);
        }

        return result.toString();
    }
}
