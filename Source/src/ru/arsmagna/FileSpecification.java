package ru.arsmagna;

/**
 * Путь на файл path.database.filename.
 */
public class FileSpecification
{
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
    public FileSpecification()
    {
    }

    /**
     * Конструктор.
     *
     * @param path     Путь.
     * @param database База данных.
     * @param fileName Имя файла.
     */
    public FileSpecification
    (
        int path,
        String database,
        String fileName
    )
    {
        this.path = path;
        this.database = database;
        this.fileName = fileName;
    }

    //=========================================================================

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (obj == this)
        {
            return true;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        FileSpecification other = (FileSpecification) obj;

        return this.path == other.path
            && this.database.equals(other.database)
            && this.fileName.equals(other.fileName);
    }

    @Override
    public String toString()
    {
        String result = fileName;

        if (isBinaryFile)
        {
            result = "@" + fileName;
        } else if (content != null)
        {
            result = "&" + fileName;
        }

        switch (path)
        {
            case 0:
            case 1:
                result = new Integer(path).toString() + ".." + result;
                break;

            default:
                result = new Integer(path).toString() + '.' + database + '.' + fileName;
                break;
        }

        if (content != null)
        {
            result = result + "&" + content;
        }

        return result;
    }
}
