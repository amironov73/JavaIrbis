// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.worksheet;

/**
 * Режим редактирования поля/подполя.
 */
@SuppressWarnings("WeakerAccess")
public class EditMode {
    /**
     * Ввод без дополнительной обработки.
     * Простая строка ввода
     */
    public static final int none = 0;

    /**
     * Ввод через простое (не иерархическое)
     * меню (справочник).
     */
    public static final int menu = 1;

    /**
     * Ввод через поисковый словарь.
     */
    public static final int dictionary = 2;

    /**
     * Ввод через рубрикатор ГРНТИ.
     */
    public static final int rubricator = 3;

    /**
     * Ввод через оконный редактор
     */
    public static final int multiline = 4;

    /**
     * Ввод через вложенный рабочий лист.
     */
    public static final int subSheet = 5;

    /**
     * Ввод через иерархический справочник
     */
    public static final int tree = 6;

    /**
     * Ввод с использованием переключателей.
     */
    public static final int switches = 7;

    /**
     * Ввод с использованием внешней программы.
     */
    public static final int externalProgram = 8;

    /**
     * Ввод на основе маски (шаблона).
     */
    public static final int template = 9;

    /**
     * Ввод через авторитетный файл.
     */
    public static final int authorityFile = 10;

    /**
     * Ввод через тезаурус.
     */
    public static final int thesaurus = 11;

    /**
     * Ввод через обращение к внешнему файлу.
     */
    public static final int externalFile = 12;

    /**
     * Ввод на основе ИРБИС-Навигатора.
     */
    public static final int navigator = 13;

    /**
     * Ввод с помощью режима (функции) пользователя.
     */
    public static final int externalDll = 14;

    /**
     * Ввод с помощью динамического справочника.
     */
    public static final int dynamicMenu = 15;

    /**
     * Ввод с помощью файловых ресурсов системы ИРБИС.
     */
    public static final int serverResource = 16;

    /**
     * Table mode (F3).
     */
    public static final int tableMode = 1024;
}
