package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MasterPlaylist {
    private final List<PlaylistData> mPlaylists;
    private final List<MediaData> mMediaData;

    private MasterPlaylist(List<PlaylistData> playlists, List<MediaData> mediaData) {
        mPlaylists = Collections.unmodifiableList(playlists);
        mMediaData = Collections.unmodifiableList(mediaData);
    }

    public List<PlaylistData> getPlaylists() {
        return mPlaylists;
    }

    public List<MediaData> getMediaData() {
        return mMediaData;
    }

    public Builder buildUpon() {
        return new Builder(mPlaylists, mMediaData);
    }

    public static class Builder {
        private List<PlaylistData> mPlaylists;
        private List<MediaData> mMediaData;

        public Builder() {
        }

        private Builder(List<PlaylistData> playlists, List<MediaData> mediaData) {
            mPlaylists = playlists;
            mMediaData = mediaData;
        }

        public Builder withPlaylists(List<PlaylistData> playlists) {
            mPlaylists = playlists;
            return this;
        }

        public Builder withMediaData(List<MediaData> mediaData) {
            mMediaData = mediaData;
            return this;
        }

        public MasterPlaylist build() {
            return new MasterPlaylist(mPlaylists, mMediaData);
        }
    }
}
