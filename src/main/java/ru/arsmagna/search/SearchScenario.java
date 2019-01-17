// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.search;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import ru.arsmagna.Utility;
import ru.arsmagna.infrastructure.IniFile;
import ru.arsmagna.infrastructure.IniSection;

import java.util.ArrayList;

import static ru.arsmagna.Utility.nullToEmpty;
import static ru.arsmagna.Utility.safeParseInt32;

/**
 * Сценарий поиска.
 */
@SuppressWarnings("WeakerAccess")
public final class SearchScenario {

    /**
     * Название поискового атрибута (автор, заглавие, инвентарный номер).
     */
    public String name;

    /**
     * Префикс для соответствующих терминов в словаре (может быть пустым).
     */
    public String prefix;

    /**
     * Тип словаря для соответствующего поиска.
     */
    public int dictionaryType;

    /**
     * Имя файла справочника.
     */
    public String menuName;

    /**
     * Имя формата (без расширения).
     */
    public String oldFormat;

    /**
     * Способ Корректировки по словарю.
     */
    public String correction;

    /**
     * Исходное положение переключателя "Усечение".
     */
    public String truncation;

    /**
     * Текст подсказки/предупреждения.
     */
    public String hint;

    /**
     * Параметр пока не задействован.
     */
    public String modByDicAuto;

    /**
     * Логические операторы.
     */
    public String logic;

    /**
     * Правила автоматического расширения поиска
     * на основе авторитетного файла или тезауруса.
     */
    public String advance;

    /**
     * Имя формата показа документов.
     */
    public String format;

    //=========================================================================

 private static String get(@NotNull IniSection section, @NotNull String name, int index) {
     String fullName = "Item" + name + Integer.toString(index);
     return section.getValue(fullName);
 }
    //=========================================================================

    /**
     * Парсинг INI-файла.
     *
     * @param iniFile INI-файл
     * @return Найденные сценарии поиска
     */
    public static SearchScenario[] parse(@NotNull IniFile iniFile) {
        ArrayList<SearchScenario> result = new ArrayList<>();
        IniSection section = iniFile.findSection("SEARCH");
        if (section != null) {
            String itemNumb = section.getValue("ItemNumb");
            int count = safeParseInt32(itemNumb);
            for (int i = 0; i < count; i++) {
                SearchScenario scenario = new SearchScenario();
                scenario.name = get(section, "Name", i);
                scenario.prefix = get(section, "Pref", i);
                scenario.dictionaryType = safeParseInt32(get(section, "DictionType", i));
                scenario.menuName = get(section, "Menu", i);
                scenario.oldFormat = null;
                scenario.correction = get(section, "ModByDic", i);
                scenario.truncation = get(section, "Tranc", i);
                scenario.hint = get(section, "Hint", i);
                scenario.modByDicAuto = get(section, "ModByDicAuto", i);
                scenario.logic = get(section, "Logic", i);
                scenario.advance = get(section, "Adv", i);
                scenario.format = get(section, "Pft", i);

                result.add(scenario);
            }
        }

        return result.toArray(new SearchScenario[0]);
    }
    //=========================================================================

    @Override
    @Contract(pure = true)
    public String toString() {
        if (Utility.isNullOrEmpty(prefix)) {
            return nullToEmpty(name);
        }

        return nullToEmpty(name) + " (" + nullToEmpty(prefix) + ")";
    }
}
