package ru.arsmagna.direct;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Файлы N01 и L01 содержат  в себе индексы словаря поисковых
 * терминов и состоят из записей (блоков) постоянной длины.
 * Записи состоят из трех частей: лидера, справочника
 * и ключей переменной длины.
 */
public class NodeRecord
{
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
    public NodeRecord()
    {
        leader = new NodeLeader();
        items = new ArrayList<NodeItem>();
    }
}
