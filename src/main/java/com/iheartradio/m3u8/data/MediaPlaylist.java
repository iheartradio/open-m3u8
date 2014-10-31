package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MediaPlaylist {
    private final List<TrackData> mTracks;

    public MediaPlaylist(List<TrackData> tracks) {
        mTracks = Collections.unmodifiableList(tracks);
    }

    public List<TrackData> getTracks() {
        return mTracks;
    }
}
