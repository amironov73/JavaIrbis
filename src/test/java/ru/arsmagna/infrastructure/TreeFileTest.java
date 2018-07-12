package ru.arsmagna.infrastructure;

import org.junit.Test;

import ru.arsmagna.IrbisException;
import ru.arsmagna.TestBase;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class TreeFileTest extends TestBase {

    @Test
    public void parse_1() throws IOException, IrbisException {
        File file = getFile("ii.tre");
        TreeFile treeFile = TreeFile.parse(file);
        assertEquals(4, treeFile.roots.size());
    }
}