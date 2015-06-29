package com.iheartradio.m3u8;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Encoding {
    UTF_8("utf-8", true),
    WINDOWS_1252("windows-1252", false);

    private static final Map<String, Encoding> sMap = new HashMap<String, Encoding>();

    final String value;
    final boolean supportsByteOrderMark;

    static {
        for (Encoding mediaType : Encoding.values()) {
            sMap.put(mediaType.value, mediaType);
        }
    }

    private Encoding(String value, boolean supportsByteOrderMark) {
        this.value = value;
        this.supportsByteOrderMark = supportsByteOrderMark;
    }

    /**
     * @return the encoding for the given value if supported, if the encoding is unsupported or null, null will be returned
     */
    public static Encoding fromValue(String value) {
        if (value == null) {
            return null;
        } else {
            return sMap.get(value.toLowerCase(Locale.US));
        }
    }

    public String getValue() {
        return value;
    }
}
