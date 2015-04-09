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

    // master playlist tags

    public static final String EXT_X_MEDIA_TAG = "EXT-X-MEDIA";
    public static final String EXT_X_STREAM_INF_TAG = "EXT-X-STREAM-INF";
    public static final String EXT_X_I_FRAME_STREAM_INF_TAG = "EXT-X-I-FRAME-STREAM-INF";

    // media playlist tags
    
    public static final String EXT_X_PLAYLIST_TYPE_TAG = "EXT-X-PLAYLIST-TYPE";
    public static final String EXT_X_TARGETDURATION_TAG = "EXT-X-TARGETDURATION";
    public static final String EXT_X_MEDIA_SEQUENCE_TAG = "EXT-X-MEDIA-SEQUENCE";
    public static final String EXT_X_ALLOW_CACHE_TAG = "EXT-X-ALLOW-CACHE";

    // media segment tags

    public static final String EXTINF_TAG = "EXTINF";
    public static final String EXT_X_KEY_TAG = "EXT-X-KEY";

    // regular expressions

    private static final String INTEGER_REGEX = "\\d+";
    private static final String FLOAT_REGEX = "\\d+\\.?\\d*";

    public static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("^0[x|X]([0-9A-F]+)$");
    public static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(" + INTEGER_REGEX + ")x(" + INTEGER_REGEX + ")$");
    public static final Pattern URL_PATTERN = Pattern.compile("^(?:https?|ftp)://[^\\s/$.?#]*\\.[^\\s]*$");

    public static final Pattern EXT_X_VERSION_PATTERN = Pattern.compile("^#" + EXT_X_VERSION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_TARGETDURATION_PATTERN = Pattern.compile("^#" + EXT_X_TARGETDURATION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_MEDIA_SEQUENCE_PATTERN = Pattern.compile("^#" + EXT_X_MEDIA_SEQUENCE_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_PLAYLIST_TYPE_PATTERN  = Pattern.compile("^#" + EXT_X_PLAYLIST_TYPE_TAG + EXT_TAG_END + "(EVENT|VOD)$");
    public static final Pattern EXT_X_MEDIA_IN_STREAM_ID_PATTERN = Pattern.compile("^CC[1-4]|SERVICE(?:[1-9]|[1-5]\\d|6[0-3])$");
    public static final Pattern EXTINF_PATTERN = Pattern.compile("^#" + EXTINF_TAG + EXT_TAG_END + "(" + FLOAT_REGEX + ")(?:,(.+)?)?$");

    // other

    public static final int MAX_COMPATIBILITY_VERSION = 3;
    public static final int IV_SIZE = 16;
    //Against the spec but used by Adobe
    public static final int IV_SIZE_ALTERNATIVE = 32;
    public static final String DEFAULT_KEY_FORMAT = "identity";
    public static final String NO_CLOSED_CAPTIONS = "NONE";
    public static final List<Integer> DEFAULT_KEY_FORMAT_VERSIONS = Arrays.asList(1);
}
