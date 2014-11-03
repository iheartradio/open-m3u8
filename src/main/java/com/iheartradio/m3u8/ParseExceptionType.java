package com.iheartradio.m3u8;

public enum ParseExceptionType {
    BAD_EXT_TAG_FORMAT("bad format found for an EXT tag"),
    INVALID_COMPATIBILITY_VERSION("invalid compatibility version"),
    MASTER_IN_MEDIA("master playlist tags we found in a media playlist"),
    MEDIA_IN_MASTER("media playlist tags we found in a master playlist"),
    MISSING_EXT_TAG_SEPARATOR("missing the colon after an EXT tag"),
    MULTIPLE_EXT_TAGS("multiple tags found for an EXT tag"),
    UNKNOWN_PLAYLIST_TYPE("unable to determine playlist type"),
    UNSUPPORTED_EXT_TAG_DETECTED("unsupported ext tag detected"),
    WHITESPACE_IN_TRACK("whitespace was found surrounding a track");

    final String message;

    private ParseExceptionType(String message) {
        this.message = message;
    }
}
