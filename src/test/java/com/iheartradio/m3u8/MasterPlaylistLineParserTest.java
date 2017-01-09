package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.MediaType;
import com.iheartradio.m3u8.data.Resolution;
import com.iheartradio.m3u8.data.StreamInfo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MasterPlaylistLineParserTest extends LineParserStateTestCase {
    @Test
    public void testEXT_X_MEDIA() throws Exception {
        final List<MediaData> expectedMediaData = new ArrayList<MediaData>();
        final IExtTagParser handler = MasterPlaylistLineParser.EXT_X_MEDIA;
        final String tag = Constants.EXT_X_MEDIA_TAG;
        final String groupId = "1234";
        final String language = "lang";
        final String associatedLanguage = "assoc-lang";
        final String name = "Foo";
        final String inStreamId = "SERVICE1";

        expectedMediaData.add(new MediaData.Builder()
                .withType(MediaType.CLOSED_CAPTIONS)
                .withGroupId(groupId)
                .withLanguage(language)
                .withAssociatedLanguage(associatedLanguage)
                .withName(name)
                .withAutoSelect(true)
                .withInStreamId(inStreamId)
                .withCharacteristics(Arrays.asList("char1", "char2"))
                .build());

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

        handler.parse(line, mParseState);
        assertEquals(expectedMediaData, mParseState.getMaster().mediaData);
    }

    @Test
    public void testEXT_X_STREAM_INF() throws Exception {
        final IExtTagParser handler = MasterPlaylistLineParser.EXT_X_STREAM_INF;
        final String tag = Constants.EXT_X_STREAM_INF_TAG;
        final int bandwidth = 10000;
        final int averageBandwidth = 5000;
        final List<String> codecs = Arrays.asList("h.263", "h.264");
        final Resolution resolution = new Resolution(800, 600);
        final String audio = "foo";
        final String video = "bar";
        final String subtitles = "titles";
        final String closedCaptions = "captions";

        final StreamInfo expectedStreamInfo = new StreamInfo.Builder()
                .withBandwidth(bandwidth)
                .withAverageBandwidth(averageBandwidth)
                .withCodecs(codecs)
                .withResolution(resolution)
                .withAudio(audio)
                .withVideo(video)
                .withSubtitles(subtitles)
                .withClosedCaptions(closedCaptions)
                .build();

        final String line = "#" + tag +
                ":BANDWIDTH=" + bandwidth +
                ",AVERAGE-BANDWIDTH=" + averageBandwidth +
                ",CODECS=\"" + codecs.get(0) + "," + codecs.get(1) + "\"" +
                ",RESOLUTION=" + resolution.width + "x" + resolution.height +
                ",AUDIO=\"" + audio + "\"" +
                ",VIDEO=\"" + video + "\"" +
                ",SUBTITLES=\"" + subtitles + "\"" +
                ",CLOSED-CAPTIONS=\"" + closedCaptions + "\"";

        assertEquals(tag, handler.getTag());

        handler.parse(line, mParseState);
        assertEquals(expectedStreamInfo, mParseState.getMaster().streamInfo);
    }
}
