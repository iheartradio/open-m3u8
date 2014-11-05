package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.MediaType;

import org.junit.Test;

import java.util.Arrays;

public class MasterPlaylistTagHandlerTest extends ParserStateHandlerTestCase {
    @Test
    public void testEXT_X_MEDIA() throws Exception {
        final IExtTagHandler handler = MasterPlaylistTagHandler.EXT_X_MEDIA;
        final String tag = Constants.EXT_X_MEDIA_TAG;
        final String groupId = "1234";
        final String language = "lang";
        final String associatedLanguage = "assoc-lang";
        final String name = "Foo";
        final String inStreamId = "SERVICE1";

        final String line = "#" + tag +
                ":TYPE=CLOSED-CAPTIONS" +
                ",GROUP-ID=\"" + groupId + "\"" +
                ",LANGUAGE=\"" + language + "\"" +
                ",ASSOC-LANGUAGE=\"" + associatedLanguage + "\"" +
                ",NAME=\"" + name + "\"" +
                ",DEFAULT=NO" +
                ",AUTOSELECT=YES" +
                ",INSTREAM-ID=\"" + inStreamId + "\"" +
                ",CHARACTERISTICS=\"char1,char2\"";

        assertEquals(tag, handler.getTag());

        handler.handle(line, mParseState);
        MediaData mediaData = mParseState.getMaster().mediaData;
        assertEquals(MediaType.CLOSED_CAPTIONS, mediaData.getType());
        assertEquals(null, mediaData.getUri());
        assertEquals(groupId, mediaData.getGroupId());
        assertEquals(language, mediaData.getLanguage());
        assertEquals(associatedLanguage, mediaData.getAssociatedLanguage());
        assertEquals(name, mediaData.getName());
        assertEquals(false, mediaData.isDefault());
        assertEquals(true, mediaData.isAutoSelect());
        assertEquals(false, mediaData.isForced());
        assertEquals(inStreamId, mediaData.getInStreamId());
        assertEquals(Arrays.asList("char1", "char2"), mediaData.getCharacteristics());
    }
}
