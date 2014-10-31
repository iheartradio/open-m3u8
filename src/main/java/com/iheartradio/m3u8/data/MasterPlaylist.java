package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MasterPlaylist {
    private final List<PlaylistData> mPlaylists;

    public MasterPlaylist(List<PlaylistData> playlists) {
        mPlaylists = Collections.unmodifiableList(playlists);
    }

    public List<PlaylistData> getPlaylists() {
        return mPlaylists;
    }
}
