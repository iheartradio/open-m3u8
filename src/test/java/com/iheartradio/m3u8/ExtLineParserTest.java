package com.iheartradio.m3u8;

import org.junit.Test;

public class ExtLineParserTest extends LineParserStateTestCase {
    @Test
    public void testEXTM3U() throws Exception {
        final IExtTagParser handler = ExtLineParser.EXTM3U_HANDLER;
        final String tag = Constants.EXTM3U_TAG;
        final String line = "#" + tag;

        assertEquals(tag, handler.getTag());

        handler.parse(line, mParseState);
        assertTrue(mParseState.isExtended());

        assertParseThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES);
    }

    @Test
    public void testEXT_X_VERSION() throws Exception {
        final IExtTagParser handler = ExtLineParser.EXT_X_VERSION_HANDLER;
        final String tag = Constants.EXT_X_VERSION_TAG;
        final String line = "#" + tag + ":2";

        assertEquals(tag, handler.getTag());

        assertParseThrows(handler, line + ".", ParseExceptionType.BAD_EXT_TAG_FORMAT);

        handler.parse(line, mParseState);
        assertEquals(2, mParseState.getCompatibilityVersion());

        assertParseThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES);
    }

    @Test
    public void testEXT_X_START() throws Exception {
        final IExtTagParser parser = ExtLineParser.EXT_X_START;
        final String tag = Constants.EXT_X_START_TAG;
        final String line = "#" + tag +
                ":TIME-OFFSET=-4.5" +
                ",PRECISE=YES";

        assertEquals(tag, parser.getTag());
        assertParseThrows(parser, line + ".", ParseExceptionType.NOT_YES_OR_NO);

        parser.parse(line, mParseState);
        assertEquals(-4.5, mParseState.startData.getTimeOffset(), 1e-12);
        assertEquals(true, mParseState.startData.isPrecise());

        assertParseThrows(parser, line, ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES);
    }
}
