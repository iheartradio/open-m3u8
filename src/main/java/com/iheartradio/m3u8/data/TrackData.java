package com.iheartradio.m3u8.data;

public class TrackData extends LocationData {
    private final TrackInfo mTrackInfo;

    public static TrackData fromPath(String path) {
        return fromPath(path, null);
    }

    public static TrackData fromPath(String path, TrackInfo trackInfo) {
        return new TrackData(LocationType.PATH, path, trackInfo);
    }

    public static TrackData fromUrl(String url) {
        return fromUrl(url, null);
    }

    public static TrackData fromUrl(String url, TrackInfo trackInfo) {
        return new TrackData(LocationType.URL, url, trackInfo);
    }

    private TrackData(LocationType locationType, String location, TrackInfo trackInfo) {
        super(locationType, location);
        mTrackInfo = trackInfo;
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
}
