package com.iheartradio.m3u8.data;

import java.util.Objects;

public class TrackData {
    private final String mUri;
    private final TrackInfo mTrackInfo;
    private final EncryptionData mEncryptionData;
    private final String mProgramDateTime;
    private final boolean mHasDiscontinuity;
    private final MapInfo mMapInfo;
    private final ByteRange mByteRange;

    private TrackData(String uri, TrackInfo trackInfo, EncryptionData encryptionData, String programDateTime, boolean hasDiscontinuity, MapInfo mapInfo, ByteRange byteRange) {
        mUri = uri;
        mTrackInfo = trackInfo;
        mEncryptionData = encryptionData;
        mProgramDateTime = programDateTime;
        mHasDiscontinuity = hasDiscontinuity;
        mMapInfo = mapInfo;
        mByteRange = byteRange;
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

    public boolean hasProgramDateTime() {
        return mProgramDateTime != null && mProgramDateTime.length() > 0;
    }

    public String getProgramDateTime() {
        return mProgramDateTime;
    }

    public boolean hasDiscontinuity() {
        return mHasDiscontinuity;
    }

    public EncryptionData getEncryptionData() {
        return mEncryptionData;
    }

    public boolean hasMapInfo() {
        return mMapInfo != null;
    }

    public MapInfo getMapInfo() {
        return mMapInfo;
    }

    public boolean hasByteRange() {
        return mByteRange != null;
    }

    public ByteRange getByteRange() {
        return mByteRange;
    }

    public Builder buildUpon() {
        return new Builder(getUri(), mTrackInfo, mEncryptionData, mHasDiscontinuity, mMapInfo, mByteRange);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackData trackData = (TrackData) o;
        return mHasDiscontinuity == trackData.mHasDiscontinuity &&
                Objects.equals(mUri, trackData.mUri) &&
                Objects.equals(mTrackInfo, trackData.mTrackInfo) &&
                Objects.equals(mEncryptionData, trackData.mEncryptionData) &&
                Objects.equals(mProgramDateTime, trackData.mProgramDateTime) &&
                Objects.equals(mMapInfo, trackData.mMapInfo) &&
                Objects.equals(mByteRange, trackData.mByteRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUri, mTrackInfo, mEncryptionData, mProgramDateTime, mHasDiscontinuity, mMapInfo, mByteRange);
    }

    @Override
    public String toString() {
        return "TrackData{" +
                "mUri='" + mUri + '\'' +
                ", mTrackInfo=" + mTrackInfo +
                ", mEncryptionData=" + mEncryptionData +
                ", mProgramDateTime='" + mProgramDateTime + '\'' +
                ", mHasDiscontinuity=" + mHasDiscontinuity +
                ", mMapInfo=" + mMapInfo +
                ", mByteRange=" + mByteRange +
                '}';
    }

    public static class Builder {
        private String mUri;
        private TrackInfo mTrackInfo;
        private EncryptionData mEncryptionData;
        private String mProgramDateTime;
        private boolean mHasDiscontinuity;
        private MapInfo mMapInfo;
        private ByteRange mByteRange;

        public Builder() {
        }

        private Builder(String uri, TrackInfo trackInfo, EncryptionData encryptionData, boolean hasDiscontinuity, MapInfo mapInfo, ByteRange byteRange) {
            mUri = uri;
            mTrackInfo = trackInfo;
            mEncryptionData = encryptionData;
            mHasDiscontinuity = hasDiscontinuity;
            mMapInfo = mapInfo;
            mByteRange = byteRange;
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

        public Builder withProgramDateTime(String programDateTime) {
            mProgramDateTime = programDateTime;
            return this;
        }

        public Builder withDiscontinuity(boolean hasDiscontinuity) {
            mHasDiscontinuity = hasDiscontinuity;
            return this;
        }

        public Builder withMapInfo(MapInfo mapInfo) {
            mMapInfo = mapInfo;
            return this;
        }

        public Builder withByteRange(ByteRange byteRange) {
            mByteRange = byteRange;
            return this;
        }

        public TrackData build() {
            return new TrackData(mUri, mTrackInfo, mEncryptionData, mProgramDateTime, mHasDiscontinuity, mMapInfo, mByteRange);
        }
    }
}
