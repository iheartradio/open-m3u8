package com.iheartradio.m3u8.data;

public class Playlist {
    public static final int MIN_COMPATIBILITY_VERSION = 1;

    private final MasterPlaylist mMasterPlaylist;
    private final MediaPlaylist mMediaPlaylist;
    private final boolean mIsExtended;
    private final Integer mCompatibilityVersion;

    private Playlist(MasterPlaylist masterPlaylist, MediaPlaylist mediaPlaylist, boolean isExtended, Integer compatibilityVersion) {
        mMasterPlaylist = masterPlaylist;
        mMediaPlaylist = mediaPlaylist;
        mIsExtended = isExtended;
        mCompatibilityVersion = compatibilityVersion;
    }

    public boolean hasMasterPlaylist() {
        return mMasterPlaylist != null;
    }

    public boolean hasMediaPlaylist() {
        return mMediaPlaylist != null;
    }

    public MasterPlaylist getMasterPlaylist() {
        return mMasterPlaylist;
    }

    public MediaPlaylist getMediaPlaylist() {
        return mMediaPlaylist;
    }

    public boolean isExtended() {
        return mIsExtended;
    }

    public int getCompatibilityVersion() {
        if (mCompatibilityVersion == null) {
            return MIN_COMPATIBILITY_VERSION;
        } else {
            return mCompatibilityVersion;
        }
    }

    public static class Builder {
        private boolean mIsExtended;
        private MasterPlaylist mMasterPlaylist;
        private MediaPlaylist mMediaPlaylist;
        private Integer mCompatibilityVersion;

        public Builder withMasterPlaylist(MasterPlaylist masterPlaylist) {
            if (mMediaPlaylist != null) {
                throw new IllegalStateException("cannot build Playlist with both a MasterPlaylist and a MediaPlaylist");
            }

            mMasterPlaylist = masterPlaylist;
            return withExtended(true);
        }

        public Builder withMediaPlaylist(MediaPlaylist mediaPlaylist) {
            if (mMasterPlaylist != null) {
                throw new IllegalStateException("cannot build Playlist with both a MasterPlaylist and a MediaPlaylist");
            }

            mMediaPlaylist = mediaPlaylist;
            return this;
        }

        public Builder withExtended(boolean isExtended) {
            if (mMasterPlaylist != null && !isExtended) {
                throw new IllegalStateException("a Playlist with a MasterPlaylist must be extended");
            }

            mIsExtended = isExtended;
            return this;
        }

        public Builder withCompatibilityVersion(int version) {
            mCompatibilityVersion = version;
            return this;
        }

        public Playlist build() {
            if (mMasterPlaylist != null || mMediaPlaylist != null) {
                return new Playlist(mMasterPlaylist, mMediaPlaylist, mIsExtended, mCompatibilityVersion);
            } else {
                throw new IllegalStateException("a Playlist must have a MasterPlaylist or a MediaPlaylist");
            }
        }
    }
}
