package ru.arsmagna.infrastructure;

import org.junit.Test;

import ru.arsmagna.IrbisException;
import ru.arsmagna.TestBase;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static ru.arsmagna.IrbisText.fromDosToUnix;

public class TreeFileTest extends TestBase {

    @Test
    public void addRoot_1() {
        TreeFile tree = new TreeFile();
        TreeNode root1 = tree.addRoot("root1");
        root1.add("child11");
        root1.add("child12");
        TreeNode root2 = tree.addRoot("root2");
        root2.add("child21");
        root2.add("child22");

        String actual = fromDosToUnix(tree.toText());
        String expected = "root1\n\tchild11\n\tchild12\nroot2\n\tchild21\n\tchild22\n";
        assertEquals(expected, actual);
    }

    @Test
    public void parse_1() throws IOException, IrbisException {
        File file = getFile("ii.tre");
        TreeFile treeFile = TreeFile.parse(file);
        assertEquals(4, treeFile.roots.size());
    }
}