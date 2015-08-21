package com.iheartradio.m3u8.data;

import java.util.Objects;

public class StartData {
    private final float mTimeOffset;
    private final boolean mPrecise;

    public StartData(float timeOffset, boolean precise) {
        mTimeOffset = timeOffset;
        mPrecise = precise;
    }

    public float getTimeOffset() {
        return mTimeOffset;
    }

    public boolean isPrecise() {
        return mPrecise;
    }

    public Builder buildUpon() {
        return new Builder(mTimeOffset, mPrecise);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mPrecise, mTimeOffset);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StartData)) {
            return false;
        }

        StartData other = (StartData) o;
        
        return this.mPrecise == other.mPrecise &&
                this.mTimeOffset == other.mTimeOffset;
    }

    public static class Builder {
        private float mTimeOffset = Float.NaN;
        private boolean mPrecise;

        public Builder() {
        }

        private Builder(float timeOffset, boolean precise) {
            mTimeOffset = timeOffset;
            mPrecise = precise;
        }

        public Builder withTimeOffset(float timeOffset) {
            mTimeOffset = timeOffset;
            return this;
        }

        public Builder withPrecise(boolean precise) {
            mPrecise = precise;
            return this;
        }
        
        public StartData build() {
            return new StartData(mTimeOffset, mPrecise);
        }
    }
}
