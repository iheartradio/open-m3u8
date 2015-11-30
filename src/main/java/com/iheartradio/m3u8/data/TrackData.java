package com.iheartradio.m3u8.data;

import java.util.Objects;

public class TrackData {
    private final String mUri;
    private final TrackInfo mTrackInfo;
    private final EncryptionData mEncryptionData;
    private final boolean mHasDiscontinuity;

    private TrackData(String uri, TrackInfo trackInfo, EncryptionData encryptionData, boolean hasDiscontinuity) {
        mUri = uri;
        mTrackInfo = trackInfo;
        mEncryptionData = encryptionData;
        mHasDiscontinuity = hasDiscontinuity;
    }


    public String getUri() {
        return mUri;
    }

    public boolean hasTrackInfo() {
        return mTrackInfo != null;
    }

    public TrackInfo getTrackInfo() {
        return mTrackInfo;
    }

    public boolean hasEncryptionData() {
        return mEncryptionData != null;
    }

    public boolean isEncrypted() {
        return hasEncryptionData() &&
               mEncryptionData.getMethod() != null &&
               mEncryptionData.getMethod() != EncryptionMethod.NONE;
    }

    public boolean hasDiscontinuity() {
        return mHasDiscontinuity;
    }

    public EncryptionData getEncryptionData() {
        return mEncryptionData;
    }

    public Builder buildUpon() {
        return new Builder(getUri(), mTrackInfo, mEncryptionData, mHasDiscontinuity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUri, mEncryptionData, mTrackInfo, mHasDiscontinuity);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TrackData)) {
            return false;
        }

        TrackData other = (TrackData) o;

        return Objects.equals(mUri, other.getUri()) &&
               Objects.equals(mEncryptionData, other.mEncryptionData) &&
               Objects.equals(mTrackInfo, other.mTrackInfo) &&
               Objects.equals(mHasDiscontinuity, other.mHasDiscontinuity);
    }

    @Override
    public String toString() {
        return "TrackData [mUri=" + mUri + ", mTrackInfo=" + mTrackInfo + ", mEncryptionData=" + mEncryptionData + ", mHasDiscontinuity=" + mHasDiscontinuity + "]";
    }

    public static class Builder {
        private String mUri;
        private TrackInfo mTrackInfo;
        private EncryptionData mEncryptionData;
        private boolean mHasDiscontinuity;

        public Builder() {
        }

        private Builder(String uri, TrackInfo trackInfo, EncryptionData encryptionData, boolean hasDiscontinuity) {
            mUri = uri;
            mTrackInfo = trackInfo;
            mEncryptionData = encryptionData;
            mHasDiscontinuity = hasDiscontinuity;
        }

        public Builder withUri(String url) {
            mUri = url;
            return this;
        }

        public Builder withTrackInfo(TrackInfo trackInfo) {
            mTrackInfo = trackInfo;
            return this;
        }

        public Builder withEncryptionData(EncryptionData encryptionData) {
            mEncryptionData = encryptionData;
            return this;
        }

        public Builder withDiscontinuity(boolean hasDiscontinuity) {
            mHasDiscontinuity = hasDiscontinuity;
            return this;
        }

        public TrackData build() {
            return new TrackData(mUri, mTrackInfo, mEncryptionData, mHasDiscontinuity);
        }
    }
}
