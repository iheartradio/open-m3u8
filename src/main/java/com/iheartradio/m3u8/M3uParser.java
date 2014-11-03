package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.MediaPlaylist;
import com.iheartradio.m3u8.data.Playlist;

import java.io.InputStream;
import java.util.Scanner;

class M3uParser {
    private final Scanner mScanner;

    M3uParser(InputStream inputStream, Encoding encoding) {
        mScanner = new Scanner(inputStream, encoding.value).useDelimiter(Constants.EOL_PATTERN);
    }

    Playlist parse() throws ParseException {
        final ParseState state = new ParseState();
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
                    .withMediaPlaylist(new MediaPlaylist(state.getMedia().tracks))
                    .build();
        } finally {
            mScanner.close();
        }
    }

    private void validateLine(final String line) throws ParseException {
        if (!isComment(line)) {
            if (line.length() != line.trim().length()) {
                throw new ParseException(ParseExceptionType.WHITESPACE_IN_TRACK);
            }
        }
    }

    private boolean isComment(final String line) {
        return line.indexOf(Constants.COMMENT_PREFIX) == 0;
    }
}
