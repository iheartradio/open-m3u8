package com.iheartradio.m3u8;

import java.util.regex.Matcher;

import com.iheartradio.m3u8.data.Playlist;

class ExtLineParser implements LineParser {
    private final IExtTagParser mTagParser;

    ExtLineParser(IExtTagParser tagParser) {
        mTagParser = tagParser;
    }

    @Override
    public void parse(String line, ParseState state) throws ParseException {
        if (mTagParser.hasData()) {
            if (line.indexOf(Constants.EXT_TAG_END) != mTagParser.getTag().length() + 1) {
                throw ParseException.create(ParseExceptionType.MISSING_EXT_TAG_SEPARATOR, mTagParser.getTag(), line);
            }
        }
    }

    static final IExtTagParser EXTM3U_HANDLER = new IExtTagParser()  {
        @Override
        public String getTag() {
            return Constants.EXTM3U_TAG;
        }

        @Override
        public boolean hasData() {
            return false;
        }

        @Override
        public void parse(String line, ParseState state) throws ParseException {
            if (state.isExtended()) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            state.setExtended();
        }
    };
    
    static final IExtTagParser EXT_UNKNOWN_HANDLER = new IExtTagParser() {
        @Override
        public String getTag() {
            return null;
        }

        @Override
        public boolean hasData() {
            return false;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            if (state.isMaster()) {
                state.getMaster().unknownTags.add(line);
            } else if (state.isMedia()) {
                state.getMedia().unknownTags.add(line);
            } else {
                // nowhere to put the tag
            }
        }
    };
    
    static final IExtTagParser EXT_X_VERSION_HANDLER = new IExtTagParser() {
        private final ExtLineParser mLineParser = new ExtLineParser(this);

        @Override
        public String getTag() {
            return Constants.EXT_X_VERSION_TAG;
        }

        @Override
        public boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            mLineParser.parse(line, state);

            final Matcher matcher = ParseUtil.match(Constants.EXT_X_VERSION_PATTERN, line, getTag());

            if (state.getCompatibilityVersion() != ParseState.NONE) {
                throw ParseException.create(ParseExceptionType.MULTIPLE_EXT_TAG_INSTANCES, getTag(), line);
            }

            final int compatibilityVersion = ParseUtil.parseInt(matcher.group(1), getTag());

            if (compatibilityVersion < Playlist.MIN_COMPATIBILITY_VERSION) {
                throw ParseException.create(ParseExceptionType.INVALID_COMPATIBILITY_VERSION, getTag(), line);
            }

            if (compatibilityVersion > Constants.MAX_COMPATIBILITY_VERSION) {
                throw ParseException.create(ParseExceptionType.UNSUPPORTED_COMPATIBILITY_VERSION, getTag(), line);
            }

            state.setCompatibilityVersion(compatibilityVersion);
        }
    };
}
