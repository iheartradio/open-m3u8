package com.iheartradio.m3u8;

import java.util.regex.Pattern;

final class Constants {
    public static final Pattern EOL_PATTERN = Pattern.compile("\\n|\\r\\n");

    public static final String COMMENT_PREFIX = "#";
    public static final String EXT_TAG_PREFIX = "#EXT";
    public static final String EXT_TAG_END = ":";

    // extension tags
    public static final String EXTM3U_TAG = "EXTM3U";
    public static final String EXT_X_VERSION_TAG = "EXT-X-VERSION";
    public static final String EXT_X_TARGETDURATION = "EXT-X-TARGETDURATION";

    private static final String INTEGER_REGEX = "\\d+";

    // extension regular expressions
    public static final Pattern EXT_X_VERSION_PATTERN = Pattern.compile("^" + COMMENT_PREFIX + EXT_X_VERSION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_TARGETDURATION_PATTERN = Pattern.compile("^" + COMMENT_PREFIX + EXT_X_TARGETDURATION + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");

    public static final Pattern URL_PATTERN = Pattern.compile("(?:https?|ftp)://[^\\s/$.?#]*\\.[^\\s]*$");
}
