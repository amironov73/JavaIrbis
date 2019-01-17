// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProtocolTextTest {

    @Test
    public void encodeSubField_1() {
        StringBuilder builder = new StringBuilder();
        SubField subField = new SubField('a', "Some text");
        ProtocolText.encodeSubField(builder, subField);
        assertEquals("^aSome text", builder.toString());
    }

    @Test
    public void encodeField_1() {
        StringBuilder builder = new StringBuilder();
        RecordField field = new RecordField(100, "Some value");
        ProtocolText.encodeField(builder, field);
        assertEquals("100#Some value", builder.toString());

        field.add('a', "Subfield A")
                .add('b', "Subfield B");
        builder = new StringBuilder();
        ProtocolText.encodeField(builder, field);
        assertEquals("100#Some value^aSubfield A^bSubfield B",
                builder.toString());
    }

    @Test
    public void encodeRecord_1() {
        MarcRecord record = new MarcRecord();
        record.mfn = 123;
        record.addField(100, "Field 100");
        RecordField field = new RecordField(200);
        field.add('a', "Subfield A")
            .add('b', "Subfield B");
        record.fields.add(field);
        record.addField(300, "Field 300");
        String expected = "123#0\u001F\u001E0#0\u001F\u001E"
            + "100#Field 100\u001F\u001E200#^aSubfield A"
            + "^bSubfield B\u001F\u001E300#Field 300\u001F\u001E";
        String actual = ProtocolText.encodeRecord(record);
        assertEquals(expected, actual);
    }
}