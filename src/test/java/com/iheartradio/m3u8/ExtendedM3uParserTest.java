package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.TrackData;
import com.iheartradio.m3u8.data.TrackInfo;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ExtendedM3uParserTest {
    @Test
    public void testParse() throws Exception {
        final String url = "http://www.my.song/file1.mp3";
        final String path = "/usr/user1/file2.mp3";

        final String validData =
                        "#EXTM3U\n" +
                        "#EXT-X-VERSION:2\n" +
                        "#EXT-X-TARGETDURATION:60\n" +
                        "#some comment\n" +
                        "#EXTINF:120.0,title 1\n" +
                        url + "\n" +
                        "#EXTINF:100.0,title 2\n" +
                        "\n" +
                        path + "\n" +
                        "\n";

        final List<TrackData> expectedTracks = Arrays.asList(
                new TrackData.Builder().withUrl(url).withTrackInfo(new TrackInfo(120, "title 1")).build(),
                new TrackData.Builder().withPath(path).withTrackInfo(new TrackInfo(100, "title 2")).build());

        final InputStream inputStream = new ByteArrayInputStream(validData.getBytes("utf-8"));
        final Playlist playlist = new ExtendedM3uParser(inputStream, Encoding.UTF_8).parse();

        assertTrue(playlist.isExtended());
        assertEquals(2, playlist.getCompatibilityVersion());
        assertEquals(60, playlist.getMediaPlaylist().getTargetDuration());
        assertEquals(expectedTracks, playlist.getMediaPlaylist().getTracks());
    }
}
