package ru.arsmagna.search;

/**
 * Какие логические операторы могут использоваться
 *  для данного вида поиска.
 */
public final class SearchLogicType
{
    /**
     * только логика ИЛИ
     */
    public static final int OR = 0;

    /**
     * логика ИЛИ и И
     */
    public static final int OR_AND = 1;

    /**
     * логика ИЛИ, И, НЕТ (по умолчанию)
     */
    public static final int OR_AND_NOT = 2;

    /**
     * логика ИЛИ, И, НЕТ, И (в поле)
     */
    public static final int OR_AND_NOT_FIELD = 3;

    /**
     * логика ИЛИ, И, НЕТ, И (в поле), И (фраза)
     */
    public static final int OR_AND_NOT_PHRASE = 4;
}
