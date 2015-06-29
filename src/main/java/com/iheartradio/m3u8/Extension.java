package com.iheartradio.m3u8;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public enum Extension {
    M3U("m3u", Encoding.WINDOWS_1252),
    M3U8("m3u8", Encoding.UTF_8);

    private static final Map<String, Extension> sMap = new HashMap<String, Extension>();

    static {
        for (Extension mediaType : Extension.values()) {
            sMap.put(mediaType.value, mediaType);
        }
    }

    final String value;
    final Encoding encoding;

    private Extension(String value, Encoding encoding) {
        this.value = value;
        this.encoding = encoding;
    }

    /**
     * @return the extension for the given value if supported, if the extension is unsupported or null, null will be returned
     */
    public static Extension fromValue(String value) {
        if (value == null) {
            return null;
        } else {
            return sMap.get(value.toLowerCase(Locale.US));
        }
    }

    public String getValue() {
        return value;
    }

    public Encoding getEncoding() {
        return encoding;
    }
}
