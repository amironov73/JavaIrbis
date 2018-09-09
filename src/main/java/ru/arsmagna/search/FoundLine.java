package ru.arsmagna.search;

/**
 * One line in the list of found documents.
 */
@SuppressWarnings("unused")
public class FoundLine {

    /**
     * Whether the line is materialized?
     */
    public boolean materialized;

    /**
     * Serial number.
     */
    public int serialNumber;

    /**
     * MFN.
     */
    public int mfn;

    /**
     * Icon.
     */
    public Object icon;

    /**
     * Selected by user.
     */
    public boolean selected;

    /**
     * Description.
     */
    public String description;

    /**
     * For list sorting.
     */
    public String sort;

    /**
     * Arbitrary user data.
     */
    public Object userData;
}
