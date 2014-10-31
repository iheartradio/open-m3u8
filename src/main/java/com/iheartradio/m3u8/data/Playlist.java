package com.iheartradio.m3u8.data;

public class Playlist {
    private final MasterPlaylist mMasterPlaylist;
    private final MediaPlaylist mMediaPlaylist;

    public Playlist(MasterPlaylist masterPlaylist) {
        this(masterPlaylist, null);
    }

    public Playlist(MediaPlaylist mediaPlaylist) {
        this(null, mediaPlaylist);
    }

    private Playlist(MasterPlaylist masterPlaylist, MediaPlaylist mediaPlaylist) {
        mMasterPlaylist = masterPlaylist;
        mMediaPlaylist = mediaPlaylist;
    }

    public boolean isMasterPlaylist() {
        return mMasterPlaylist != null;
    }

    public boolean isMediaPlaylist() {
        return mMediaPlaylist != null;
    }

    public MasterPlaylist asMasterPlaylist() {
        return mMasterPlaylist;
    }

    public MediaPlaylist asMediaPlaylist() {
        return mMediaPlaylist;
    }
}
