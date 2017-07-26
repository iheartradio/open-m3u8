package com.iheartradio.m3u8.data;

import java.util.Objects;

public class PlaylistData {
    private final String mUri;
    private final StreamInfo mStreamInfo;

    private PlaylistData(String uri, StreamInfo streamInfo) {
        mUri = uri;
        mStreamInfo = streamInfo;
    }

    public String getUri() {
        return mUri;
    }

    public boolean hasStreamInfo() {
        return mStreamInfo != null;
    }

    public StreamInfo getStreamInfo() {
        return mStreamInfo;
    }

    public Builder buildUpon() {
        return new Builder(mUri, mStreamInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUri, mStreamInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PlaylistData)) {
            return false;
        }

        PlaylistData other = (PlaylistData) o;
        return Objects.equals(mUri, other.mUri) && Objects.equals(mStreamInfo, other.mStreamInfo);
    }

    @Override
    public String toString() {
        return "PlaylistData [mStreamInfo=" + mStreamInfo
                + ", mUri=" + mUri + "]";
    }

    public static class Builder {
        private String mUri;
        private StreamInfo mStreamInfo;

        public Builder() {
        }

        private Builder(String uri, StreamInfo streamInfo) {
            mUri = uri;
            mStreamInfo = streamInfo;
        }

        public Builder withPath(String path) {
            mUri = path;
            return this;
        }

        public Builder withUri(String uri) {
            mUri = uri;
            return this;
        }

        public Builder withStreamInfo(StreamInfo streamInfo) {
            mStreamInfo = streamInfo;
            return this;
        }

        public PlaylistData build() {
            return new PlaylistData(mUri, mStreamInfo);
        }
    }
}
