package com.iheartradio.m3u8;

public enum ParseExceptionType {
    BAD_EXT_TAG_FORMAT("bad format found for an EXT tag"),
    ILLEGAL_CARRIAGE_RETURN("found an illegal carriage return"),
    ILLEGAL_WHITESPACE("found illegal whitespace"),
    INTERNAL_ERROR("there was an unrecoverable problem"),
    INVALID_COMPATIBILITY_VERSION("invalid compatibility version"),
    INVALID_ENCRYPTION_METHOD("invalid encryption method"),
    INVALID_HEXADECIMAL_STRING("a hexadecimal string was not properly formatted"),
    INVALID_IV_SIZE("the initialization vector is the wrong size"),
    INVALID_KEY_FORMAT_VERSIONS("invalid key format versions"),
    INVALID_QUOTED_STRING("a quoted string was not properly formatted"),
    MASTER_IN_MEDIA("master playlist tags we found in a media playlist"),
    MEDIA_IN_MASTER("media playlist tags we found in a master playlist"),
    MISSING_ATTRIBUTE_NAME("missing the name of an attribute"),
    MISSING_ATTRIBUTE_VALUE("missing the value of an attribute"),
    MISSING_ATTRIBUTE_SEPARATOR("missing the separator in an attribute"),
    MISSING_ENCRYPTION_URI("missing the URI for encrypted media segments"),
    MISSING_EXT_TAG_SEPARATOR("missing the colon after an EXT tag"),
    MISSING_TRACK_INFO("missing track info for a track in an extended media playlist"),
    MULTIPLE_ATTRIBUTE_NAME_INSTANCES("multiple instances of an attribute name found in an attribute list"),
    MULTIPLE_EXT_TAG_INSTANCES("multiple instances of an EXT tag found for which only one is allowed"),
    NOT_JAVA_INTEGER("only java integers are supported"),
    NOT_JAVA_FLOAT("only java floats are supported"),
    UNCLOSED_QUOTED_STRING("a quoted string was not closed"),
    UNKNOWN_PLAYLIST_TYPE("unable to determine playlist type"),
    UNSUPPORTED_COMPATIBILITY_VERSION("open m3u8 does not support this version"),
    UNSUPPORTED_EXT_TAG_DETECTED("unsupported ext tag detected"),
    WHITESPACE_IN_TRACK("whitespace was found surrounding a track");

    final String message;

    private ParseExceptionType(String message) {
        this.message = message;
    }
}
