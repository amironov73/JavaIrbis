// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IrbisFormatTest {

    private static void testComments(String source, String expected) {
        String actual = IrbisFormat.removeComments(source);
        assertEquals(expected, actual);
    }

    @Test
    public void removeComments_1() {
        testComments("", "");
        testComments(" ", " ");
        testComments("v100,/,v200", "v100,/,v200");
        testComments("\tv100\r\n", "\tv100\r\n");
        testComments
                (
                        "v100/* Comment\r\nv200",
                        "v100\r\nv200"
                );
        testComments
                (
                        "v100, '/* Not comment', v200",
                        "v100, '/* Not comment', v200"
                );
        testComments
                (
                        "v100, |/* Not comment|, v200",
                        "v100, |/* Not comment|, v200"
                );
        testComments
                (
                        "v100, \"/* Not comment\", v200",
                        "v100, \"/* Not comment\", v200"
                );
        testComments
                (
                        "v100, '/*not comment' v200, /*comment\r\nv300",
                        "v100, '/*not comment' v200, \r\nv300"
                );
        testComments
                (
                        "v100, '/*not comment' v200, /,\r\nv300",
                        "v100, '/*not comment' v200, /,\r\nv300"
                );
    }

    private static void testPrepare(String source, String expected) {
        String actual = IrbisFormat.prepareFormat(source);
        assertEquals(expected, actual);
    }

    @Test
    public void prepareFormat_1() {
        testPrepare("", "");
        testPrepare(" ", " ");
        testPrepare("\r\n", "");
        testPrepare("v100,/,v200", "v100,/,v200");
        testPrepare("\tv100\r\n", "v100");
        testPrepare
                (
                        "v100/*comment\r\nv200",
                        "v100v200"
                );
        testPrepare
                (
                        "v100 '\t'\r\nv200",
                        "v100 ''v200"
                );
    }
}