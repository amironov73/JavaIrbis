package ru.arsmagna.worksheet;

/**
 * Элемент рабочего листа.
 */
public class WorksheetItem
{
    /**
     * Числовая метка поля/код подполя.
     */
    public String tag;

    /**
     * Наименование поля/подполя.
     */
    public String title;

    /**
     * Повторяемость. Для подполей игнорируется.
     */
    public boolean repeatable;

    /**
     * Индекс контекстной помощи.
     */
    public String help;

    /**
     * Режим ввода.
     */
    public String editMode;

    /**
     * Дополнительная информация для расширенных средств ввода.
     */
    public String inputInfo;

    /**
     * ФЛК.
     */
    public String formalVerification;

    /**
     * Подсказка - текст помощи (инструкции),
     * сопровождающий ввод в поле.
     */
    public String hint;

    /**
     * Значение по умолчанию при создании новой записи.
     */
    public String defaultValue;

    /**
     * Используется при определенных режимах ввода.
     */
    public String reserved;

    /**
     * Произвольные пользовательские данные.
     */
    public Object userData;
}
