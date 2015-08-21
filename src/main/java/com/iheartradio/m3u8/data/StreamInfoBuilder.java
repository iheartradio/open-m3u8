package com.iheartradio.m3u8.data;

import java.util.List;

public interface StreamInfoBuilder {
    public StreamInfoBuilder withBandwidth(int bandwidth);

    public StreamInfoBuilder withAverageBandwidth(int averageBandwidth);

    public StreamInfoBuilder withCodecs(List<String> codecs);

    public StreamInfoBuilder withResolution(Resolution resolution);

    public StreamInfoBuilder withFrameRate(float frameRate);

    public StreamInfoBuilder withVideo(String video);
}
