// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;

import ru.arsmagna.*;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;
import static ru.arsmagna.Utility.nullToEmpty;

/**
 * PAR-file.
 */
@SuppressWarnings({"UnnecessaryLocalVariable", "WeakerAccess", "unused"})
public final class ParFile {

    /**
     * Extension.
     */
    public static final String EXTENSION = ".par";

    //=========================================================================

    public String xrf;
    public String mst;
    public String cnt;
    public String n01;
    public String n02;
    public String l01;
    public String l02;
    public String ifp;
    public String any;
    public String pft;
    public String ext;

    //=========================================================================

    public ParFile() {
    }

    public ParFile(@NotNull String mst) {
        this.xrf = mst;
        this.mst = mst;
        this.cnt = mst;
        this.n01 = mst;
        this.n02 = mst;
        this.l01 = mst;
        this.l02 = mst;
        this.ifp = mst;
        this.any = mst;
        this.pft = mst;
        this.ext = mst;
    }

    //=========================================================================

    private static Map<String, String> readDictionary(@NotNull Scanner scanner) throws IrbisException {
        HashMap<String, String> result = new HashMap<>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine().trim();
            if (isNullOrEmpty(line)) {
                continue;
            }
            String[] parts = line.split("=", 2);
            if (parts.length != 2) {
                throw new IrbisException();
            }
            String key = parts[0].trim();
            if (isNullOrEmpty(key)) {
                throw new IrbisException();
            }
            String value = parts[1].trim();
            result.put(key, value);
        }

        return result;
    }

    //=========================================================================

    public static ParFile parse(@NotNull Scanner scanner) throws IrbisException {
        Map<String, String> map = readDictionary(scanner);
        ParFile result = new ParFile();
        result.xrf = map.getOrDefault("1", null);
        result.mst = map.getOrDefault("2", null);
        result.cnt = map.getOrDefault("3", null);
        result.n01 = map.getOrDefault("4", null);
        result.n02 = map.getOrDefault("5", null);
        result.l01 = map.getOrDefault("6", null);
        result.l02 = map.getOrDefault("7", null);
        result.ifp = map.getOrDefault("8", null);
        result.any = map.getOrDefault("9", null);
        result.pft = map.getOrDefault("10", null);
        result.ext = map.getOrDefault("11", null);

        return result;
    }

    public static ParFile parse(@NotNull File file) throws IrbisException, IOException {
        try(InputStream stream = new FileInputStream(file)) {
            try(Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                ParFile result = parse(scanner);

                return result;
            }
        }
    }

    //=========================================================================

    @Override
    public String toString() {
        return "1=" + nullToEmpty(xrf) + "\n"
             + "2=" + nullToEmpty(mst) + "\n"
             + "3=" + nullToEmpty(cnt) + "\n"
             + "4=" + nullToEmpty(n01) + "\n"
             + "5=" + nullToEmpty(n02) + "\n"
             + "6=" + nullToEmpty(l01) + "\n"
             + "7=" + nullToEmpty(l02) + "\n"
             + "8=" + nullToEmpty(ifp) + "\n"
             + "9=" + nullToEmpty(any) + "\n"
             + "10=" + nullToEmpty(pft) + "\n"
             + "11=" + nullToEmpty(ext) + "\n";
    }
}
