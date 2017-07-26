package com.iheartradio.m3u8.data;

import java.util.HashMap;
import java.util.Map;

public enum MediaType {
    AUDIO("AUDIO"),
    VIDEO("VIDEO"),
    SUBTITLES("SUBTITLES"),
    CLOSED_CAPTIONS("CLOSED-CAPTIONS");

    private static final Map<String, MediaType> sMap = new HashMap<String, MediaType>();

    private final String value;

    static {
        for (MediaType mediaType : MediaType.values()) {
            sMap.put(mediaType.value, mediaType);
        }
    }

    private MediaType(String value) {
        this.value = value;
    }

    public static MediaType fromValue(String value) {
        return sMap.get(value);
    }
    
    public String getValue() {
        return value;
    }
}
