package ru.arsmagna.infrastructure;

import org.junit.Ignore;
import org.junit.Test;

import ru.arsmagna.IrbisException;
import ru.arsmagna.TestBase;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class OptFileTest extends TestBase {

    @Test
    public void parse_1() throws IOException, IrbisException {
        File file = getFile("ws31.opt");
        OptFile optFile = OptFile.parse(file);
        //assertEquals("!NJ31", optFile.resolveWorksheet("NJ"));
    }
}