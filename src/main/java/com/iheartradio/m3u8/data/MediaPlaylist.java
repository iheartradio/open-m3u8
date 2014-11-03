package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

public class MediaPlaylist {
    private final List<TrackData> mTracks;
    private final int mTargetDuration;

    private MediaPlaylist(List<TrackData> tracks, int targetDuration) {
        mTracks = Collections.unmodifiableList(tracks);
        mTargetDuration = targetDuration;
    }

    public List<TrackData> getTracks() {
        return mTracks;
    }

    public int getTargetDuration() {
        return mTargetDuration;
    }

    public static class Builder {
        private List<TrackData> mTracks;
        private int mTargetDuration;

        public Builder withTracks(List<TrackData> tracks) {
            mTracks = tracks;
            return this;
        }

        public Builder withTargetDuration(int targetDuration) {
            mTargetDuration = targetDuration;
            return this;
        }

        public MediaPlaylist build() {
            return new MediaPlaylist(mTracks, mTargetDuration);
        }
    }
}
