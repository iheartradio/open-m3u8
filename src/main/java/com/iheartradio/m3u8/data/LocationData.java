package com.iheartradio.m3u8.data;

import java.util.Objects;

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
    public int hashCode() {
        return Objects.hash(mLocation, mLocationType);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocationData)) {
            return false;
        }

        LocationData other = (LocationData) o;
        
        return Objects.equals(this.mLocation, other.mLocation) &&
                Objects.equals(this.mLocationType, other.mLocationType);
    }

    @Override
    public String toString() {
        return "LocationData [mLocationType=" + mLocationType + ", mLocation="
                + mLocation + "]";
    }
}
