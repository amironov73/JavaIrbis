// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

/**
 * Статус записи.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class RecordStatus {
    /**
     * Запись логически удалена.
     */
    public static final int LOGICALLY_DELETED = 1;

    /**
     * Запись физически удалена.
     */
    public static final int PHYSICALLY_DELETED = 2;

    /**
     * Запись отсутствует.
     */
    public static final int ABSENT = 4;

    /**
     * Запись не актуализирована.
     */
    public static final int NON_ACTUALIZED = 8;

    /**
     * Последняя версия записи.
     */
    public static final int LAST = 32;

    /**
     * Запись заблокирована.
     */
    public static final int LOCKED = 64;
}
