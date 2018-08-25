package ru.arsmagna.infrastructure;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import ru.arsmagna.*;

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
    public void writeRecord_1() throws IOException, IrbisException {
        Charset ansi = IrbisEncoding.ansi();
        File file = File.createTempFile("record", ".iso");
        try(OutputStream stream = new FileOutputStream(file)) {
            MarcRecord record = getRecord();
            Iso2709.writeRecord(stream, record, ansi);
        }
    }

    @Test
    public void writeRecord_2() throws IOException, IrbisException {
        Charset utf = IrbisEncoding.utf();
        File file = File.createTempFile("record", ".iso");
        try(OutputStream stream = new FileOutputStream(file)) {
            MarcRecord record = getRecord();
            Iso2709.writeRecord(stream, record, utf);
        }
    }

    private void copyRecords(@NotNull File inputFile,
                             @NotNull File outputFile,
                             @NotNull Charset encoding)
        throws IOException, IrbisException {
        try(InputStream inputStream = new FileInputStream(inputFile)) {
            try(OutputStream outputStream = new FileOutputStream(outputFile)) {
                MarcRecord record;
                while ((record = Iso2709.readRecord(inputStream, encoding)) != null) {
                    Iso2709.writeRecord(outputStream, record, encoding);
                }
            }
        }
    }

    @Test
    public void roundTrip_1() throws IOException, IrbisException {
        Charset ansi = IrbisEncoding.ansi();
        File originalFile = getFile("test1.iso");
        File firstFile = File.createTempFile("first", ".iso");
        File secondFile = File.createTempFile("second", ".iso");
        copyRecords(originalFile, firstFile, ansi);
        copyRecords(firstFile, secondFile, ansi);

        assertEquals(originalFile.length(), firstFile.length());
        assertEquals(firstFile.length(), secondFile.length());

        byte[] expected = Utility.readAllBytes(firstFile);
        byte[] actual = Utility.readAllBytes(secondFile);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void roundTrip_2() throws IOException, IrbisException {
        Charset ansi = IrbisEncoding.ansi();
        File firstFile = File.createTempFile("first", ".iso");
        File secondFile = File.createTempFile("second", ".iso");
        try(OutputStream stream = new FileOutputStream(firstFile)) {
            MarcRecord record = getRecord();
            Iso2709.writeRecord(stream, record, ansi);
        }

        copyRecords(firstFile, secondFile, ansi);

        byte[] expected = Utility.readAllBytes(firstFile);
        byte[] actual = Utility.readAllBytes(secondFile);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void roundTrip_3() throws IOException, IrbisException {
        Charset utf = IrbisEncoding.utf();
        File firstFile = File.createTempFile("first", ".iso");
        File secondFile = File.createTempFile("second", ".iso");
        try(OutputStream stream = new FileOutputStream(firstFile)) {
            MarcRecord record = getRecord();
            Iso2709.writeRecord(stream, record, utf);
        }

        copyRecords(firstFile, secondFile, utf);

        byte[] expected = Utility.readAllBytes(firstFile);
        byte[] actual = Utility.readAllBytes(secondFile);
        assertArrayEquals(expected, actual);
    }
}