package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MasterPlaylist;
import com.iheartradio.m3u8.data.Playlist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MasterPlaylistParserTest {
    @Test
    public void test() throws Exception {
        final Playlist playlist = TestUtil.parsePlaylistFromResource("masterPlaylist.m3u8");
        final MasterPlaylist masterPlaylist = playlist.getMasterPlaylist();

        assertTrue(playlist.hasMasterPlaylist());
        assertFalse(playlist.hasMediaPlaylist());

        assertTrue(masterPlaylist.hasStartData());
        assertEquals(4.5, masterPlaylist.getStartData().getTimeOffset(), 1e-12);
        assertFalse(masterPlaylist.getStartData().isPrecise());
    }
}
