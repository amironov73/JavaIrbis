package ru.arsmagna.direct;

/**
 * Справочник в N01/L01 является таблицей, определяющей
 * поисковый термин. Каждый ключ переменной длины, который
 * есть в записи, представлен в справочнике одним входом,
 * формат которого описывает следующая структура
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class NodeItem {

    /**
     * Длина ключа.
     */
    public short length;

    /**
     * Смещение ключа от начала записи.
     */
    public short keyOffset;

    /**
     * Младшее слово смещения.
     */
    public int lowOffset;

    /**
     * Старшее слово смещения.
     */
    public int highOffset;

    /**
     * Текстовое значение ключа.
     */
    public String text;

    /**
     * Полное смещение.
     *
     * @return Полное смещение.
     */
    public long fullOffset() {
        return (((long)highOffset) << 32) + lowOffset;
    }

    /**
     * Ссылается на лист?
     * @return true or false
     */
    public boolean refersToLeaf() {
        return lowOffset < 0;
    }

    //=========================================================================

    @Override
    public String toString() {
        return String.format("Length: %d, KeyOffset: %d, "
            + "LowOffset: %d, HighOffset: %d, FullOffset:%d, "
            + "Text: %s", length, keyOffset, lowOffset,
                highOffset, fullOffset(), text);
    }
}
