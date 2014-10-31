package com.iheartradio.m3u8;

import java.util.regex.Pattern;

final class Constants {
    public static final Pattern EOL_PATTERN = Pattern.compile("\\n|\\r\\n");

    public static final String COMMENT_PREFIX = "#";
    public static final String EXT_TAG_PREFIX = "#EXT";
    public static final String EXT_TAG_END = ":";

    public static Pattern URL_PATTERN = Pattern.compile("(?:https?|ftp)://[^\\s/$.?#]*\\.[^\\s]*$");
}
