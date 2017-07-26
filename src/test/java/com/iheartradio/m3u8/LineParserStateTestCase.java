package com.iheartradio.m3u8;

import junit.framework.TestCase;

import org.junit.Test;

public class LineParserStateTestCase extends TestCase {
    protected ParseState mParseState;

    @Override
    protected void setUp() throws Exception {
        mParseState = new ParseState(Encoding.UTF_8);
    }

    protected void assertParseThrows(IExtTagParser handler, String line, ParseExceptionType exceptionType) {
        try {
            handler.parse(line, mParseState);
            assertFalse(true);
        } catch (ParseException exception) {
            assertEquals(exceptionType, exception.type);
        }
    }

    @Test
    public void test() {
        // workaround for no tests found warning
    }
}
