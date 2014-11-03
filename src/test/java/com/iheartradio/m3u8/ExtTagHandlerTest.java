package com.iheartradio.m3u8;

import junit.framework.TestCase;

import org.junit.Test;

public class ExtTagHandlerTest extends TestCase {
    private ParseState mParseState;

    @Override
    protected void setUp() throws Exception {
        mParseState = new ParseState();
    }

    @Test
    public void testEXTM3U() throws Exception {
        final IExtTagHandler handler = ExtTagHandler.EXTM3U_HANDLER;
        final String tag = Constants.EXTM3U_TAG;
        final String line = "#" + tag;

        assertEquals(tag, handler.getTag());

        handler.handle(line, mParseState);
        assertTrue(mParseState.isExtended());

        assertHandleThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAGS);
    }

    @Test
    public void testEXT_X_VERSION() throws Exception {
        final IExtTagHandler handler = ExtTagHandler.EXT_X_VERSION_HANDLER;
        final String tag = Constants.EXT_X_VERSION_TAG;
        final String line = "#" + tag + ":3";

        assertEquals(tag, handler.getTag());

        assertHandleThrows(handler, line + ".", ParseExceptionType.BAD_EXT_TAG_FORMAT);

        handler.handle(line, mParseState);
        assertEquals(3, (int) mParseState.getCompatibilityVersion());

        assertHandleThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAGS);
    }

    private void assertHandleThrows(IExtTagHandler handler, String line, ParseExceptionType exceptionType) {
        try {
            handler.handle(line, mParseState);
            assertFalse(true);
        } catch (ParseException exception) {
            assertEquals(exceptionType, exception.type);
        }
    }
}
