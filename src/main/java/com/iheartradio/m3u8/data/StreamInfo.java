package com.iheartradio.m3u8.data;

import java.util.List;
import java.util.Objects;

public class StreamInfo implements IStreamInfo {
    public static final int NO_BANDWIDTH = -1;

    private final int mBandwidth;
    private final int mAverageBandwidth;
    private final List<String> mCodecs;
    private final Resolution mResolution;
    private final float mFrameRate;
    private final String mAudio;
    private final String mVideo;
    private final String mSubtitles;
    private final String mClosedCaptions;

    private StreamInfo(
            int bandwidth,
            int averageBandwidth,
            List<String> codecs,
            Resolution resolution,
            float frameRate,
            String audio,
            String video,
            String subtitles,
            String closedCaptions) {
        mBandwidth = bandwidth;
        mAverageBandwidth = averageBandwidth;
        mCodecs = codecs;
        mResolution = resolution;
        mFrameRate = frameRate;
        mAudio = audio;
        mVideo = video;
        mSubtitles = subtitles;
        mClosedCaptions = closedCaptions;
    }

    @Override
    public int getBandwidth() {
        return mBandwidth;
    }

    @Override
    public boolean hasAverageBandwidth() {
        return mAverageBandwidth != NO_BANDWIDTH;
    }

    @Override
    public int getAverageBandwidth() {
        return mAverageBandwidth;
    }

    @Override
    public boolean hasCodecs() {
        return mCodecs != null;
    }

    @Override
    public List<String> getCodecs() {
        return mCodecs;
    }

    @Override
    public boolean hasResolution() {
        return mResolution != null;
    }

    @Override
    public Resolution getResolution() {
        return mResolution;
    }

    @Override
    public boolean hasFrameRate() {
        return !Float.isNaN(mFrameRate);
    }

    @Override
    public float getFrameRate() {
        return mFrameRate;
    }

    public boolean hasAudio() {
        return mAudio != null;
    }

    public String getAudio() {
        return mAudio;
    }

    @Override
    public boolean hasVideo() {
        return mVideo != null;
    }

    @Override
    public String getVideo() {
        return mVideo;
    }

    public boolean hasSubtitles() {
        return mSubtitles != null;
    }

    public String getSubtitles() {
        return mSubtitles;
    }

    public boolean hasClosedCaptions() {
        return mClosedCaptions != null;
    }

    public String getClosedCaptions() {
        return mClosedCaptions;
    }

    public Builder buildUpon() {
        return new Builder(
                mBandwidth,
                mAverageBandwidth,
                mCodecs,
                mResolution,
                mFrameRate,
                mAudio,
                mVideo,
                mSubtitles,
                mClosedCaptions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                mBandwidth,
                mAverageBandwidth,
                mCodecs,
                mResolution,
                mFrameRate,
                mAudio,
                mVideo,
                mSubtitles,
                mClosedCaptions);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StreamInfo)) {
            return false;
        }

        StreamInfo other = (StreamInfo) o;

        return mBandwidth == other.mBandwidth &&
               mAverageBandwidth == other.mAverageBandwidth &&
               Objects.equals(mCodecs, other.mCodecs) &&
               Objects.equals(mResolution, other.mResolution) &&
               Objects.equals(mFrameRate, other.mFrameRate) &&
               Objects.equals(mAudio, other.mAudio) &&
               Objects.equals(mVideo, other.mVideo) &&
               Objects.equals(mSubtitles, other.mSubtitles) &&
               Objects.equals(mClosedCaptions, other.mClosedCaptions);
    }

    public static class Builder implements StreamInfoBuilder {
        private int mBandwidth = NO_BANDWIDTH;
        private int mAverageBandwidth = NO_BANDWIDTH;
        private List<String> mCodecs;
        private Resolution mResolution;
        private float mFrameRate = Float.NaN;
        private String mAudio;
        private String mVideo;
        private String mSubtitles;
        private String mClosedCaptions;

        public Builder() {
        }

        private Builder(
                int bandwidth,
                int averageBandwidth,
                List<String> codecs,
                Resolution resolution,
                float frameRate,
                String audio,
                String video,
                String subtitles,
                String closedCaptions) {
            mBandwidth = bandwidth;
            mAverageBandwidth = averageBandwidth;
            mCodecs = codecs;
            mResolution = resolution;
            mFrameRate = frameRate;
            mAudio = audio;
            mVideo = video;
            mSubtitles = subtitles;
            mClosedCaptions = closedCaptions;
        }

        @Override
        public Builder withBandwidth(int bandwidth) {
            mBandwidth = bandwidth;
            return this;
        }

        @Override
        public Builder withAverageBandwidth(int averageBandwidth) {
            mAverageBandwidth = averageBandwidth;
            return this;
        }

        @Override
        public Builder withCodecs(List<String> codecs) {
            mCodecs = codecs;
            return this;
        }

        @Override
        public Builder withResolution(Resolution resolution) {
            mResolution = resolution;
            return this;
        }

        @Override
        public Builder withFrameRate(float frameRate) {
            mFrameRate = frameRate;
            return this;
        }

        public Builder withAudio(String audio) {
            mAudio = audio;
            return this;
        }

        @Override
        public Builder withVideo(String video) {
            mVideo = video;
            return this;
        }

        public Builder withSubtitles(String subtitles) {
            mSubtitles = subtitles;
            return this;
        }

        public Builder withClosedCaptions(String closedCaptions) {
            mClosedCaptions = closedCaptions;
            return this;
        }

        public StreamInfo build() {
            return new StreamInfo(
                    mBandwidth,
                    mAverageBandwidth,
                    mCodecs,
                    mResolution,
                    mFrameRate,
                    mAudio,
                    mVideo,
                    mSubtitles,
                    mClosedCaptions);
        }
    }
}
