package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.util.regex.Matcher;

interface ExtTagHandler extends LineHandler {
    String getTag();

    public static final ExtTagHandler EXTM3U_HANDLER = new ExtTagHandler() {
        @Override
        public String getTag() {
            return Constants.EXTM3U_TAG;
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            if (state.isExtended()) {
                throw new ParseException(ParseExceptionType.MULTIPLE_EXT_TAGS, getTag());
            }

            state.setExtended();
        }
    };

    public static final ExtTagHandler EXT_X_VERSION_HANDLER = new ExtTagHandler() {
        @Override
        public String getTag() {
            return Constants.EXT_X_VERSION_TAG;
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            if (line.indexOf(Constants.EXT_TAG_END) != getTag().length() + 1) {
                throw new ParseException(ParseExceptionType.MISSING_EXT_TAG_SEPARATOR, getTag());
            }

            final Matcher matcher = Constants.EXT_X_VERSION_PATTERN.matcher(line);

            if (!matcher.matches()) {
                throw new ParseException(ParseExceptionType.BAD_EXT_TAG_FORMAT, getTag());
            }

            if (state.getCompatibilityVersion() != null) {
                throw new ParseException(ParseExceptionType.MULTIPLE_EXT_TAGS, getTag());
            }

            final int compatibilityVersion = Integer.parseInt(matcher.group(1));

            if (compatibilityVersion < Playlist.MIN_COMPATIBILITY_VERSION) {
                throw new ParseException(ParseExceptionType.INVALID_COMPATIBILITY_VERSION, getTag() + ":" + compatibilityVersion);
            }

            state.setCompatibilityVersion(compatibilityVersion);
        }
    };
}
