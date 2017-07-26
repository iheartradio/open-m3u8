package com.iheartradio.m3u8.data;

import java.util.HashMap;
import java.util.Map;

public enum PlaylistType {
    EVENT("EVENT"), VOD("VOD");
    
    private static final Map<String, PlaylistType> sMap = new HashMap<String, PlaylistType>();

    private final String value;

    static {
        for (PlaylistType mediaType : PlaylistType.values()) {
            sMap.put(mediaType.value, mediaType);
        }
    }

    private PlaylistType(String value) {
        this.value = value;
    }

    public static PlaylistType fromValue(String value) {
        return sMap.get(value);
    }
    
    public String getValue() {
        return value;
    }
}
