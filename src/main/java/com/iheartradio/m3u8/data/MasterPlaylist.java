package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MasterPlaylist {
    private final List<PlaylistData> mPlaylists;
    private final List<MediaData> mMediaData;
    private final List<String> mUnknownTags;

    private MasterPlaylist(List<PlaylistData> playlists, List<MediaData> mediaData, List<String> unknownTags) {
        mPlaylists = Collections.unmodifiableList(playlists);
        mMediaData = Collections.unmodifiableList(mediaData);
        mUnknownTags = Collections.unmodifiableList(unknownTags);
    }

    public List<PlaylistData> getPlaylists() {
        return mPlaylists;
    }

    public List<MediaData> getMediaData() {
        return mMediaData;
    }
   
    public boolean hasUnknownTags() {
        return mUnknownTags.size() > 0;
    }
    
    public List<String> getUnknownTags() {
        return mUnknownTags;
    }

    public Builder buildUpon() {
        return new Builder(mPlaylists, mMediaData);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mMediaData, mPlaylists, mUnknownTags);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MasterPlaylist)) {
            return false;
        }

        MasterPlaylist other = (MasterPlaylist) o;
        
        return Objects.equals(mMediaData, other.mMediaData) &&
               Objects.equals(mPlaylists, other.mPlaylists) &&
               Objects.equals(mUnknownTags, other.mUnknownTags);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(MasterPlaylist")
                .append(" mPlaylists=").append(mPlaylists.toString())
                .append(" mMediaData=").append(mMediaData.toString())
                .append(" mUnknownTags=").append(mUnknownTags.toString())
                .append(")")
                .toString();
    }

    public static class Builder {
        private List<PlaylistData> mPlaylists;
        private List<MediaData> mMediaData;
        private List<String> mUnknownTags = Collections.emptyList();

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
        
        public Builder withUnknownTags(List<String> unknownTags) {
            mUnknownTags = unknownTags;
            return this;
        }

        public MasterPlaylist build() {
            return new MasterPlaylist(mPlaylists, mMediaData, mUnknownTags);
        }
    }
}
