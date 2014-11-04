package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.EncryptionData;
import com.iheartradio.m3u8.data.EncryptionMethod;

import org.junit.Test;

import java.util.Arrays;

public class MediaPlaylistTagHandlerTest extends ParserStateHandlerTestCase {
    @Test
    public void testEXT_X_TARGETDURATION() throws Exception {
        final IExtTagHandler handler = MediaPlaylistTagHandler.EXT_X_TARGETDURATION;
        final String tag = Constants.EXT_X_TARGETDURATION_TAG;
        final String line = "#" + tag + ":60";

        assertEquals(tag, handler.getTag());

        handler.handle(line, mParseState);
        assertEquals(60, (int) mParseState.getMedia().targetDuration);

        assertHandleThrows(handler, line, ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES);
    }

    @Test
    public void testEXT_X_KEY() throws Exception {
        final IExtTagHandler handler = MediaPlaylistTagHandler.EXT_X_KEY;
        final String tag = Constants.EXT_X_KEY_TAG;
        final String uri = "http://foo.bar.com/";
        final String format = "format";

        final String line = "#" + tag +
                ":METHOD=AES-128" +
                ",URI=\"" + uri + "\"" +
                ",IV=0x1234abcd5678EF90" +
                ",KEYFORMAT=\"" + format + "\"" +
                ",KEYFORMATVERSIONS=\"1/2/3\"";

        assertEquals(tag, handler.getTag());

        handler.handle(line, mParseState);
        EncryptionData encryptionData = mParseState.getMedia().encryptionData;
        assertEquals(EncryptionMethod.AES, encryptionData.getMethod());
        assertEquals(uri, encryptionData.getUri());

        assertEquals(
                Arrays.asList((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 10, (byte) 11, (byte) 12, (byte) 13,
                              (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 14, (byte) 15, (byte) 9, (byte) 0),
                encryptionData.getInitializationVector());

        assertEquals(format, encryptionData.getKeyFormat());
        assertEquals(Arrays.asList(1, 2, 3), encryptionData.getKeyFormatVersions());
    }
}
