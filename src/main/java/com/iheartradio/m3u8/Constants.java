package com.iheartradio.m3u8;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

final class Constants {
    public static final String ATTRIBUTE_SEPARATOR = "=";
    public static final String COMMENT_PREFIX = "#";
    public static final String EXT_TAG_PREFIX = "#EXT";
    public static final String EXT_TAG_END = ":";

    // extension tags

    public static final String EXTM3U_TAG = "EXTM3U";
    public static final String EXT_X_VERSION_TAG = "EXT-X-VERSION";
    public static final String EXT_X_TARGETDURATION_TAG = "EXT-X-TARGETDURATION";
    public static final String EXTINF_TAG = "EXTINF";
    public static final String EXT_X_KEY_TAG = "EXT-X-KEY";

    // regular expressions

    private static final String INTEGER_REGEX = "\\d+";
    private static final String FLOAT_REGEX = "\\d+\\.?\\d*";

    public static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("^0[x|X]([0-9A-F]+)$");
    public static final Pattern URL_PATTERN = Pattern.compile("(?:https?|ftp)://[^\\s/$.?#]*\\.[^\\s]*$");

    public static final Pattern EXT_X_VERSION_PATTERN = Pattern.compile("^#" + EXT_X_VERSION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_TARGETDURATION_PATTERN = Pattern.compile("^#" + EXT_X_TARGETDURATION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXTINF_PATTERN = Pattern.compile("^#" + EXTINF_TAG + EXT_TAG_END + "(" + FLOAT_REGEX + ")(?:,(.+)?)?$");

    // other

    public static final int MAX_COMPATIBILITY_VERSION = 3;
    public static final int IV_SIZE = 16;
    public static final String DEFAULT_KEY_FORMAT = "identity";
    public static final List<Integer> DEFAULT_KEY_FORMAT_VERSIONS = Arrays.asList(1);
}
