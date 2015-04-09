package com.iheartradio.m3u8;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.iheartradio.m3u8.data.Playlist;

class ExtendedM3uParser {
    private final ExtendedM3uScanner mScanner;
    private final Encoding mEncoding;
    private final Map<String, IExtTagParser> mExtTagParsers = new HashMap<String, IExtTagParser>();

    ExtendedM3uParser(InputStream inputStream, Encoding encoding) {
        mScanner = new ExtendedM3uScanner(inputStream, encoding);
        mEncoding = encoding;

        // TODO implement the EXT tag handlers and add them here
        putParsers(
                ExtTagParser.EXTM3U_HANDLER,
                ExtTagParser.EXT_X_VERSION_HANDLER,
                MediaPlaylistTagParser.EXT_X_PLAYLIST_TYPE,
                MediaPlaylistTagParser.EXT_X_KEY,
                MediaPlaylistTagParser.EXT_X_TARGETDURATION,
                MediaPlaylistTagParser.EXT_X_START,
                MediaPlaylistTagParser.EXT_X_MEDIA_SEQUENCE,
                MediaPlaylistTagParser.EXT_X_I_FRAMES_ONLY,
                MasterPlaylistTagParser.EXT_X_MEDIA,
                MediaPlaylistTagParser.EXT_X_ALLOW_CACHE,
                MasterPlaylistTagParser.EXT_X_STREAM_INF,
                MasterPlaylistTagParser.EXT_X_I_FRAME_STREAM_INF,
                MediaPlaylistTagParser.EXTINF,
                MediaPlaylistTagParser.EXT_X_ENDLIST
        );
    }

    Playlist parse(ParsingMode parsingMode) throws ParseException {
        final ParseState state = new ParseState(mEncoding);
        final LineParser playlistParser = new PlaylistLineParser();
        final LineParser trackLineParser = new TrackLineParser();

        try {
            while (mScanner.hasNext()) {
                final String line = mScanner.next();
                checkWhitespace(line);

                if (line.length() == 0 || isComment(line)) {
                    continue;
                } else {
                    if (isExtTag(line)) {
                        final String tagKey = getExtTagKey(line);
                        IExtTagParser handler = mExtTagParsers.get(tagKey);

                        if (handler == null) {
                            //To support forward compatibility, when parsing Playlists, Clients
                            //MUST:
                            //o  ignore any unrecognized tags.
                            switch(parsingMode) {
                                case STRICT:
                                    throw ParseException.create(ParseExceptionType.UNSUPPORTED_EXT_TAG_DETECTED, tagKey, line);
                                case LENIENT:
                                default:
                                    handler = ExtTagParser.EXT_UNKNOWN_HANDLER;
                                    break;
                            }
                        } 
                        handler.parse(line, state);
                        
                    } else if (state.isMaster()) {
                        playlistParser.parse(line, state);
                    } else if (state.isMedia()) {
                        trackLineParser.parse(line, state);
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

    private void putParsers(IExtTagParser... parsers) {
        if (parsers != null) {
            for (IExtTagParser parser : parsers) {
                mExtTagParsers.put(parser.getTag(), parser);
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
