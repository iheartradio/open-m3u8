package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class ExtendedM3uParser {
    private final ExtendedM3uScanner mScanner;
    private final Encoding mEncoding;
    private final Map<String, IExtTagHandler> mExtTagHandlers = new HashMap<String, IExtTagHandler>();

    ExtendedM3uParser(InputStream inputStream, Encoding encoding) {
        mScanner = new ExtendedM3uScanner(inputStream, encoding);
        mEncoding = encoding;

        // TODO implement the EXT tag handlers and add them here
        putHandlers(
                ExtTagHandler.EXTM3U_HANDLER,
                ExtTagHandler.EXT_X_VERSION_HANDLER,
                MasterPlaylistTagHandler.EXT_X_MEDIA,
                MasterPlaylistTagHandler.EXT_X_STREAM_INF,
                MediaPlaylistTagHandler.EXT_X_TARGETDURATION,
                MediaPlaylistTagHandler.EXT_X_MEDIA_SEQUENCE,
                MediaPlaylistTagHandler.EXT_X_ALLOW_CACHE,
                MediaPlaylistTagHandler.EXT_X_PROGRAM_DATE_TIME,
                MediaPlaylistTagHandler.EXTINF,
                MediaPlaylistTagHandler.EXT_X_KEY
        );
    }

    Playlist parse() throws ParseException {
        final ParseState state = new ParseState(mEncoding);
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
                        final String tagKey = getExtTagKey(line);
                        final IExtTagHandler handler = mExtTagHandlers.get(tagKey);

                        if (handler == null) {
                            throw ParseException.create(ParseExceptionType.UNSUPPORTED_EXT_TAG_DETECTED, tagKey, line);
                        } else {
                            handler.handle(line, state);
                        }
                    } else if (state.isMaster()) {
                        playlistHandler.handle(line, state);
                    } else if (state.isMedia()) {
                        trackHandler.handle(line, state);
                    } else {
                        throw ParseException.create(ParseExceptionType.UNKNOWN_PLAYLIST_TYPE, line);
                    }
                }
            }

            return state.buildPlaylist();
        } catch (ParseException exception) {
            exception.setInput(mScanner.getInput());
            throw exception;
        } finally {
            mScanner.close();
        }
    }

    private void putHandlers(IExtTagHandler... handlers) {
        if (handlers != null) {
            for (IExtTagHandler handler : handlers) {
                mExtTagHandlers.put(handler.getTag(), handler);
            }
        }
    }

    private void checkWhitespace(final String line) throws ParseException {
        if (!isComment(line)) {
            if (line.length() != line.trim().length()) {
                throw ParseException.create(ParseExceptionType.WHITESPACE_IN_TRACK, line);
            }
        }
    }

    private boolean isComment(final String line) {
        return line.startsWith(Constants.COMMENT_PREFIX) && !isExtTag(line);
    }

    private boolean isExtTag(final String line) {
        return line.startsWith(Constants.EXT_TAG_PREFIX);
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
