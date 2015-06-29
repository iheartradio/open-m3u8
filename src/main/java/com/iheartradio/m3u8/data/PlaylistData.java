package com.iheartradio.m3u8.data;

import java.util.Objects;

public class PlaylistData extends LocationData {
    private final StreamInfo mStreamInfo;

    private PlaylistData(LocationType locationType, String location, StreamInfo streamInfo) {
        super(locationType, location);
        mStreamInfo = streamInfo;
    }

    public boolean hasStreamInfo() {
        return mStreamInfo != null;
    }

    public StreamInfo getStreamInfo() {
        return mStreamInfo;
    }

    public Builder buildUpon() {
        return new Builder(getLocationType(), getLocation(), mStreamInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mStreamInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlaylistData)) {
            return false;
        }

        PlaylistData other = (PlaylistData) o;
        
        return super.equals(other) &&
               Objects.equals(mStreamInfo, other.mStreamInfo);
    }

    public static class Builder {
        private LocationType mLocationType;
        private String mLocation;
        private StreamInfo mStreamInfo;

        public Builder() {
        }

        private Builder(LocationType locationType, String location, StreamInfo streamInfo) {
            mLocationType = locationType;
            mLocation = location;
            mStreamInfo = streamInfo;
        }

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

        public Builder withStreamInfo(StreamInfo streamInfo) {
            mStreamInfo = streamInfo;
            return this;
        }

        public PlaylistData build() {
            if (mLocationType == null) {
                throw new IllegalStateException("cannot build PlaylistData without a path or url");
            }

            return new PlaylistData(mLocationType, mLocation, mStreamInfo);
        }
    }

    @Override
    public String toString() {
        return "PlaylistData [mStreamInfo=" + mStreamInfo
                + ", getLocationType()=" + getLocationType()
                + ", getLocation()=" + getLocation() + "]";
    }
}
