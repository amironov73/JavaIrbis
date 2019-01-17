// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.search;

/**
 * Тип словаря для соответствующего поиска.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DictionaryType {
    /**
     * Стандартный словарь, содержащий алфавитный
     * список терминов с указанием количества ссылок
     * для каждого из них.
     */
    public static final int STANDARD = 0;

    /**
     * Словарь, дополнительно к стандартным данным содержащий
     * пояснения (раскодировку) для каждого термина; применяется
     * для терминов, которые представляют собой кодированную
     * информацию (например, "Страна издания") и для которых
     * имеется соответствующий справочник (файл с расширением
     * MNU - например, STR.MNU для кодов стран); в этом случае
     * соответствующий справочник указывается
     * в параметре ItemMenuNN.
     */
    public static final int EXPLANATORY = 1;

    /**
     * Специальный вид компоненты "Словарь" для
     * Тематического рубрикатора.
     */
    public static final int RUBRICATOR = 2;
}
