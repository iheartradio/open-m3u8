package com.iheartradio.m3u8.data;

public class LocationData {
    private final LocationType mLocationType;
    private final String mLocation;

    public LocationData(LocationType locationType, String location) {
        if (locationType == null) {
            throw new IllegalArgumentException("locationType is null");
        }

        if (location == null) {
            throw new IllegalArgumentException("location is null");
        }

        mLocationType = locationType;
        mLocation = location;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocationData)) {
            return false;
        }

        LocationData other = (LocationData) o;

        return this.mLocationType == other.mLocationType &&
               ObjectUtil.equals(this.mLocation, other.mLocation);
    }

    public boolean isPath() {
        return mLocationType == LocationType.PATH;
    }

    public boolean isUrl() {
        return mLocationType == LocationType.URL;
    }

    public LocationType getLocationType() {
        return mLocationType;
    }

    public String getLocation() {
        return mLocation;
    }

    @Override
    public String toString() {
        return "LocationData [mLocationType=" + mLocationType + ", mLocation="
                + mLocation + "]";
    }
}
