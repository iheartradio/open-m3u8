package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MediaPlaylist {
    private final List<TrackData> mTracks;
    private final int mTargetDuration;
    private final int mMediaSequenceNumber;
    private final PlaylistType mPlaylistType;
    private final StartData mStartData;

    private MediaPlaylist(List<TrackData> tracks, int targetDuration, StartData startData, int mediaSequenceNumber, PlaylistType playlistType) {
        mTracks = Collections.unmodifiableList(tracks);
        mTargetDuration = targetDuration;
        mMediaSequenceNumber = mediaSequenceNumber;
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
    
    public StartData getStartData() {
        return mStartData;
    }

    public PlaylistType getPlaylistType() {
        return mPlaylistType;
    }

    public static class Builder {
        private List<TrackData> mTracks;
        private int mTargetDuration;
        private int mMediaSequenceNumber;
        private PlaylistType mPlaylistType;
        private StartData mStartData;

        public Builder withTracks(List<TrackData> tracks) {
            mTracks = tracks;
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
        
        public Builder withPlaylistType(PlaylistType playlistType) {
            mPlaylistType = playlistType;
            return this;
        }

        public MediaPlaylist build() {
            return new MediaPlaylist(mTracks, mTargetDuration, mStartData, mMediaSequenceNumber, mPlaylistType);
        }

    }
}
