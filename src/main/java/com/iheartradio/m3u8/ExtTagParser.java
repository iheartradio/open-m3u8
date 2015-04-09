package com.iheartradio.m3u8;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.iheartradio.m3u8.data.Playlist;

abstract class ExtTagParser implements IExtTagParser {
    @Override
    public void parse(String line, ParseState state) throws ParseException {
        if (hasData()) {
            if (line.indexOf(Constants.EXT_TAG_END) != getTag().length() + 1) {
                throw ParseException.create(ParseExceptionType.MISSING_EXT_TAG_SEPARATOR, getTag(), line);
            }
        }
    }
    
    abstract boolean hasData();
    
    Matcher match(Pattern pattern, String line) throws ParseException {
        final Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            throw ParseException.create(ParseExceptionType.BAD_EXT_TAG_FORMAT, getTag(), line);
        }

        return matcher;
    }
    
    <T> void parseAttributes(String line, T builder, ParseState state, Map<String, ? extends AttributeParser<T>> handlers) throws ParseException {
        for (Attribute attribute : ParseUtil.parseAttributeList(line, getTag())) {
            if (handlers.containsKey(attribute.name)) {
                handlers.get(attribute.name).parse(attribute, builder, state);
            } else {
                throw ParseException.create(ParseExceptionType.INVALID_ATTRIBUTE_NAME, getTag(), line);
            }
        }
    }

    static final IExtTagParser EXTM3U_HANDLER = new ExtTagParser()  {
        @Override
        public String getTag() {
            return Constants.EXTM3U_TAG;
        }

        @Override
        boolean hasData() {
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
    
    static final IExtTagParser EXT_UNKNOWN_HANDLER = new ExtTagParser() {
        @Override
        public String getTag() {
            return null;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            if (state.isMaster()) {
                state.getMaster().unknownTags.add(line);
            } else {
                state.getMedia().unknownTags.add(line);
            }
        };
    };
    
    static final IExtTagParser EXT_X_VERSION_HANDLER = new ExtTagParser() {
        
        @Override
        public String getTag() {
            return Constants.EXT_X_VERSION_TAG;
        }

        @Override
        boolean hasData() {
            return true;
        }
        
        @Override
        public void parse(String line, ParseState state) throws ParseException {
            super.parse(line, state);

            final Matcher matcher = match(Constants.EXT_X_VERSION_PATTERN, line);

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
