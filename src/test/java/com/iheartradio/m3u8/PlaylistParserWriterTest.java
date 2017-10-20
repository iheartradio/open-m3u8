package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class PlaylistParserWriterTest {
    Playlist readPlaylist(String fileName) throws IOException, ParseException, PlaylistException {
        assertNotNull(fileName);
        
        try(InputStream is = new FileInputStream("src/test/resources/" + fileName)) {
            Playlist playlist = new PlaylistParser(is, Format.EXT_M3U, Encoding.UTF_8).parse();
            return playlist;
        }
    }
    
    String writePlaylist(Playlist playlist) throws IOException, ParseException, PlaylistException {
        assertNotNull(playlist);
        
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PlaylistWriter writer = new PlaylistWriter(os, Format.EXT_M3U, Encoding.UTF_8);
            writer.write(playlist);
            
            return os.toString(Encoding.UTF_8.value);
        }
    }
    
    @Test
    public void simpleMediaPlaylist() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("simpleMediaPlaylist.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }

    @Test
    public void liveMediaPlaylist() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("liveMediaPlaylist.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }

    @Test
    public void playlistWithEncryptedMediaSegments() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("playlistWithEncryptedMediaSegments.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void masterPlaylist() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("masterPlaylist.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void masterPlaylistWithIFrames() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("masterPlaylistWithIFrames.m3u8");
        assertTrue(playlist.hasMasterPlaylist());
        
        MasterPlaylist masterPlaylist = playlist.getMasterPlaylist();
        assertNotNull(masterPlaylist);
        
        List<PlaylistData> playlistDatas = masterPlaylist.getPlaylists();
        List<IFrameStreamInfo> iFrameInfo = masterPlaylist.getIFramePlaylists();
        assertNotNull(playlistDatas);
        assertNotNull(iFrameInfo);
        assertEquals(4, playlistDatas.size());
        assertEquals(3, iFrameInfo.size());

        PlaylistData lowXStreamInf = playlistDatas.get(0);
        assertNotNull(lowXStreamInf);
        assertNotNull(lowXStreamInf.getStreamInfo());
        assertEquals(1280000, lowXStreamInf.getStreamInfo().getBandwidth());
        assertEquals("low/audio-video.m3u8", lowXStreamInf.getUri());

        PlaylistData midXStreamInf = playlistDatas.get(1);
        assertNotNull(midXStreamInf);
        assertNotNull(midXStreamInf.getStreamInfo());
        assertEquals(2560000, midXStreamInf.getStreamInfo().getBandwidth());
        assertEquals("mid/audio-video.m3u8", midXStreamInf.getUri());

        PlaylistData hiXStreamInf = playlistDatas.get(2);
        assertNotNull(hiXStreamInf);
        assertNotNull(hiXStreamInf.getStreamInfo());
        assertEquals(7680000, hiXStreamInf.getStreamInfo().getBandwidth());
        assertEquals("hi/audio-video.m3u8", hiXStreamInf.getUri());

        PlaylistData audioXStreamInf = playlistDatas.get(3);
        assertNotNull(audioXStreamInf);
        assertNotNull(audioXStreamInf.getStreamInfo());
        assertEquals(65000, audioXStreamInf.getStreamInfo().getBandwidth());
        assertNotNull(audioXStreamInf.getStreamInfo().getCodecs());
        assertEquals(1, audioXStreamInf.getStreamInfo().getCodecs().size());
        assertEquals("mp4a.40.5", audioXStreamInf.getStreamInfo().getCodecs().get(0));
        assertEquals("audio-only.m3u8", audioXStreamInf.getUri());

        IFrameStreamInfo lowXIFrameStreamInf = iFrameInfo.get(0);
        assertNotNull(lowXIFrameStreamInf);
        assertEquals(86000, lowXIFrameStreamInf.getBandwidth());
        assertEquals("low/iframe.m3u8", lowXIFrameStreamInf.getUri());

        IFrameStreamInfo midXIFrameStreamInf = iFrameInfo.get(1);
        assertNotNull(midXIFrameStreamInf);
        assertEquals(150000, midXIFrameStreamInf.getBandwidth());
        assertEquals("mid/iframe.m3u8", midXIFrameStreamInf.getUri());

        IFrameStreamInfo hiXIFrameStreamInf = iFrameInfo.get(2);
        assertNotNull(hiXIFrameStreamInf);
        assertEquals(550000, hiXIFrameStreamInf.getBandwidth());
        assertEquals("hi/iframe.m3u8", hiXIFrameStreamInf.getUri());
        
        String writtenPlaylist = writePlaylist(playlist);
        assertEquals(
                "#EXTM3U\n" +
                "#EXT-X-VERSION:1\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=1280000\n" +
                "low/audio-video.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=2560000\n" +
                "mid/audio-video.m3u8\n" +
                "#EXT-X-STREAM-INF:BANDWIDTH=7680000\n" +
                "hi/audio-video.m3u8\n" +
                "#EXT-X-STREAM-INF:CODECS=\"mp4a.40.5\",BANDWIDTH=65000\n" +
                "audio-only.m3u8\n" +
                "#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=86000,URI=\"low/iframe.m3u8\"\n" +
                "#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=150000,URI=\"mid/iframe.m3u8\"\n" +
                "#EXT-X-I-FRAME-STREAM-INF:BANDWIDTH=550000,URI=\"hi/iframe.m3u8\"\n",
                writtenPlaylist);
    }

    @Test
    public void masterPlaylistWithAlternativeAudio() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("masterPlaylistWithAlternativeAudio.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void masterPlaylistWithAlternativeVideo() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("masterPlaylistWithAlternativeVideo.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void discontinutyPlaylist() throws IOException, ParseException, PlaylistException {
        Playlist playlist = readPlaylist("withDiscontinuity.m3u8");
        String sPlaylist = writePlaylist(playlist);
        System.out.println("***************");
        System.out.println(sPlaylist);
    }

    @Test
    public void playlistWithByteRanges() throws Exception {
        final Playlist playlist = TestUtil.parsePlaylistFromResource("mediaPlaylistWithByteRanges.m3u8");
        final MediaPlaylist mediaPlaylist = playlist.getMediaPlaylist();
        List<ByteRange> byteRanges = new ArrayList<>();
        for (TrackData track : mediaPlaylist.getTracks()) {
            assertTrue(track.hasByteRange());
            byteRanges.add(track.getByteRange());
        }

        List<ByteRange> expected = Arrays.asList(
                new ByteRange(0, 10),
                new ByteRange(20),
                new ByteRange(30)
        );

        assertEquals(expected, byteRanges);

        assertEquals(
                "#EXTM3U\n" +
                "#EXT-X-VERSION:4\n" +
                "#EXT-X-TARGETDURATION:10\n" +
                "#EXT-X-MEDIA-SEQUENCE:0\n"+
                "#EXT-X-BYTERANGE:0@10\n" +
                "#EXTINF:9.009\n" +
                "http://media.example.com/first.ts\n" +
                "#EXT-X-BYTERANGE:20\n" +
                "#EXTINF:9.009\n" +
                "http://media.example.com/first.ts\n" +
                "#EXT-X-BYTERANGE:30\n" +
                "#EXTINF:3.003\n" +
                "http://media.example.com/first.ts\n" +
                "#EXT-X-ENDLIST\n", writePlaylist(playlist));
    }
}
