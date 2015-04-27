package com.iheartradio.m3u8;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.iheartradio.m3u8.data.MasterPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import com.iheartradio.m3u8.data.PlaylistData;

import static org.junit.Assert.*;

public class PlaylistParserWriterTest {

    private PlaylistWriter writer;

    @Before
    public void setUp() {
        writer = new PlaylistWriter();
    }
    
    Playlist readPlaylist(String fileName) throws IOException, ParseException {
        assertNotNull(fileName);
        
        try(InputStream is = new FileInputStream("src/test/resources/" + fileName)) {
            Playlist playlist = new PlaylistParser(is, Format.EXT_M3U, Encoding.UTF_8).parse();
            return playlist;
        }
    }
    
    String writePlaylist(Playlist playlist) throws IOException, ParseException {
        assertNotNull(playlist);
        
        try(ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            writer.write(os, playlist, Format.EXT_M3U, Encoding.UTF_8);
            
            return os.toString(Encoding.UTF_8.getValue());
        }
    }
    
    @Test
    public void simpleMediaPlaylist() throws IOException, ParseException {
        Playlist playlist = readPlaylist("simpleMediaPlaylist.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }

    @Test
    public void liveMediaPlaylist() throws IOException, ParseException {
        Playlist playlist = readPlaylist("liveMediaPlaylist.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }

    @Test
    public void playlistWithEncryptedMediaSegments() throws IOException, ParseException {
        Playlist playlist = readPlaylist("playlistWithEncryptedMediaSegments.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void masterPlaylist() throws IOException, ParseException {
        Playlist playlist = readPlaylist("masterPlaylist.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void masterPlaylistWithIFrames() throws IOException, ParseException {
        Playlist playlist = readPlaylist("masterPlaylistWithIFrames.m3u8");
        assertTrue(playlist.hasMasterPlaylist());
        
        MasterPlaylist masterPlaylist = playlist.getMasterPlaylist();
        assertNotNull(masterPlaylist);
        
        List<PlaylistData> playlistDatas = masterPlaylist.getPlaylists();
        assertNotNull(playlistDatas);
        assertEquals(7, playlistDatas.size());
        
        PlaylistData lowXStreamInf = playlistDatas.get(0);
        assertNotNull(lowXStreamInf);
        assertNotNull(lowXStreamInf.getStreamInfo());
        assertEquals(1280000, lowXStreamInf.getStreamInfo().getBandwidth());
        assertEquals("low/audio-video.m3u8", lowXStreamInf.getLocation());
        assertNull(lowXStreamInf.getStreamInfo().getUri());
        
        PlaylistData lowXIFrameStreamInf = playlistDatas.get(1);
        assertNotNull(lowXIFrameStreamInf);
        assertNotNull(lowXIFrameStreamInf.getStreamInfo());
        assertEquals(86000, lowXIFrameStreamInf.getStreamInfo().getBandwidth());
        assertEquals("low/iframe.m3u8", lowXIFrameStreamInf.getStreamInfo().getUri());

        PlaylistData midXStreamInf = playlistDatas.get(2);
        assertNotNull(midXStreamInf);
        assertNotNull(midXStreamInf.getStreamInfo());
        assertEquals(2560000, midXStreamInf.getStreamInfo().getBandwidth());
        assertEquals("mid/audio-video.m3u8", midXStreamInf.getLocation());
        assertNull(midXStreamInf.getStreamInfo().getUri());

        PlaylistData midXIFrameStreamInf = playlistDatas.get(3);
        assertNotNull(midXIFrameStreamInf);
        assertNotNull(midXIFrameStreamInf.getStreamInfo());
        assertEquals(150000, midXIFrameStreamInf.getStreamInfo().getBandwidth());
        assertEquals("mid/iframe.m3u8", midXIFrameStreamInf.getStreamInfo().getUri());

        PlaylistData hiXStreamInf = playlistDatas.get(4);
        assertNotNull(hiXStreamInf);
        assertNotNull(hiXStreamInf.getStreamInfo());
        assertEquals(7680000, hiXStreamInf.getStreamInfo().getBandwidth());
        assertEquals("hi/audio-video.m3u8", hiXStreamInf.getLocation());
        assertNull(hiXStreamInf.getStreamInfo().getUri());

        PlaylistData hiXIFrameStreamInf = playlistDatas.get(5);
        assertNotNull(hiXIFrameStreamInf);
        assertNotNull(hiXIFrameStreamInf.getStreamInfo());
        assertEquals(550000, hiXIFrameStreamInf.getStreamInfo().getBandwidth());
        assertEquals("hi/iframe.m3u8", hiXIFrameStreamInf.getStreamInfo().getUri());

        PlaylistData audioXStreamInf = playlistDatas.get(6);
        assertNotNull(audioXStreamInf);
        assertNotNull(audioXStreamInf.getStreamInfo());
        assertEquals(65000, audioXStreamInf.getStreamInfo().getBandwidth());
        assertNotNull(audioXStreamInf.getStreamInfo().getCodecs());
        assertEquals(1, audioXStreamInf.getStreamInfo().getCodecs().size());
        assertEquals("mp4a.40.5", audioXStreamInf.getStreamInfo().getCodecs().get(0));
        assertEquals("audio-only.m3u8", audioXStreamInf.getLocation());
        assertNull(audioXStreamInf.getStreamInfo().getUri());
        
        String writtenPlaylist = writePlaylist(playlist);
        assertEquals("#EXTM3U\n" +
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
    public void masterPlaylistWithAlternativeAudio() throws IOException, ParseException {
        Playlist playlist = readPlaylist("masterPlaylistWithAlternativeAudio.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
    
    @Test
    public void masterPlaylistWithAlternativeVideo() throws IOException, ParseException {
        Playlist playlist = readPlaylist("masterPlaylistWithAlternativeVideo.m3u8");
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
    }
}
