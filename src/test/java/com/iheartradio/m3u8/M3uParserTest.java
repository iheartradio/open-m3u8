package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.TrackData;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class M3uParserTest {
    @Test
    public void testParse() throws Exception {
        final String absolute = "http://www.my.song/file1.mp3";
        final String relative = "user1/file2.mp3";

        final String validData =
                        "#some comment\n" +
                        absolute + "\n" +
                        "\n" +
                        relative + "\n" +
                        "\n";

        final List<TrackData> expectedTracks = Arrays.asList(
                new TrackData.Builder().withUri(absolute).build(),
                new TrackData.Builder().withUri(relative).build());

        final InputStream inputStream = new ByteArrayInputStream(validData.getBytes("utf-8"));
        final MediaPlaylist mediaPlaylist = new M3uParser(inputStream, Encoding.UTF_8).parse().getMediaPlaylist();

        assertEquals(expectedTracks, mediaPlaylist.getTracks());
    }
}
