package com.iheartradio.m3u8;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

final class Constants {
    public static final String MIME_TYPE = "application/vnd.apple.mpegurl";
    public static final String MIME_TYPE_COMPATIBILITY = "audio/mpegurl";
    
    public static final String ATTRIBUTE_SEPARATOR = "=";
    public static final char COMMA_CHAR = ',';
    public static final String COMMA = Character.toString(COMMA_CHAR);
    public static final String ATTRIBUTE_LIST_SEPARATOR = COMMA;
    public static final String LIST_SEPARATOR = "/";
    public static final String COMMENT_PREFIX = "#";
    public static final String EXT_TAG_PREFIX = "#EXT";
    public static final String EXT_TAG_END = ":";
    public static final String WRITE_NEW_LINE = "\n";
    public static final String PARSE_NEW_LINE = "\\r?\\n";

    // extension tags

    public static final String EXTM3U_TAG = "EXTM3U";
    public static final String EXT_X_VERSION_TAG = "EXT-X-VERSION";

    // master playlist tags

    public static  final String URI = "URI";
    public static final String BYTERANGE = "BYTERANGE";

    public static final String EXT_X_MEDIA_TAG = "EXT-X-MEDIA";
    public static  final String TYPE = "TYPE";
    public static  final String GROUP_ID = "GROUP-ID";
    public static  final String LANGUAGE = "LANGUAGE";
    public static  final String ASSOCIATED_LANGUAGE = "ASSOC-LANGUAGE";
    public static  final String NAME = "NAME";
    public static  final String DEFAULT = "DEFAULT";
    public static  final String AUTO_SELECT = "AUTOSELECT";
    public static  final String FORCED = "FORCED";
    public static  final String IN_STREAM_ID = "INSTREAM-ID";
    public static  final String CHARACTERISTICS = "CHARACTERISTICS";
    public static  final String CHANNELS = "CHANNELS";
    
    public static final String EXT_X_STREAM_INF_TAG = "EXT-X-STREAM-INF";
    public static final String EXT_X_I_FRAME_STREAM_INF_TAG = "EXT-X-I-FRAME-STREAM-INF";
    public static final String BANDWIDTH = "BANDWIDTH";
    public static final String AVERAGE_BANDWIDTH = "AVERAGE-BANDWIDTH";
    public static final String CODECS = "CODECS";
    public static final String RESOLUTION = "RESOLUTION";
    public static final String FRAME_RATE = "FRAME-RATE";
    public static final String VIDEO = "VIDEO";
    public static final String PROGRAM_ID = "PROGRAM-ID";

    public static final String AUDIO = "AUDIO";
    public static final String SUBTITLES = "SUBTITLES";
    public static final String CLOSED_CAPTIONS = "CLOSED-CAPTIONS";
    

    // media playlist tags
    
    public static final String EXT_X_PLAYLIST_TYPE_TAG = "EXT-X-PLAYLIST-TYPE";
    public static final String EXT_X_PROGRAM_DATE_TIME_TAG = "EXT-X-PROGRAM-DATE-TIME";
    public static final String EXT_X_TARGETDURATION_TAG = "EXT-X-TARGETDURATION";
    public static final String EXT_X_START_TAG = "EXT-X-START";
    public static final String TIME_OFFSET = "TIME-OFFSET";
    public static final String PRECISE = "PRECISE";
    
    public static final String EXT_X_MEDIA_SEQUENCE_TAG = "EXT-X-MEDIA-SEQUENCE";
    public static final String EXT_X_ALLOW_CACHE_TAG = "EXT-X-ALLOW-CACHE";
    public static final String EXT_X_ENDLIST_TAG = "EXT-X-ENDLIST";
    public static final String EXT_X_I_FRAMES_ONLY_TAG = "EXT-X-I-FRAMES-ONLY";
    public static final String EXT_X_DISCONTINUITY_TAG = "EXT-X-DISCONTINUITY";

    // media segment tags

