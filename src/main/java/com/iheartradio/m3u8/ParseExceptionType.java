package com.iheartradio.m3u8;

public enum ParseExceptionType {
    MASTER_IN_MEDIA("master playlist tags we found in a media playlist"),
    MEDIA_IN_MASTER("media playlist tags we found in a master playlist"),
    UNKNOWN_PLAYLIST_TYPE("unable to determine playlist type"),
    UNSUPPORTED_EXT_TAG_DETECTED("unsupported ext tag detected"),
    WHITESPACE_IN_TRACK("whitespace was found surrounding a track");

    public final String message;

    private ParseExceptionType(String message) {
        this.message = message;
    }
}
