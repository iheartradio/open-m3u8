package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MasterPlaylist {
    private final List<PlaylistData> mPlaylists;
    private final MediaData mMediaData;

    private MasterPlaylist(List<PlaylistData> playlists, MediaData mediaData) {
        mPlaylists = Collections.unmodifiableList(playlists);
        mMediaData = mediaData;
    }

    public List<PlaylistData> getPlaylists() {
        return mPlaylists;
    }

    public MediaData getMediaData() {
        return mMediaData;
    }

    public static class Builder {
        private List<PlaylistData> mPlaylists;
        private MediaData mMediaData;

        public Builder withPlaylists(List<PlaylistData> playlists) {
            mPlaylists = playlists;
            return this;
        }

        public Builder withMediaData(MediaData mediaData) {
            mMediaData = mediaData;
            return this;
        }

        public MasterPlaylist build() {
            return new MasterPlaylist(mPlaylists, mMediaData);
        }
    }
}
