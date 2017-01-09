package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MediaPlaylistParserTest {
    @Test
    public void test() throws Exception {
        final Playlist playlist = TestUtil.parsePlaylistFromResource("mediaPlaylist.m3u8");
        final MediaPlaylist mediaPlaylist = playlist.getMediaPlaylist();

        assertFalse(playlist.hasMasterPlaylist());
        assertTrue(playlist.hasMediaPlaylist());
        assertTrue(mediaPlaylist.hasStartData());
        assertEquals(-4.5, mediaPlaylist.getStartData().getTimeOffset(), 1e-12);
        assertTrue(mediaPlaylist.getStartData().isPrecise());
        assertEquals(10, mediaPlaylist.getTargetDuration());
    }
}
