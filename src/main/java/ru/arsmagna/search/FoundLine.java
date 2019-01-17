// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

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
