package com.iheartradio.m3u8;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import com.iheartradio.m3u8.data.Playlist;

import static org.junit.Assert.*;

public class PlaylistParserWriterTest {

    private PlaylistParser parser;
    private PlaylistWriter writer;

    @Before
    public void setUp() {
        parser = new PlaylistParser();
        writer = new PlaylistWriter();
    }
    
    Playlist readPlaylist(String fileName) throws IOException, ParseException {
        assertNotNull(fileName);
        
        try(InputStream is = new FileInputStream("src/test/resources/" + fileName)) {
            Playlist playlist = parser.parse(is, Format.EXT_M3U, Encoding.UTF_8);
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
        
        String sPlaylist = writePlaylist(playlist);
        
        System.out.println(sPlaylist);
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
