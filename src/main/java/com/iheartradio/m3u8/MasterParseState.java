package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MasterPlaylist;
import com.iheartradio.m3u8.data.PlaylistData;

import java.util.ArrayList;
import java.util.List;

class MasterParseState implements IParseState<MasterPlaylist> {
    public final List<PlaylistData> playlists;

    public MasterParseState() {
        playlists = new ArrayList<PlaylistData>();
    }

    public MasterParseState(MasterParseState source) {
        playlists = source.playlists;
    }

    @Override
    public MasterPlaylist buildPlaylist() throws ParseException {
        return new MasterPlaylist(playlists);
    }
}
