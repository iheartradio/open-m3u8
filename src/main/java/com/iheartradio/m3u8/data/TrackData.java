package com.iheartradio.m3u8.data;

public class TrackData extends LocationData {
    private final TrackInfo mTrackInfo;
    private final EncryptionData mEncryptionData;

    private TrackData(LocationType locationType, String location, TrackInfo trackInfo, EncryptionData encryptionData) {
        super(locationType, location);
        mTrackInfo = trackInfo;
        mEncryptionData = encryptionData;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TrackData)) {
            return false;
        }

        TrackData other = (TrackData) o;
        return super.equals(other) && ObjectUtil.equals(this.mTrackInfo, other.mTrackInfo);
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
        return hasEncryptionData() && mEncryptionData.getMethod() != EncryptionMethod.NONE;
    }

    public EncryptionData getEncryptionData() {
        return mEncryptionData;
    }

    public static class Builder {
        private LocationType mLocationType;
        private String mLocation;
        private TrackInfo mTrackInfo;
        private EncryptionData mEncryptionData;

        public Builder withPath(String path) {
            if (path == null || path.isEmpty()) {
                throw new IllegalStateException("path cannot be empty");
            }

            mLocationType = LocationType.PATH;
            mLocation = path;
            return this;
        }

        public Builder withUrl(String url) {
            if (url == null || url.isEmpty()) {
                throw new IllegalStateException("url cannot be empty");
            }

            mLocationType = LocationType.URL;
            mLocation = url;
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

        public TrackData build() {
            if (mLocationType == null) {
                throw new IllegalStateException("cannot build TrackData without a path or url");
            }

            return new TrackData(mLocationType, mLocation, mTrackInfo, mEncryptionData);
        }
    }
}
