package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * STW file.
 */
@SuppressWarnings({"unused", "WeakerAccess", "UnnecessaryLocalVariable"})
public class StopWords {

    public final Map<String, Object> mapping = new HashMap<>();

    //=========================================================================

    /**
     * Parse the text representation of the list.
     * @param scanner Text scanner.
     * @return Parsed table.
     */
    public static StopWords parse(@NotNull Scanner scanner) {
        StopWords result = new StopWords();
        while (scanner.hasNext()) {
            String line = Utility.emptyToNull(scanner.nextLine().trim());
            if (line != null) {
                result.mapping.put(line, null);
            }
        }

        return result;
    }

    /**
     * Whether given word is stop-word?
     *
     * @param word Word to check.
     * @return True if the word is empty or stop-word.
     */
    public boolean isStopWord(@Nullable String word) {
        if (Utility.isNullOrEmpty(word)) {
            return true;
        }

        word = word.trim();
        if (Utility.isNullOrEmpty(word)) {
            return true;
        }

        return mapping.containsKey(word);
    }

    /**
     * Parse the local file for stop-word list.
     * @param file File to parse.
     * @return Read table.
     * @throws IOException Error during input-output.
     */
    public static StopWords parse(@NotNull File file) throws IOException {
        try(FileInputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                StopWords result = parse(scanner);

                return result;
            }
        }
    }

    /**
     * Read the stop-word list from the server.
     * @param connection Connection to use.
     * @return Read list or null.
     * @throws IOException Error during input-output.
     */
    public static StopWords read(@NotNull IrbisConnection connection,
                                     @NotNull FileSpecification specification)
            throws IOException {
        String text = connection.readTextContent(specification);
        if (isNullOrEmpty(text)) {
            return null;
        }

        Scanner scanner = new Scanner(text);
        return parse(scanner);
    }

}
