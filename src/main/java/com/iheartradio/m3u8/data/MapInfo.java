package com.iheartradio.m3u8.data;

import java.util.Objects;

public class MapInfo {
    private final String uri;
    private final ByteRange byteRange;

    public MapInfo(String uri, ByteRange byteRange) {
        this.uri = uri;
        this.byteRange = byteRange;
    }

    public String getUri() {
        return uri;
    }

    public boolean hasByteRange() {
        return byteRange != null;
    }

    public ByteRange getByteRange() {
        return byteRange;
    }

    public Builder buildUpon() {
        return new Builder(uri, byteRange);
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "uri='" + uri + '\'' +
                ", byteRange='" + byteRange + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapInfo mapInfo = (MapInfo) o;
        return Objects.equals(uri, mapInfo.uri) &&
                Objects.equals(byteRange, mapInfo.byteRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, byteRange);
    }

    public static class Builder {
        private String mUri;
        private ByteRange mByteRange;

        public Builder() {
        }

        private Builder(String uri, ByteRange byteRange) {
            this.mUri = uri;
            this.mByteRange = byteRange;
        }

        public Builder withUri(String uri) {
            this.mUri = uri;
            return this;
        }

        public Builder withByteRange(ByteRange byteRange) {
            this.mByteRange = byteRange;
            return this;
        }

        public MapInfo build() {
            return new MapInfo(mUri, mByteRange);
        }
    }
}
