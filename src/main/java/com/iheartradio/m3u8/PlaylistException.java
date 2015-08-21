package com.iheartradio.m3u8;

import java.util.Set;

/**
 * Represents a playlist with invalid data.
 */
public class PlaylistException extends Exception {
    private static final long serialVersionUID = 7426782115004559238L;

    private final String mInput;
    private final Set<PlaylistError> mErrors;

    public PlaylistException(String input, Set<PlaylistError> errors) {
        mInput = input;
        mErrors = errors;
    }

    public String getInput() {
        return mInput;
    }

    public Set<PlaylistError> getErrors() {
        return mErrors;
    }
}
