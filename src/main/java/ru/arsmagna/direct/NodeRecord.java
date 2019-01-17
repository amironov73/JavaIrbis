// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.direct;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Файлы N01 и L01 содержат  в себе индексы словаря поисковых
 * терминов и состоят из записей (блоков) постоянной длины.
 * Записи состоят из трех частей: лидера, справочника
 * и ключей переменной длины.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class NodeRecord {

    /**
     * Длина записи в текущей реализации.
     */
    public static final int RECORD_SIZE = 2048;

    //=========================================================================

    /**
     * Лист?
     */
    public boolean isLeaf;

    /**
     * Заголовок записи.
     */
    public NodeLeader leader;

    /**
     * Ссылки.
     */
    public Collection<NodeItem> items;

    //=========================================================================

    /**
     * Конструктор.
     */
    public NodeRecord() {
        leader = new NodeLeader();
        items = new ArrayList<NodeItem>();
    }

    //=========================================================================

    @Override
    public String toString() {
        StringBuilder items = new StringBuilder();
        for (NodeItem item: this.items) {
            items.append(item.toString());
            items.append('\n');
        }

        return String.format("%s%n%s", leader.toString(),
                items.toString());
    }
}
