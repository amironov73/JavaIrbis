package ru.arsmagna.infrastructure;

import org.junit.Test;

import ru.arsmagna.IrbisEncoding;
import ru.arsmagna.MarcRecord;
import ru.arsmagna.RecordField;
import ru.arsmagna.TestBase;

import java.io.*;
import java.nio.charset.Charset;

import static org.junit.Assert.*;

public class Iso2709Test extends TestBase {

    private MarcRecord getRecord() {
        MarcRecord result = new MarcRecord();

        RecordField field = new RecordField(1);
        field.value = "ISTU/IBIS/123456790";
        result.fields.add(field);

        field = new RecordField(700);
        field.add('a', "Иванов");
        field.add('b', "И. И.");
        result.fields.add(field);

        field = new RecordField(701);
        field.add('a', "Петров");
        field.add('b', "П. П.");
        result.fields.add(field);

        field = new RecordField(200);
        field.add('a', "Заглавие");
        field.add('e', "подзаголовочное");
        field.add('f', "И. И. Иванов, П. П. Петров");
        result.fields.add(field);

        field = new RecordField(300, "Первое примечание");
        result.fields.add(field);
        field = new RecordField(300, "Второе примечание");
        result.fields.add(field);
        field = new RecordField(300, "Третье примечание");
        result.fields.add(field);

        return result;
    }

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

    @Test
    public void writeRecord_1() throws IOException {
        Charset ansi = IrbisEncoding.ansi();
        File file = File.createTempFile("record", ".iso");
        try(OutputStream stream = new FileOutputStream(file)) {
            MarcRecord record = getRecord();
            Iso2709.writeRecord(stream, record, ansi);
        }
    }
}