package ru.arsmagna.direct;

import java.util.stream.Stream;

public class InvertedFile64 {

    /**
     * Длина записи L01/N01.
     */
    public static final int NODE_LENGTH = 2048;

    /**
     * Максимальный размер термина.
     */
    public static final int MAX_TERM_SIZE = 255;

    /**
     * Размер блока.
     */
    public static final int BLOCK_SIZE = 205048;

    //=========================================================================

    /**
     * Name of the file.
     */
    public String fileName;

    /**
     * Access mode.
     */
    public int mode;

    /**
     * IFP file.
     */
    public Stream Ifp;

    /**
     * Control record of the IFP file.
     */
    public IfpControlRecord64 IfpControlRecord;

    public Stream L01;

    public Stream N01;
}
