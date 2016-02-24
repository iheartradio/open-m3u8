package com.iheartradio.m3u8.data;

import java.util.List;
import java.util.Objects;

public class MediaPlaylist {
    private final List<TrackData> mTracks;
    private final List<String> mUnknownTags;
    private final int mTargetDuration;
    private final int mMediaSequenceNumber;
    private final boolean mIsIframesOnly;
    private final boolean mIsOngoing;
    private final PlaylistType mPlaylistType;
    private final StartData mStartData;

    private MediaPlaylist(List<TrackData> tracks, List<String> unknownTags, int targetDuration, StartData startData, int mediaSequenceNumber, boolean isIframesOnly, boolean isOngoing, PlaylistType playlistType) {
        mTracks = DataUtil.emptyOrUnmodifiable(tracks);
        mUnknownTags = DataUtil.emptyOrUnmodifiable(unknownTags);
        mTargetDuration = targetDuration;
        mMediaSequenceNumber = mediaSequenceNumber;
        mIsIframesOnly = isIframesOnly;
        mIsOngoing = isOngoing;
        mStartData = startData;
        mPlaylistType = playlistType;
    }

    public boolean hasTracks() {
        return !mTracks.isEmpty();
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

    public boolean isOngoing() {
        return mIsOngoing;
    }

    public boolean hasUnknownTags() {
        return !mUnknownTags.isEmpty();
    }
    
    public List<String> getUnknownTags() {
        return mUnknownTags;
    }
    
    public StartData getStartData() {
        return mStartData;
    }
    
    public boolean hasStartData() {
        return mStartData != null;
    }

    public PlaylistType getPlaylistType() {
        return mPlaylistType;
    }
    
    public boolean hasPlaylistType() {
        return mPlaylistType != null;
    }

    public int getDiscontinuitySequenceNumber(final int segmentIndex) {
        if (segmentIndex < 0 || segmentIndex >= mTracks.size()) {
            throw new IndexOutOfBoundsException();
        }

        int discontinuitySequenceNumber = 0;

        for (int i = 0; i <= segmentIndex; ++i) {
            if (mTracks.get(i).hasDiscontinuity()) {
                ++discontinuitySequenceNumber;
            }
        }

        return discontinuitySequenceNumber;
    }

    public Builder buildUpon() {
        return new Builder(mTracks, mUnknownTags, mTargetDuration, mMediaSequenceNumber, mIsIframesOnly, mIsOngoing, mPlaylistType, mStartData);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(
                mTracks,
                mUnknownTags,
                mTargetDuration,
                mMediaSequenceNumber,
                mIsIframesOnly,
                mIsOngoing,
                mPlaylistType,
                mStartData);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MediaPlaylist)) {
            return false;
        }

        MediaPlaylist other = (MediaPlaylist) o;

        return Objects.equals(mTracks, other.mTracks) &&
               Objects.equals(mUnknownTags, other.mUnknownTags) &&
               mTargetDuration == other.mTargetDuration &&
               mMediaSequenceNumber == other.mMediaSequenceNumber &&
               mIsIframesOnly == other.mIsIframesOnly &&
               mIsOngoing == other.mIsOngoing &&
               Objects.equals(mPlaylistType, other.mPlaylistType) &&
               Objects.equals(mStartData, other.mStartData);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(MediaPlaylist")
                .append(" mTracks=").append(mTracks)
                .append(" mUnknownTags=").append(mUnknownTags)
                .append(" mTargetDuration=").append(mTargetDuration)
                .append(" mMediaSequenceNumber=").append(mMediaSequenceNumber)
                .append(" mIsIframesOnly=").append(mIsIframesOnly)
                .append(" mIsOngoing=").append(mIsOngoing)
                .append(" mPlaylistType=").append(mPlaylistType)
                .append(" mStartData=").append(mStartData)
                .append(")")
                .toString();
    }

    public static class Builder {
        private List<TrackData> mTracks;
        private List<String> mUnknownTags;
        private int mTargetDuration;
        private int mMediaSequenceNumber;
        private boolean mIsIframesOnly;
        private boolean mIsOngoing;
        private PlaylistType mPlaylistType;
        private StartData mStartData;

        public Builder() {
        }

        private Builder(List<TrackData> tracks, List<String> unknownTags, int targetDuration, int mediaSequenceNumber, boolean isIframesOnly, boolean isOngoing, PlaylistType playlistType, StartData startData) {
            mTracks = tracks;
            mUnknownTags = unknownTags;
            mTargetDuration = targetDuration;
            mMediaSequenceNumber = mediaSequenceNumber;
            mIsIframesOnly = isIframesOnly;
            mIsOngoing = isOngoing;
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

        public Builder withIsOngoing(boolean isOngoing) {
            mIsOngoing = isOngoing;
            return this;
        }

        public Builder withPlaylistType(PlaylistType playlistType) {
            mPlaylistType = playlistType;
            return this;
        }

        public MediaPlaylist build() {
            return new MediaPlaylist(mTracks, mUnknownTags, mTargetDuration, mStartData, mMediaSequenceNumber, mIsIframesOnly, mIsOngoing, mPlaylistType);
        }
    }
}
