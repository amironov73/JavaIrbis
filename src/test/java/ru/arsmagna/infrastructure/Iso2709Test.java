package ru.arsmagna.infrastructure;

import org.junit.Test;

import ru.arsmagna.IrbisEncoding;
import ru.arsmagna.MarcRecord;
import ru.arsmagna.TestBase;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class Iso2709Test extends TestBase {

    @Test
    public void readRecord_1() throws IOException {
        Charset ansi = IrbisEncoding.ansi();
        File file = getFile("test1.iso");
        try(InputStream stream = new FileInputStream(file)) {
            MarcRecord record = Iso2709.readRecord(stream, ansi);
            assertNotNull(record);
            assertEquals(16, record.fields.size());

            record = Iso2709.readRecord(stream, ansi);
            assertNotNull(record);
            assertEquals(15, record.fields.size());

            int count = 0;
            while (true) {
                record = Iso2709.readRecord(stream, ansi);
                if (record == null) {
                    break;
                }
                count++;
            }
            assertEquals(79, count);
        }
    }
}