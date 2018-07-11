package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;

public final class OptFile {

    public static final char WILDCARD = '+';

    //=========================================================================

    public final Collection<OptLine> lines = new ArrayList<>();

    public int worksheetLength;

    public int worksheetTag;

    //=========================================================================

    @Nullable
    public String getWorksheet(@NotNull MarcRecord record) {
        if (record == null) {
            throw new IllegalArgumentException();
        }

        return record.fm(worksheetTag);
    }

    public static OptFile parse(@NotNull File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException();
        }

        try (FileInputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                OptFile result = parse(scanner);

                return result;
            }
        }
    }

    public static OptFile parse(@NotNull Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException();
        }

        OptFile result = new OptFile();
        result.worksheetTag = scanner.nextInt();
        result.worksheetLength = scanner.nextInt();
        while (scanner.hasNext()) {
            String text = scanner.nextLine();
            if (text.startsWith("*")) {
                break;
            }

            OptLine line = OptLine.parse(text);
            result.lines.add(line);
        }

        return result;
    }

    public static OptFile parse (@NotNull ServerResponse response) throws IOException {
        if (response == null) {
            throw new IllegalArgumentException();
        }

        String text = response.readRemainingAnsiText();
        StringReader reader = new StringReader(text);
        Scanner scanner = new Scanner(reader);
        OptFile result = parse(scanner);

        return result;
    }

    public static OptFile read (@NotNull IrbisConnection connection, @NotNull FileSpecification specification) throws IOException {
        if (connection == null) {
            throw new IllegalArgumentException();
        }

        OptFile result;
        String text = connection.readTextFile(specification);
        if (isNullOrEmpty(text)) {
            result = new OptFile();
        } else {
            StringReader reader = new StringReader(text);
            Scanner scanner = new Scanner(reader);
            result = parse(scanner);
        }

        return result;
    }

    public static boolean sameChar(char pattern, char testable) {
        if (pattern == WILDCARD) {
            return true;
        }

        return Character.toLowerCase(pattern) == Character.toLowerCase(testable);
    }

    public static boolean sameText(@NotNull String pattern, @NotNull String testable) {
        if (pattern == null || testable == null) {
            throw new IllegalArgumentException();
        }

        if (pattern.length() == 0) {
            return false;
        }

        if (testable.length() == 0) {
            return pattern.charAt(0) == WILDCARD;
        }

        int patternIndex = 0, testableIndex = 0;
        while (true) {
            char patternChar = pattern.charAt(patternIndex);
            char testableChar = testable.charAt(testableIndex);
            boolean patternNext = patternIndex++ < pattern.length();
            boolean testableNext = testableIndex++ < testable.length();

            if (patternNext && !testableNext) {
                if (patternChar == WILDCARD) {
                    while (patternIndex < pattern.length()) {
                        patternChar = pattern.charAt(patternIndex);
                        patternIndex++;
                        if (patternChar != WILDCARD) {
                            return false;
                        }
                    }

                    return true;
                }
            }

            if (patternNext != testableNext) {
                return false;
            }

            if (!patternNext) {
                return true;
            }

            if (!sameChar(patternChar, testableChar)) {
                return false;
            }
        }
    }

    public String resolveWorksheet(@NotNull String tag) throws IrbisException {
        for (OptLine line: lines) {
            if (sameText(line.pattern, tag))
            {
                return line.worksheet;
            }
        }

        throw new IrbisException();
    }
}
