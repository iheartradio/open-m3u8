package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaData;
import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.MediaType;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.StreamInfo;
import com.iheartradio.m3u8.data.TrackData;
import com.iheartradio.m3u8.data.TrackInfo;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.iheartradio.m3u8.TestUtil.inputStreamFromResource;
import static org.junit.Assert.*;

public class ExtendedM3uParserTest {
    @Test
    public void testParseMaster() throws Exception {
        final List<MediaData> expectedMediaData = new ArrayList<MediaData>();

        expectedMediaData.add(new MediaData.Builder()
                .withType(MediaType.AUDIO)
                .withGroupId("1234")
                .withName("Foo")
                .build());

        final StreamInfo expectedStreamInfo = new StreamInfo.Builder()
                .withBandwidth(500)
                .build();

        final String validData =
                "#EXTM3U\n" +
                        "#EXT-X-VERSION:2\n" +
                        "#EXT-X-MEDIA:TYPE=AUDIO,GROUP-ID=\"1234\",NAME=\"Foo\"\n" +
                        "#EXT-X-STREAM-INF:BANDWIDTH=500\n" +
                        "http://foo.bar.com/\n" +
                        "\n";

        final InputStream inputStream = new ByteArrayInputStream(validData.getBytes("utf-8"));
        final ExtendedM3uParser parser = new ExtendedM3uParser(inputStream, Encoding.UTF_8, ParsingMode.STRICT);

        assertTrue(parser.isAvailable());

        final Playlist playlist = parser.parse();

        assertFalse(parser.isAvailable());
        assertTrue(playlist.isExtended());
        assertEquals(2, playlist.getCompatibilityVersion());
        assertTrue(playlist.hasMasterPlaylist());
        assertEquals(expectedMediaData, playlist.getMasterPlaylist().getMediaData());
        assertEquals(expectedStreamInfo, playlist.getMasterPlaylist().getPlaylists().get(0).getStreamInfo());
    }

    @Test
    public void testLenientParsing() throws Exception {
        final String validData =
                "#EXTM3U\n" +
                        "#EXT-X-VERSION:2\n" +
                        "#EXT-X-TARGETDURATION:60\n" +
                        "#EXT-X-MEDIA-SEQUENCE:10\n" +
                        "#EXT-FAXS-CM:MIIa4QYJKoZIhvcNAQcCoIIa0jCCGs4C...\n" +
                        "#some comment\n" +
                        "#EXTINF:120.0,title 1\n" +
                        "http://www.my.song/file1.mp3\n" +
                        "\n";

        final InputStream inputStream = new ByteArrayInputStream(validData.getBytes("utf-8"));
        final Playlist playlist = new ExtendedM3uParser(inputStream, Encoding.UTF_8, ParsingMode.LENIENT).parse();

        assertTrue(playlist.isExtended());
        assertTrue(playlist.getMediaPlaylist().hasUnknownTags());
        assertTrue(playlist.getMediaPlaylist().getUnknownTags().get(0).length() > 0);
    }

    @Test
    public void testParseMedia() throws Exception {
        final String absolute = "http://www.my.song/file1.mp3";
        final String relative = "user1/file2.mp3";

        final String validData =
                "#EXTM3U\n" +
                        "#EXT-X-VERSION:2\n" +
                        "#EXT-X-TARGETDURATION:60\n" +
                        "#EXT-X-MEDIA-SEQUENCE:10\n" +
                        "#some comment\n" +
                        "#EXTINF:120.0,title 1\n" +
                        absolute + "\n" +
                        "#EXTINF:100.0,title 2\n" +
                        "#EXT-X-PROGRAM-DATE-TIME:2010-02-19T14:54:23.031+08:00\n" +
                        "\n" +
                        relative + "\n" +
                        "\n";

        final List<TrackData> expectedTracks = Arrays.asList(
                new TrackData.Builder().withUri(absolute).withTrackInfo(new TrackInfo(120, "title 1")).build(),
                new TrackData.Builder().withUri(relative).withTrackInfo(new TrackInfo(100, "title 2")).withProgramDateTime("2010-02-19T14:54:23.031+08:00").build());

        final InputStream inputStream = new ByteArrayInputStream(validData.getBytes("utf-8"));
        final Playlist playlist = new ExtendedM3uParser(inputStream, Encoding.UTF_8, ParsingMode.STRICT).parse();

        assertTrue(playlist.isExtended());
        assertEquals(2, playlist.getCompatibilityVersion());
        assertTrue(playlist.hasMediaPlaylist());
        assertEquals(60, playlist.getMediaPlaylist().getTargetDuration());
        assertEquals(10, playlist.getMediaPlaylist().getMediaSequenceNumber());
        assertEquals(expectedTracks, playlist.getMediaPlaylist().getTracks());
    }

    @Test
    public void testParsingMultiplePlaylists() throws Exception {
        try (final InputStream inputStream = inputStreamFromResource("twoMediaPlaylists.m3u8")) {
            final PlaylistParser parser = new PlaylistParser(inputStream, Format.EXT_M3U, Encoding.UTF_8);

            assertTrue(parser.isAvailable());

            final Playlist playlist1 = parser.parse();

            assertTrue(parser.isAvailable());

            final Playlist playlist2 = parser.parse();

            assertFalse(parser.isAvailable());

            List<TrackData> expected1 = Arrays.asList(
                    makeTrackData("http://media.example.com/first.ts", 9.009f),
                    makeTrackData("http://media.example.com/second.ts", 9.009f),
                    makeTrackData("http://media.example.com/third.ts", 3.003f));

            assertEquals(
                    expected1,
                    playlist1.getMediaPlaylist().getTracks());

            assertEquals(
                    Arrays.asList(
                            makeTrackData("http://media.example.com/fourth.ts", 9.01f),
                            makeTrackData("http://media.example.com/fifth.ts", 9.011f)),
                    playlist2.getMediaPlaylist().getTracks());

            assertEquals(0, inputStream.available());
        }
    }

    @Test
    public void testParseDiscontinuity() throws Exception {
        final String absolute = "http://www.my.song/file1.mp3";
        final String relative = "user1/file2.mp3";

        final String validData =
                "#EXTM3U\n" +
                        "#EXT-X-VERSION:2\n" +
                        "#EXT-X-TARGETDURATION:60\n" +
                        "#EXT-X-MEDIA-SEQUENCE:10\n" +
                        "#some comment\n" +
                        "#EXTINF:120.0,title 1\n" +
                        absolute + "\n" +
                        "#EXT-X-DISCONTINUITY\n" +
                        "#EXTINF:100.0,title 2\n" +
                        "\n" +
                        relative + "\n" +
                        "\n";

        final InputStream inputStream = new ByteArrayInputStream(validData.getBytes("utf-8"));
        final MediaPlaylist mediaPlaylist = new ExtendedM3uParser(inputStream, Encoding.UTF_8, ParsingMode.STRICT).parse().getMediaPlaylist();

        assertFalse(mediaPlaylist.getTracks().get(0).hasDiscontinuity());
        assertTrue(mediaPlaylist.getTracks().get(1).hasDiscontinuity());
        assertEquals(0, mediaPlaylist.getDiscontinuitySequenceNumber(0));
        assertEquals(1, mediaPlaylist.getDiscontinuitySequenceNumber(1));
    }

    private static TrackData makeTrackData(String uri, float duration) {
        return new TrackData.Builder()
                .withTrackInfo(new TrackInfo(duration, null))
                .withUri(uri)
                .build();
    }
}
