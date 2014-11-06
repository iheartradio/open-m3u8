package com.iheartradio.m3u8.data;

public class Resolution {
    public final int width;
    public final int height;

    public Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Resolution)) {
            return false;
        }

        Resolution other = (Resolution) o;
        return this.width == other.width && this.height == other.height;
    }
}
