package com.iheartradio.m3u8.data;

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

    public static class Builder {
        private float mTimeOffset;
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
            if (mTimeOffset == 0.0f) {
                throw new IllegalStateException("StartData requires a timeOffset");
            }
    
            return new StartData(mTimeOffset, mPrecise);
        }
    }
}
