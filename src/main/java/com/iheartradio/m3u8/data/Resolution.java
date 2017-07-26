package com.iheartradio.m3u8.data;

import java.util.Objects;

public class Resolution {
    public final int width;
    public final int height;

    public Resolution(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, width);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Resolution)) {
            return false;
        }

        Resolution other = (Resolution) o;

        return width == other.width &&
               height == other.height;
    }
}
