package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MediaPlaylist {
    private final List<TrackData> mTracks;
    private final List<String> mUnknownTags;
    private final int mTargetDuration;
    private final int mMediaSequenceNumber;
    private final boolean mIsIframesOnly;
    private final PlaylistType mPlaylistType;
    private final StartData mStartData;

    private MediaPlaylist(List<TrackData> tracks, List<String> unknownTags, int targetDuration, StartData startData, int mediaSequenceNumber, boolean isIframesOnly, PlaylistType playlistType) {
        mTracks = Collections.unmodifiableList(tracks);
        mUnknownTags = Collections.unmodifiableList(unknownTags);
        mTargetDuration = targetDuration;
        mMediaSequenceNumber = mediaSequenceNumber;
        mIsIframesOnly = isIframesOnly;
        mStartData = startData;
        mPlaylistType = playlistType;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MediaPlaylist)) {
            return false;
        }

        MediaPlaylist other = (MediaPlaylist) o;

        return ObjectUtil.equals(this.mTracks, other.mTracks) &&
               this.mTargetDuration == other.mTargetDuration &&
               this.mMediaSequenceNumber == other.mMediaSequenceNumber &&
               this.mIsIframesOnly == other.mIsIframesOnly &&
               ObjectUtil.equals(this.mStartData, other.mStartData) &&
               this.mPlaylistType == other.mPlaylistType;
    }

    public List<TrackData> getTracks() {
        return mTracks;
    }

    public int getTargetDuration() {
        return mTargetDuration;
    }

    public int getMediaSequenceNumber() {
        return mMediaSequenceNumber;
    }
    
    public boolean isIframesOnly() {
        return mIsIframesOnly;
    }
    
    public boolean hasUnknownTags() {
        return mUnknownTags.size() > 0;
    }
    
    public List<String> getUnknownTags() {
        return mUnknownTags;
    }
    
    public StartData getStartData() {
        return mStartData;
    }

    public PlaylistType getPlaylistType() {
        return mPlaylistType;
    }

    public Builder buildUpon() {
        return new Builder(mTracks, mTargetDuration, mMediaSequenceNumber, mIsIframesOnly, mPlaylistType, mStartData);
    }

    public static class Builder {
        private List<TrackData> mTracks;
        private List<String> mUnknownTags = Collections.emptyList();
        private int mTargetDuration;
        private int mMediaSequenceNumber;
        private boolean mIsIframesOnly;
        private PlaylistType mPlaylistType;
        private StartData mStartData;

        public Builder() {
        }

        private Builder(List<TrackData> tracks, int targetDuration, int mediaSequenceNumber, boolean isIframesOnly, PlaylistType playlistType, StartData startData) {
            mTracks = tracks;
            mTargetDuration = targetDuration;
            mMediaSequenceNumber = mediaSequenceNumber;
            mIsIframesOnly = isIframesOnly;
            mPlaylistType = playlistType;
            mStartData = startData;
        }

        public Builder withTracks(List<TrackData> tracks) {
            mTracks = tracks;
            return this;
        }
        
        public Builder withUnknownTags(List<String> unknownTags) {
            mUnknownTags = unknownTags;
            return this;
        }

        public Builder withTargetDuration(int targetDuration) {
            mTargetDuration = targetDuration;
            return this;
        }

        public Builder withStartData(StartData startData) {
            mStartData = startData;
            return this;
        }
        
        public Builder withMediaSequenceNumber(int mediaSequenceNumber) {
            mMediaSequenceNumber = mediaSequenceNumber;
            return this;
        }
        
        public Builder withIsIframesOnly(boolean isIframesOnly) {
            mIsIframesOnly = isIframesOnly;
            return this;
        }
        
        public Builder withPlaylistType(PlaylistType playlistType) {
            mPlaylistType = playlistType;
            return this;
        }

        public MediaPlaylist build() {
            return new MediaPlaylist(mTracks, mUnknownTags, mTargetDuration, mStartData, mMediaSequenceNumber, mIsIframesOnly, mPlaylistType);
        }
    }
}
