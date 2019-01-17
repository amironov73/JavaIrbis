// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.search;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.IrbisConnection;
import ru.arsmagna.IrbisException;
import ru.arsmagna.MarcRecord;

import java.io.IOException;

/**
 * Наиболее распространенные поиски.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class CommonSearches {

    /**
     * Ключевые слова.
     */
    public static final String KEYWORD_PREFIX = "K=";

    /**
     * Индивидуальный автор, редактор, составитель.
     */
    public static final String AUTHOR_PREFIX = "A=";

    /**
     * Коллектив или мероприятие.
     */
    public static final String COLLECTIVE_PREFIX = "M=";

    /**
     * Заглавие.
     */
    public static final String TITLE_PREFIX = "T=";

    /**
     * Инвентарный номер, штрих-код или радиометка.
     */
    public static final String INVENTORY_PREFIX = "IN=";

    /**
     * Шифр документа в базе.
     */
    public static final String INDEX_PREFIX = "I=";

    //=========================================================================

    /**
     * Поиск единственной записи, содержащей экземпляр
     * с указанным инвентарным номером (или штрих-кодом
     * или радио-меткой).
     * Запись может отсутствовать, это не будет считаться ошибкой.
     *
     * @param connection Подключение к серверу
     * @param inventory  Инвентарный номер
     * @return Запись или null
     * @throws IOException    Ошибка ввода-вывода
     * @throws IrbisException Ошибка протокола
     */
    @Nullable
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static MarcRecord byInventory(@NotNull IrbisConnection connection,
                                         @NotNull String inventory)
            throws IOException, IrbisException {
        MarcRecord result = singleOrDefault(connection, INVENTORY_PREFIX, inventory);

        return result;
    }

    /**
     * Поиск единственной записи с указанным шифром в базе.
     * Запись должна существовать.
     *
     * @param connection Подключение к серверу
     * @param index      Шифр в базе
     * @return Запись
     * @throws IOException    Ошибка ввода-вывода
     * @throws IrbisException Ошибка протокола
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static MarcRecord byIndex(@NotNull IrbisConnection connection,
                                     @NotNull String index)
            throws IOException, IrbisException {
        MarcRecord result = required(connection, INDEX_PREFIX, index);

        return result;
    }

    /**
     * Поиск первой попавшейся записи, удовлетворяющей
     * указанному условию.
     * Запись может отсутствовать, это не будет считаться ошибкой.
     *
     * @param connection Подключение к серверу
     * @param prefix     Поисковый префикс
     * @param value      Искомое значение
     * @return Найденная запись или null
     * @throws IOException    Ошибка ввода-вывода
     * @throws IrbisException Ошибка протокола
     */
    @Nullable
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static MarcRecord firstOrDefault(@NotNull IrbisConnection connection,
                                            @NotNull String prefix,
                                            @NotNull String value)
            throws IOException, IrbisException {
        SearchParameters parameters = new SearchParameters();
        parameters.database = connection.database;
        parameters.searchExpression = '"' + prefix + value + '"';
        parameters.numberOfRecords = 1;
        parameters.firstRecord = 1;
        int[] found = connection.search(parameters);
        if (found.length == 0) {
            return null;
        }

        MarcRecord result = connection.readRecord(found[0]);

        return result;
    }

    /**
     * Поиск единственной записи, удовлетворяющей
     * указанному условию.
     * Запись может отсутствовать, это не будет считаться ошибкой.
     *
     * @param connection Подключение к серверу
     * @param prefix     Поисковый префикс
     * @param value      Искомое значение
     * @return Найденная запись либо null
     * @throws IOException    Ошибка ввода-вывода
     * @throws IrbisException Ошибка протокола
     */
    @Nullable
    @SuppressWarnings("UnnecessaryLocalVariable")
    public static MarcRecord singleOrDefault(@NotNull IrbisConnection connection,
                                             @NotNull String prefix,
                                             @NotNull String value)
            throws IOException, IrbisException {
        SearchParameters parameters = new SearchParameters();
        parameters.database = connection.database;
        parameters.searchExpression = '"' + prefix + value + '"';
        parameters.numberOfRecords = 2;
        parameters.firstRecord = 1;
        int[] found = connection.search(parameters);
        if (found.length == 0) {
            return null;
        }
        if (found.length != 1) {
            throw new IrbisException("Too many records found");
        }

        MarcRecord result = connection.readRecord(found[0]);

        return result;
    }

    /**
     * Поиск единственной записи, удовлетворяющей указанному условию.
     * Запись должна существовать.
     *
     * @param connection Подключение к серверу
     * @param prefix     Поисковый префикс
     * @param value      Искомое значение
     * @return Найденная запись
     * @throws IOException    Ошибка ввода-вывода
     * @throws IrbisException Ошибка протокола,
     *                        найдено более одной записи, либо вообще ничего не найдено
     */
    public static MarcRecord required(@NotNull IrbisConnection connection,
                                      @NotNull String prefix,
                                      @NotNull String value)
            throws IOException, IrbisException {
        MarcRecord result = singleOrDefault(connection, prefix, value);
        if (result == null) {
            throw new IrbisException("Not found: " + prefix + value);
        }

        return result;
    }
}
