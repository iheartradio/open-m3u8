package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.InputStream;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;

class M3uParser implements IPlaylistParser {
    private final M3uScanner mScanner;
    private final Encoding mEncoding;

    M3uParser(InputStream inputStream, Encoding encoding) {
        mScanner = new M3uScanner(inputStream, encoding);
        mEncoding = encoding;
    }

    @Override
    public Playlist parse() throws IOException, ParseException {
        final ParseState state = new ParseState(mEncoding);
        final TrackLineParser trackLineParser = new TrackLineParser();

        try {
            state.setMedia();

            while (mScanner.hasNext()) {
                final String line = mScanner.next();
                validateLine(line);

                if (line.length() == 0 || isComment(line)) {
                    continue;
                } else {
                    trackLineParser.parse(line, state);
                }
            }

            return new Playlist.Builder()
                    .withMediaPlaylist(new MediaPlaylist.Builder()
                            .withTracks(state.getMedia().tracks)
                            .build())
                    .build();
        } catch (ParseException exception) {
            exception.setInput(mScanner.getInput());
            throw exception;
        }
    }

    private void validateLine(final String line) throws ParseException {
        if (!isComment(line)) {
            if (line.length() != line.trim().length()) {
                throw ParseException.create(ParseExceptionType.WHITESPACE_IN_TRACK, line, "" + line.length());
            }
        }
    }

    private boolean isComment(final String line) {
        return line.indexOf(Constants.COMMENT_PREFIX) == 0;
    }
}
