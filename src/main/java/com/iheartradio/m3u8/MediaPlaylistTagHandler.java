package com.iheartradio.m3u8;

import java.util.regex.Matcher;

abstract class MediaPlaylistTagHandler extends ExtTagHandler {
    @Override
    public void handle(String line, ParseState state) throws ParseException {
        validateNotMaster(state);
        super.handle(line, state);
    }

    private void validateNotMaster(ParseState state) throws ParseException {
        if (state.isMaster()) {
            throw new ParseException(ParseExceptionType.MEDIA_IN_MASTER, getTag());
        }
    }

    static final IExtTagHandler EXT_X_TARGETDURATION = new MediaPlaylistTagHandler() {
        @Override
        public String getTag() {
            return Constants.EXT_X_TARGETDURATION;
        }

        @Override
        boolean hasAttributes() {
            return true;
        }

        @Override
        public void handle(String line, ParseState state) throws ParseException {
            super.handle(line, state);

            final Matcher matcher = match(Constants.EXT_X_TARGETDURATION_PATTERN, line);

            if (state.getMedia().targetDuration != null) {
                throw new ParseException(ParseExceptionType.MULTIPLE_EXT_TAGS, getTag());
            }

            state.getMedia().targetDuration = ParseUtil.parseInt(matcher.group(1), getTag());
        }
    };
}
