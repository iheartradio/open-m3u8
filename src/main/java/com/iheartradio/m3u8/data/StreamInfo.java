package com.iheartradio.m3u8.data;

import java.util.List;

public class StreamInfo {
    private static final int NONE = -1;

    private final int mBandwidth;
    private final int mAverageBandwidth;
    private final List<String> mCodecs;
    private final Resolution mResolution;
    private final String mAudio;
    private final String mUri;
    private final String mVideo;
    private final String mSubtitles;
    private final String mClosedCaptions;

    private StreamInfo(
            int bandwidth,
            int averageBandwidth,
            List<String> codecs,
            Resolution resolution,
            String audio,
            String uri,
            String video,
            String subtitles,
            String closedCaptions) {
        mBandwidth = bandwidth;
        mAverageBandwidth = averageBandwidth;
        mCodecs = codecs;
        mResolution = resolution;
        mAudio = audio;
        mUri = uri;
        mVideo = video;
        mSubtitles = subtitles;
        mClosedCaptions = closedCaptions;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StreamInfo)) {
            return false;
        }

        StreamInfo other = (StreamInfo) o;

        return this.mBandwidth == other.mBandwidth &&
               this.mAverageBandwidth == other.mAverageBandwidth &&
               ObjectUtil.equals(this.mResolution, other.mResolution) &&
               ObjectUtil.equals(this.mAudio, other.mAudio) &&
               ObjectUtil.equals(this.mUri, other.mUri) &&
               ObjectUtil.equals(this.mVideo, other.mVideo) &&
               ObjectUtil.equals(this.mSubtitles, other.mSubtitles) &&
               ObjectUtil.equals(this.mClosedCaptions, other.mClosedCaptions);
    }

    public boolean hasBandwidth() {
        return mBandwidth != NONE;
    }

    public int getBandwidth() {
        return mBandwidth;
    }

    public boolean hasAverageBandwidth() {
        return mAverageBandwidth != NONE;
    }

    public int getAverageBandwidth() {
        return mAverageBandwidth;
    }

    public boolean hasCodecs() {
        return mCodecs != null;
    }

    public List<String> getCodecs() {
        return mCodecs;
    }

    public boolean hasResolution() {
        return mResolution != null;
    }

    public Resolution getResolution() {
        return mResolution;
    }

    public boolean hasAudio() {
        return mAudio != null;
    }

    public String getAudio() {
        return mAudio;
    }
    
    public boolean hasUri() {
        return mUri != null;
    }
    
    public String getUri() {
        return mUri;
    }

    public boolean hasVideo() {
        return mVideo != null;
    }

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
        return new Builder(mBandwidth,
                mAverageBandwidth,
                mCodecs,
                mResolution,
                mAudio,
                mUri,
                mVideo,
                mSubtitles,
                mClosedCaptions);
    }

    public static class Builder {
        private int mBandwidth = NONE;
        private int mAverageBandwidth = NONE;
        private List<String> mCodecs;
        private Resolution mResolution;
        private String mAudio;
        private String mUri;
        private String mVideo;
        private String mSubtitles;
        private String mClosedCaptions;
        private boolean mBandwidthSet;
        private boolean mUriSet;

        public Builder() {
        }

        private Builder(
                int bandwidth,
                int averageBandwidth,
                List<String> codecs,
                Resolution resolution,
                String audio,
                String uri,
                String video,
                String subtitles,
                String closedCaptions) {
            mBandwidth = bandwidth;
            mAverageBandwidth = averageBandwidth;
            mCodecs = codecs;
            mResolution = resolution;
            mAudio = audio;
            mUri = uri;
            mVideo = video;
            mSubtitles = subtitles;
            mClosedCaptions = closedCaptions;
        }

        public Builder withBandwidth(int bandwidth) {
            mBandwidth = bandwidth;
            mBandwidthSet = true;
            return this;
        }

        public Builder withAverageBandwidth(int averageBandwidth) {
            mAverageBandwidth = averageBandwidth;
            return this;
        }

        public Builder withCodecs(List<String> codecs) {
            mCodecs = codecs;
            return this;
        }

        public Builder withResolution(Resolution resolution) {
            mResolution = resolution;
            return this;
        }

        public Builder withUri(String uri) {
            mUri = uri;
            mUriSet = true;
            return this;
        }
        
        public Builder withAudio(String audio) {
            mAudio = audio;
            return this;
        }

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

        public boolean isUriSet() {
            return mUriSet;
        }
        
        public boolean isBandwidthSet() {
            return mBandwidthSet;
        }

        public StreamInfo build() {
            if (mAverageBandwidth < NONE) {
                throw new IllegalStateException("invalid value for average bandwidth: " + mAverageBandwidth);
            }

            return new StreamInfo(
                    mBandwidth,
                    mAverageBandwidth,
                    mCodecs,
                    mResolution,
                    mAudio,
                    mUri,
                    mVideo,
                    mSubtitles,
                    mClosedCaptions);
        }
    }
}
