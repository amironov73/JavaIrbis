package ru.arsmagna;

/**
 * Постинг терма.
 */
public class TermPosting
{
    /**
     * MFN записи с искомым термом.
     */
    public int mfn;

    /**
     * Метка поля с искомым термом.
     */
    public int tag;

    /**
     * Повторение поля.
     */
    public int occurrence;

    /**
     * Количество повторений.
     */
    public int count;

    /**
     * Результат форматирования.
     */
    public String text;

    //=========================================================================

    /**
     * Клонирование.
     * @return Копию.
     */
    public TermPosting Clone()
    {
        TermPosting result = new TermPosting();
        result.mfn = mfn;
        result.tag = tag;
        result.occurrence = occurrence;
        result.count = count;
        result.text = text;

        return result;
    }

    //=========================================================================

    @Override
    public String toString()
    {
        return "TermPosting{" +
            "mfn=" + mfn +
            ", tag=" + tag +
            ", occurrence=" + occurrence +
            ", count=" + count +
            ", text='" + text + '\'' +
            '}';
    }
}
