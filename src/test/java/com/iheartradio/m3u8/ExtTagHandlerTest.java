package com.iheartradio.m3u8;

import org.junit.Test;

public class ExtTagHandlerTest extends ParserStateHandlerTestCase {
    @Test
    public void testEXTM3U() throws Exception {
        final IExtTagHandler handler = ExtTagHandler.EXTM3U_HANDLER;
        final String tag = Constants.EXTM3U_TAG;
        final String line = "#" + tag;

        assertEquals(tag, handler.getTag());

        handler.handle(line, mParseState);
        assertTrue(mParseState.isExtended());

        assertHandleThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES);
    }

    @Test
    public void testEXT_X_VERSION() throws Exception {
        final IExtTagHandler handler = ExtTagHandler.EXT_X_VERSION_HANDLER;
        final String tag = Constants.EXT_X_VERSION_TAG;
        final String line = "#" + tag + ":2";

        assertEquals(tag, handler.getTag());

        assertHandleThrows(handler, line + ".", ParseExceptionType.BAD_EXT_TAG_FORMAT);

        handler.handle(line, mParseState);
        assertEquals(2, mParseState.getCompatibilityVersion());

        assertHandleThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES);
    }
}
