package com.iheartradio.m3u8;

public enum ParseExceptionType {
    AUTO_SELECT_DISABLED_FOR_DEFAULT("default media data must be auto selected"),
    BAD_EXT_TAG_FORMAT("bad format found for an EXT tag"),
    EMPTY_MEDIA_CHANNELS("CHANNELS is empty"),
    EMPTY_MEDIA_CHARACTERISTICS("CHARACTERISTICS is empty"),
    EMPTY_MEDIA_GROUP_ID("GROUP-ID is empty"),
    EMPTY_MEDIA_NAME("NAME is empty"),
    ILLEGAL_WHITESPACE("found illegal whitespace"),
    INTERNAL_ERROR("there was an unrecoverable problem"),
    INVALID_ATTRIBUTE_NAME("invalid attribute name"),
    INVALID_COMPATIBILITY_VERSION("invalid compatibility version"),
    INVALID_ENCRYPTION_METHOD("invalid encryption method"),
    INVALID_HEXADECIMAL_STRING("a hexadecimal string was not properly formatted"),
    INVALID_IV_SIZE("the initialization vector is the wrong size"),
    INVALID_KEY_FORMAT_VERSIONS("invalid KEYFORMATVERSIONS"),
    INVALID_MEDIA_IN_STREAM_ID("invalid media INSTREAM-ID"),
    INVALID_MEDIA_TYPE("invalid media TYPE"),
    INVALID_RESOLUTION_FORMAT("a resolution was not formatted properly"),
    INVALID_QUOTED_STRING("a quoted string was not properly formatted"),
    INVALID_DATE_TIME_FORMAT("a date-time string was not properly formatted"),
    INVALID_BYTERANGE_FORMAT("a byte range string was not properly formatted"),
    MASTER_IN_MEDIA("master playlist tags we found in a media playlist"),
    MEDIA_IN_MASTER("media playlist tags we found in a master playlist"),
    MISSING_ATTRIBUTE_NAME("missing the name of an attribute"),
    MISSING_ATTRIBUTE_VALUE("missing the value of an attribute"),
    MISSING_ATTRIBUTE_SEPARATOR("missing the separator in an attribute"),
    MISSING_ENCRYPTION_URI("missing the URI for encrypted media segments"),
    MISSING_EXT_TAG_SEPARATOR("missing the colon after an EXT tag"),
    MISSING_TRACK_INFO("missing EXTINF for a track in an extended media playlist"),
    MULTIPLE_ATTRIBUTE_NAME_INSTANCES("multiple instances of an attribute name found in an attribute list"),
    MULTIPLE_EXT_TAG_INSTANCES("multiple instances of an EXT tag found for which only one is allowed"),
    NOT_JAVA_INTEGER("only java integers are supported"),
    NOT_JAVA_ENUM("only specific values are supported"),
    NOT_JAVA_FLOAT("only java floats are supported"),
    NOT_YES_OR_NO("the only valid values are YES and NO"),
    UNCLOSED_QUOTED_STRING("a quoted string was not closed"),
    UNKNOWN_PLAYLIST_TYPE("unable to determine playlist type"),
    UNSUPPORTED_COMPATIBILITY_VERSION("open m3u8 does not support this version"),
    UNSUPPORTED_EXT_TAG_DETECTED("unsupported ext tag detected"),
    WHITESPACE_IN_TRACK("whitespace was found surrounding a track"), 
    REQUIRES_PROTOCOL_VERSION_4_OR_HIGHER("A Media Playlist REQUIRES protocol version 4 or higher");

    final String message;

    private ParseExceptionType(String message) {
        this.message = message;
    }
}
