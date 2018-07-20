package ru.arsmagna;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Путь на файл path.database.filename.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class FileSpecification {
    /**
     * Признак двоичного файла.
     */
    public boolean isBinaryFile;

    /**
     * Путь.
     */
    public int path;

    /**
     * База данных.
     */
    public String database;

    /**
     * Имя файла.
     */
    public String fileName;

    /**
     * Содержимое текстового файла.
     */
    public String content;

    //=========================================================================

    /**
     * Конструктор по умолчанию.
     */
    public FileSpecification() {
    }

    /**
     * Конструктор.
     *
     * @param path     Путь.
     * @param database База данных.
     * @param fileName Имя файла.
     */
    public FileSpecification(int path, @Nullable String database,
                             @NotNull String fileName) {
        this.path = path;
        this.database = database;
        this.fileName = fileName;
    }

    //=========================================================================

    @Override
    public int hashCode() {
        return Objects.hash(path, database, fileName, content);
    }

    @Override
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        FileSpecification other = (FileSpecification) obj;

        return this.path == other.path
                && this.database.equals(other.database)
                && this.fileName.equals(other.fileName);
    }

    @Override
    public String toString() {
        String result = fileName;

        if (isBinaryFile) {
            result = "@" + fileName;
        } else if (content != null) {
            result = "&" + fileName;
        }

        switch (path) {
            case 0:
            case 1:
                result = Integer.toString(path) + ".." + result;
                break;

            default:
                result = Integer.toString(path) + '.' + database + '.' + result;
                break;
        }

        if (content != null) {
            result = result + "&" + content;
        }

        return result;
    }
}
