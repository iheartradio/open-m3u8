package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class ExtendedM3uParser {
    private final Scanner mScanner;
    private final Map<String, ExtTagHandler> mExtTagHandlers = new HashMap<String, ExtTagHandler>();

    ExtendedM3uParser(InputStream inputStream, Encoding encoding) {
        mScanner = new Scanner(inputStream, encoding.value).useDelimiter(Constants.EOL_PATTERN);

        // TODO implement the EXT tag handlers and add them here
        putHandlers(
                ExtTagHandler.EXTM3U_HANDLER,
                ExtTagHandler.EXT_X_VERSION_HANDLER
        );
    }

    Playlist parse() throws ParseException {
        final ParseState state = new ParseState();
        final LineHandler playlistHandler = new PlaylistHandler();
        final LineHandler trackHandler = new TrackHandler();

        try {
            while (mScanner.hasNext()) {
                final String line = mScanner.next();
                checkWhitespace(line);

                if (line.length() == 0 || isComment(line)) {
                    continue;
                } else {
                    if (isExtTag(line)) {
                        final ExtTagHandler handler = mExtTagHandlers.get(getExtTagKey(line));

                        if (handler == null) {
                            throw new ParseException(ParseExceptionType.UNSUPPORTED_EXT_TAG_DETECTED);
                        } else {
                            handler.handle(line, state);
                        }
                    } else if (state.isMaster()) {
                        playlistHandler.handle(line, state);
                    } else if (state.isMedia()) {
                        trackHandler.handle(line, state);
                    } else {
                        throw new ParseException(ParseExceptionType.UNKNOWN_PLAYLIST_TYPE);
                    }
                }
            }

            return state.buildPlaylist();
        } finally {
            mScanner.close();
        }
    }

    private void putHandlers(ExtTagHandler ... handlers) {
        if (handlers != null) {
            for (ExtTagHandler handler : handlers) {
                mExtTagHandlers.put(handler.getTag(), handler);
            }
        }
    }

    private void checkWhitespace(final String line) throws ParseException {
        if (!isComment(line)) {
            if (line.length() != line.trim().length()) {
                throw new ParseException(ParseExceptionType.WHITESPACE_IN_TRACK);
            }
        }
    }

    private boolean isComment(final String line) {
        return line.indexOf(Constants.COMMENT_PREFIX) == 0 && !isExtTag(line);
    }

    private boolean isExtTag(final String line) {
        return line.indexOf(Constants.EXT_TAG_PREFIX) == 0;
    }

    private String getExtTagKey(final String line) {
        int index = line.indexOf(Constants.EXT_TAG_END);

        if (index == -1) {
            return line.substring(1);
        } else {
            return line.substring(1, index);
        }
    }
}
