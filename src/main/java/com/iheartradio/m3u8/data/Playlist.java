package com.iheartradio.m3u8.data;

/**
 * The Playlist is similar to a C style union of a MasterPlaylist and MediaPlaylist in that it has one or the other but not both.
 */
public class Playlist {
    public static final int MIN_COMPATIBILITY_VERSION = 1;

    private final MasterPlaylist mMasterPlaylist;
    private final MediaPlaylist mMediaPlaylist;
    private final boolean mIsExtended;
    private final int mCompatibilityVersion;

    private Playlist(MasterPlaylist masterPlaylist, MediaPlaylist mediaPlaylist, boolean isExtended, int compatibilityVersion) {
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
        return mCompatibilityVersion;
    }

    public Builder buildUpon() {
        return new Builder(mMasterPlaylist, mMediaPlaylist, mIsExtended, mCompatibilityVersion);
    }

    public static class Builder {
        private MasterPlaylist mMasterPlaylist;
        private MediaPlaylist mMediaPlaylist;
        private boolean mIsExtended;
        private int mCompatibilityVersion = MIN_COMPATIBILITY_VERSION;

        public Builder() {
        }

        private Builder(MasterPlaylist masterPlaylist, MediaPlaylist mediaPlaylist, boolean isExtended, int compatibilityVersion) {
            mMasterPlaylist = masterPlaylist;
            mMediaPlaylist = mediaPlaylist;
            mIsExtended = isExtended;
            mCompatibilityVersion = compatibilityVersion;
        }

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
            if (version < MIN_COMPATIBILITY_VERSION) {
                throw new IllegalArgumentException("compatibility version must be >= " + MIN_COMPATIBILITY_VERSION);
            }

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
