package ru.arsmagna.search;

import org.jetbrains.annotations.Contract;

/**
 * Сценарий поиска.
 */
public final class SearchScenario {
    /**
     * Название поиска.
     */
    public String name;

    /**
     * Префикс для соответствующих терминов в словаре.
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

    @Override
    @Contract(pure = true)
    public String toString() {
        return "SearchScenario{" +
                "name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                '}';
    }
}
