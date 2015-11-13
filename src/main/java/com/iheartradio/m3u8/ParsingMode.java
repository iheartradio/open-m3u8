package com.iheartradio.m3u8;

public class ParsingMode {
    public static final ParsingMode STRICT = new Builder().build();

    public static final ParsingMode LENIENT = new Builder()
            .allowUnknownTags()
            .allowNegativeNumbers()
            .build();

    public final boolean allowUnknownTags;
    public final boolean allowNegativeNumbers;

    private ParsingMode(final boolean allowUnknownTags, final boolean allowNegativeNumbers) {
        this.allowUnknownTags = allowUnknownTags;
        this.allowNegativeNumbers = allowNegativeNumbers;
    }

    public static class Builder {
        private boolean mAllowUnknownTags = false;
        private boolean mAllowNegativeNumbers = false;

        public Builder allowUnknownTags() {
            mAllowUnknownTags = true;
            return this;
        }

        public Builder allowNegativeNumbers() {
            mAllowNegativeNumbers = true;
            return this;
        }

        public ParsingMode build() {
            return new ParsingMode(mAllowUnknownTags, mAllowNegativeNumbers);
        }
    }
}
