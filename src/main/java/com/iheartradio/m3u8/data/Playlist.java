package com.iheartradio.m3u8.data;

import java.util.Objects;

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
    
    @Override
    public int hashCode() {
        return Objects.hash(mCompatibilityVersion, mIsExtended, mMasterPlaylist, mMediaPlaylist);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Playlist)) {
            return false;
        }

        Playlist other = (Playlist) o;
        
        return Objects.equals(mMasterPlaylist, other.mMasterPlaylist) &&
               Objects.equals(mMediaPlaylist, other.mMediaPlaylist) &&
               mIsExtended == other.mIsExtended &&
               mCompatibilityVersion == other.mCompatibilityVersion;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("(Playlist")
                .append(" mMasterPlaylist=").append(mMasterPlaylist)
                .append(" mMediaPlaylist=").append(mMediaPlaylist)
                .append(" mIsExtended=").append(mIsExtended)
                .append(" mCompatibilityVersion=").append(mCompatibilityVersion)
                .append(")")
                .toString();
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
            mMasterPlaylist = masterPlaylist;
            return withExtended(true);
        }

        public Builder withMediaPlaylist(MediaPlaylist mediaPlaylist) {
            mMediaPlaylist = mediaPlaylist;
            return this;
        }

        public Builder withExtended(boolean isExtended) {
            mIsExtended = isExtended;
            return this;
        }

        public Builder withCompatibilityVersion(int version) {
            mCompatibilityVersion = version;
            return this;
        }

        public Playlist build() {
            return new Playlist(mMasterPlaylist, mMediaPlaylist, mIsExtended, mCompatibilityVersion);
        }
    }
}
