// This is an open source non-commercial project. Dear PVS-Studio, please check it.
// PVS-Studio Static Code Analyzer for C, C++ and C#: http://www.viva64.com

package ru.arsmagna.infrastructure;

import org.junit.Test;

import static org.junit.Assert.*;

public class HtmlTextTest {

    @Test
    public void encode_1() {
        assertEquals(null, HtmlText.encode(null));
        assertEquals("", HtmlText.encode(""));
        assertEquals("&quot;Hello&quot;", HtmlText.encode("\"Hello\""));
        assertEquals("&num;5", HtmlText.encode("#5"));
        assertEquals("&amp;5", HtmlText.encode("&5"));
        assertEquals("&apos;Hello&apos;", HtmlText.encode("'Hello'"));
        assertEquals("&lt;10", HtmlText.encode("<10"));
        assertEquals("&gt;10", HtmlText.encode(">10"));
        assertEquals("Hello&nbsp;World", HtmlText.encode("Hello\u00A0World"));
        assertEquals("&cent;10", HtmlText.encode("\u00A210"));
        assertEquals("&pound;10", HtmlText.encode("\u00A310"));
        assertEquals("&yen;10", HtmlText.encode("\u00A510"));
        assertEquals("&sect;10", HtmlText.encode("\u00A710"));
        assertEquals("&copy; by me", HtmlText.encode("\u00A9 by me"));
        assertEquals("Hel&shy;lo", HtmlText.encode("Hel\u00ADlo"));
        assertEquals("&reg; 2017", HtmlText.encode("\u00AE 2017"));
        assertEquals("&euro;10", HtmlText.encode("\u20AC10"));
    }
}