package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;

import java.io.InputStream;

class M3uParser {
    private final ExtendedM3uScanner mScanner;
    private final Encoding mEncoding;

    M3uParser(InputStream inputStream, Encoding encoding) {
        mScanner = new ExtendedM3uScanner(inputStream, encoding);
        mEncoding = encoding;
    }

    Playlist parse() throws ParseException {
        final ParseState state = new ParseState(mEncoding);
        final TrackHandler trackHandler = new TrackHandler();

        try {
            state.setMedia();

            while (mScanner.hasNext()) {
                final String line = mScanner.next();
                validateLine(line);

                if (line.length() == 0 || isComment(line)) {
                    continue;
                } else {
                    trackHandler.handle(line, state);
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
        } finally {
            mScanner.close();
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
