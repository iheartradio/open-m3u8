package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.Playlist;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

class ExtendedM3uParser extends BaseM3uParser {
    private final ParsingMode mParsingMode;
    private final Map<String, IExtTagParser> mExtTagParsers = new HashMap<String, IExtTagParser>();

    ExtendedM3uParser(InputStream inputStream, Encoding encoding, ParsingMode parsingMode) {
        super(inputStream, encoding);
        mParsingMode = parsingMode;

        // TODO implement the remaining EXT tag handlers and add them here
        putParsers(
                ExtLineParser.EXTM3U_HANDLER,
                ExtLineParser.EXT_X_VERSION_HANDLER,
                ExtLineParser.EXT_X_START,
                MediaPlaylistLineParser.EXT_X_PLAYLIST_TYPE,
                MediaPlaylistLineParser.EXT_X_PROGRAM_DATE_TIME,
                MediaPlaylistLineParser.EXT_X_KEY,
                MediaPlaylistLineParser.EXT_X_TARGETDURATION,
                MediaPlaylistLineParser.EXT_X_MEDIA_SEQUENCE,
                MediaPlaylistLineParser.EXT_X_I_FRAMES_ONLY,
                MasterPlaylistLineParser.EXT_X_MEDIA,
                MediaPlaylistLineParser.EXT_X_ALLOW_CACHE,
                MasterPlaylistLineParser.EXT_X_STREAM_INF,
                MasterPlaylistLineParser.EXT_X_I_FRAME_STREAM_INF,
                MediaPlaylistLineParser.EXTINF,
                MediaPlaylistLineParser.EXT_X_ENDLIST,
                MediaPlaylistLineParser.EXT_X_DISCONTINUITY,
                MediaPlaylistLineParser.EXT_X_MAP,
                MediaPlaylistLineParser.EXT_X_BYTERANGE
        );
    }

    @Override
    public Playlist parse() throws IOException, ParseException, PlaylistException {
        validateAvailable();

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
                        IExtTagParser tagParser = mExtTagParsers.get(tagKey);

                        if (tagParser == null) {
                            //To support forward compatibility, when parsing Playlists, Clients
                            //MUST:
                            //o  ignore any unrecognized tags.
                            if (mParsingMode.allowUnknownTags) {
                                tagParser = ExtLineParser.EXT_UNKNOWN_HANDLER;
                            } else {
                                throw ParseException.create(ParseExceptionType.UNSUPPORTED_EXT_TAG_DETECTED, tagKey, line);
                            }
                        }

                        tagParser.parse(line, state);

                        if (state.isMedia() && state.getMedia().endOfList) {
                            break;
                        }
                    } else if (state.isMaster()) {
                        playlistParser.parse(line, state);
                    } else if (state.isMedia()) {
                        trackLineParser.parse(line, state);
                    } else {
                        throw ParseException.create(ParseExceptionType.UNKNOWN_PLAYLIST_TYPE, line);
                    }
                }
            }

            final Playlist playlist = state.buildPlaylist();
            final PlaylistValidation validation = PlaylistValidation.from(playlist, mParsingMode);

            if (validation.isValid()) {
                return playlist;
            } else {
                throw new PlaylistException(mScanner.getInput(), validation.getErrors());
            }
        } catch (ParseException exception) {
            exception.setInput(mScanner.getInput());
            throw exception;
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
