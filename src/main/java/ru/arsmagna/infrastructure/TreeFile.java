// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ru.arsmagna.*;

import java.io.*;
import java.util.*;

import static ru.arsmagna.Utility.isNullOrEmpty;

/**
 * TRE-file.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class TreeFile {

    /**
     * Tabulation sign.
     */
    public final static char INDENT = '\u0009';

    //=========================================================================

    @Nullable
    public String fileName;

    /**
     * Root nodes.
     */
    public final Collection<TreeNode> roots = new LinkedList<>();

    //=========================================================================

    @SuppressWarnings("UnnecessaryLocalVariable")
    private static void arrangeLevel(@NotNull List<TreeNode> list, int level) {
        int count = list.size();
        int index = 0;

        while (index < count) {
            int next = arrangeLevel(list, level, index, count);
            index = next;
        }
    }

    private static int arrangeLevel(@NotNull List<TreeNode> list, int level,
                                    int index, int count) {
        int next = index + 1;
        int level2 = level + 1;

        TreeNode parent = list.get(index);
        while (next < count) {
            TreeNode child = list.get(next);
            if (child.level <= level) {
                break;
            }

            if (child.level == level2) {
                parent.children.add(child);
            }

            next++;
        }

        return next;
    }

    private static int countIndent(@NotNull String text) {
        int result = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == INDENT) {
                result++;
            } else {
                break;
            }
        }

        return result;
    }

    //=========================================================================

    /**
     * Добавление элемента нулевого уровня.
     *
     * @param value Текст элемента.
     * @return Созданный и добавленный в дерево элемент.
     */
    public TreeNode addRoot(@NotNull String value) {
        TreeNode result = new TreeNode(value);
        roots.add(result);

        return result;
    }

    @SuppressWarnings("ConstantConditions")
    public static TreeFile parse(@NotNull Scanner scanner) throws IrbisException {
        TreeFile result = new TreeFile();
        if (!scanner.hasNext()) {
            return result;
        }

        List<TreeNode> list = new ArrayList<>();
        int currentLevel = 0;
        String line = scanner.nextLine();
        if (countIndent(line) != 0) {
            throw new IrbisException();
        }
        list.add(new TreeNode(line));

        while (scanner.hasNext()) {
            line = scanner.nextLine();
            int level = countIndent(line);
            if (level > currentLevel + 1) {
                throw new IrbisException();
            }
            currentLevel = level;
            line = line.substring(currentLevel);
            TreeNode node = new TreeNode(line, level);
            list.add(node);
        }

        int maxLevel = 0;
        for (TreeNode node : list) {
            if (node.level > maxLevel) {
                maxLevel = node.level;
            }
        }
        for (int level = 0; level < maxLevel; level++) {
            arrangeLevel(list, level);
        }

        for (TreeNode node : list) {
            if (node.level == 0) {
                result.roots.add(node);
            }
        }

        return result;
    }

    public static TreeFile parse(@NotNull File file) throws IOException, IrbisException {
        try (InputStream stream = new FileInputStream(file)) {
            try (Scanner scanner = new Scanner(stream, IrbisEncoding.ansi().name())) {
                TreeFile result = parse(scanner);
                result.fileName = file.getAbsolutePath();

                return result;
            }
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static TreeFile parse(@NotNull ServerResponse response)
            throws IOException, IrbisException {
        String text = response.readRemainingAnsiText();
        StringReader reader = new StringReader(text);
        Scanner scanner = new Scanner(reader);
        TreeFile result = parse(scanner);

        return result;
    }

    public static TreeFile parse(@NotNull IrbisConnection connection,
                                 @NotNull FileSpecification specification)
            throws IOException, IrbisException {
        TreeFile result;
        String text = connection.readTextContent(specification);
        if (isNullOrEmpty(text)) {
            result = new TreeFile();
        } else {
            StringReader reader = new StringReader(text);
            Scanner scanner = new Scanner(reader);
            result = parse(scanner);
        }

        return result;
    }

    public String toText() {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        write(printWriter);
        printWriter.flush();
        stringWriter.flush();
        String result = stringWriter.toString();

        return result;
    }

    /**
     * Вывод дерева в текстовый поток.
     *
     * @param writer Текстовый поток.
     */
    public void write(@NotNull PrintWriter writer) {
        for (TreeNode root : roots) {
            root.write(writer, 0);
        }
    }

    /**
     * Запись дерева в файл.
     *
     * @param file Файл, в который предполагается записать дерево.
     * @throws IOException Ошибка ввода-вывода.
     */
    public void write(@NotNull File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file, IrbisEncoding.ansi().name())) {
            write(writer);
        }
    }
}
