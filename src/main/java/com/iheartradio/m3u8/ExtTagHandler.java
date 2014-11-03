package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class ExtTagHandler implements IExtTagHandler {
    @Override
    public void handle(String line, ParseState state) throws ParseException {
        state.setMedia();

        if (hasAttributes()) {
            if (line.indexOf(Constants.EXT_TAG_END) != getTag().length() + 1) {
                throw new ParseException(ParseExceptionType.MISSING_EXT_TAG_SEPARATOR, getTag());
            }
        }
    }

    abstract boolean hasAttributes();

    Matcher match(Pattern pattern, String line) throws ParseException {
        final Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            throw new ParseException(ParseExceptionType.BAD_EXT_TAG_FORMAT, getTag());
        }

        return matcher;
    }

    static final IExtTagHandler EXTM3U_HANDLER = new ExtTagHandler() {
        @Override
        public String getTag() {
            return Constants.EXTM3U_TAG;
        }

        @Override
        boolean hasAttributes() {
            return false;
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            if (state.isExtended()) {
                throw new ParseException(ParseExceptionType.MULTIPLE_EXT_TAGS, getTag());
            }

            state.setExtended();
        }
    };

    static final IExtTagHandler EXT_X_VERSION_HANDLER = new ExtTagHandler() {
        @Override
        public String getTag() {
            return Constants.EXT_X_VERSION_TAG;
        }

        @Override
        boolean hasAttributes() {
            return true;
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            super.handle(line, state);

            final Matcher matcher = match(Constants.EXT_X_VERSION_PATTERN, line);

            if (state.getCompatibilityVersion() != null) {
                throw new ParseException(ParseExceptionType.MULTIPLE_EXT_TAGS, getTag());
            }

            final int compatibilityVersion = ParseUtil.parseInt(matcher.group(1), getTag());

            if (compatibilityVersion < Playlist.MIN_COMPATIBILITY_VERSION) {
                throw new ParseException(ParseExceptionType.INVALID_COMPATIBILITY_VERSION, getTag() + ":" + compatibilityVersion);
            }

            state.setCompatibilityVersion(compatibilityVersion);
        }
    };
}
