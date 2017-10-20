package com.iheartradio.m3u8.data;

import java.util.Objects;

public class ByteRange {
    private final long mSubRangeLength;
    private final Long mOffset;

    public ByteRange(long subRangeLength, long offset) {
        this.mSubRangeLength = subRangeLength;
        this.mOffset = offset;
    }

    public ByteRange(long subRangeLength, Long offset) {
        this.mSubRangeLength = subRangeLength;
        this.mOffset = offset;
    }

    public ByteRange(long subRangeLength) {
        this(subRangeLength, null);
    }

    public long getSubRangeLength() {
        return mSubRangeLength;
    }

    public Long getOffset() {
        return mOffset;
    }

    public boolean hasOffset() {
        return mOffset != null;
    }

    @Override
    public String toString() {
        return "ByteRange{" +
                "mSubRangeLength=" + mSubRangeLength +
                ", mOffset=" + mOffset +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ByteRange byteRange = (ByteRange) o;
        return mSubRangeLength == byteRange.mSubRangeLength &&
                Objects.equals(mOffset, byteRange.mOffset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mSubRangeLength, mOffset);
    }
}