    public static final String EXTINF_TAG = "EXTINF";
    public static final String EXT_X_KEY_TAG = "EXT-X-KEY";
    public static final String METHOD = "METHOD";
    public static final String IV = "IV";
    public static final String KEY_FORMAT = "KEYFORMAT";
    public static final String KEY_FORMAT_VERSIONS = "KEYFORMATVERSIONS";
    public static final String EXT_X_MAP = "EXT-X-MAP";
    public static final String EXT_X_BYTERANGE_TAG = "EXT-X-BYTERANGE";

    // regular expressions
    public static final String YES = "YES";
    public static final String NO = "NO";
    private static final String INTEGER_REGEX = "\\d+";
    private static final String SIGNED_FLOAT_REGEX = "-?\\d*\\.?\\d*";
    private static final String TIMESTAMP_REGEX = "\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{3})?(?:Z?|\\+\\d{2}:\\d{2})?";
    private static final String BYTERANGE_REGEX = "(" + INTEGER_REGEX + ")(?:@(" + INTEGER_REGEX + "))?";

    public static final Pattern HEXADECIMAL_PATTERN = Pattern.compile("^0[x|X]([0-9A-F]+)$");
    public static final Pattern RESOLUTION_PATTERN = Pattern.compile("^(" + INTEGER_REGEX + ")x(" + INTEGER_REGEX + ")$");

    public static final Pattern EXT_X_VERSION_PATTERN = Pattern.compile("^#" + EXT_X_VERSION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_TARGETDURATION_PATTERN = Pattern.compile("^#" + EXT_X_TARGETDURATION_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_MEDIA_SEQUENCE_PATTERN = Pattern.compile("^#" + EXT_X_MEDIA_SEQUENCE_TAG + EXT_TAG_END + "(" + INTEGER_REGEX + ")$");
    public static final Pattern EXT_X_PLAYLIST_TYPE_PATTERN  = Pattern.compile("^#" + EXT_X_PLAYLIST_TYPE_TAG + EXT_TAG_END + "(EVENT|VOD)$");
    public static final Pattern EXT_X_PROGRAM_DATE_TIME_PATTERN  = Pattern.compile("^#" + EXT_X_PROGRAM_DATE_TIME_TAG + EXT_TAG_END + "(" + TIMESTAMP_REGEX + ")$");
    public static final Pattern EXT_X_MEDIA_IN_STREAM_ID_PATTERN = Pattern.compile("^CC[1-4]|SERVICE(?:[1-9]|[1-5]\\d|6[0-3])$");
    public static final Pattern EXTINF_PATTERN = Pattern.compile("^#" + EXTINF_TAG + EXT_TAG_END + "(" + SIGNED_FLOAT_REGEX + ")(?:,(.+)?)?$");
    public static final Pattern EXT_X_ENDLIST_PATTERN = Pattern.compile("^#" + EXT_X_ENDLIST_TAG + "$");
    public static final Pattern EXT_X_I_FRAMES_ONLY_PATTERN = Pattern.compile("^#" + EXT_X_I_FRAMES_ONLY_TAG);
    public static final Pattern EXT_X_DISCONTINUITY_PATTERN = Pattern.compile("^#" + EXT_X_DISCONTINUITY_TAG + "$");
    public static final Pattern EXT_X_BYTERANGE_PATTERN = Pattern.compile("^#" + EXT_X_BYTERANGE_TAG + EXT_TAG_END + BYTERANGE_REGEX + "$");
    public static final Pattern EXT_X_BYTERANGE_VALUE_PATTERN = Pattern.compile("^" + BYTERANGE_REGEX + "$");

    // other

    public static final int[] UTF_8_BOM_BYTES = {0xEF, 0xBB, 0xBF};
    public static final char UNICODE_BOM = '\uFEFF';
    public static final int MAX_COMPATIBILITY_VERSION = 5;
    public static final int IV_SIZE = 16;
    //Against the spec but used by Adobe
    public static final int IV_SIZE_ALTERNATIVE = 32;
    public static final String DEFAULT_KEY_FORMAT = "identity";
    public static final String NO_CLOSED_CAPTIONS = "NONE";
    public static final List<Integer> DEFAULT_KEY_FORMAT_VERSIONS = Arrays.asList(1);
}
