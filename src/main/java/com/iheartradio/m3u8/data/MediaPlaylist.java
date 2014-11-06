package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MediaPlaylist {
    private final List<TrackData> mTracks;
    private final int mTargetDuration;
    private final int mMediaSequenceNumber;

    private MediaPlaylist(List<TrackData> tracks, int targetDuration, int mediaSequenceNumber) {
        mTracks = Collections.unmodifiableList(tracks);
        mTargetDuration = targetDuration;
        mMediaSequenceNumber = mediaSequenceNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MediaPlaylist)) {
            return false;
        }

        MediaPlaylist other = (MediaPlaylist) o;

        return ObjectUtil.equals(this.mTracks, other.mTracks) &&
               this.mTargetDuration == other.mTargetDuration &&
               this.mMediaSequenceNumber == other.mMediaSequenceNumber;
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

    public static class Builder {
        private List<TrackData> mTracks;
        private int mTargetDuration;
        private int mMediaSequenceNumber;

        public Builder withTracks(List<TrackData> tracks) {
            mTracks = tracks;
            return this;
        }

        public Builder withTargetDuration(int targetDuration) {
            mTargetDuration = targetDuration;
            return this;
        }

        public Builder withMediaSequenceNumber(int mediaSequenceNumber) {
            mMediaSequenceNumber = mediaSequenceNumber;
            return this;
        }

        public MediaPlaylist build() {
            return new MediaPlaylist(mTracks, mTargetDuration, mMediaSequenceNumber);
        }
    }
}
