package com.iheartradio.m3u8;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Encoding {
    UTF_8("utf-8"),
    WINDOWS_1252("windows-1252");

    private static final Map<String, Encoding> sMap = new HashMap<String, Encoding>();

    private final String value;

    static {
        for (Encoding mediaType : Encoding.values()) {
            sMap.put(mediaType.value, mediaType);
        }
    }

    private Encoding(String value) {
        this.value = value;
    }

    public static Encoding fromValue(String value) {
        if (value == null) {
            return null;
        }
        String valueInLowerCase = value.toLowerCase(Locale.US);
        return sMap.get(valueInLowerCase);
    }
    
    public String getValue() {
        return value;
    }
}
