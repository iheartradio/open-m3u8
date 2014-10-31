package com.iheartradio.m3u8;

public enum Extension {
    M3U("m3u", Encoding.WINDOWS_1252),
    M3U8("m3u8", Encoding.UTF_8);

    final String value;
    final Encoding encoding;

    private Extension(String value, Encoding encoding) {
        this.value = value;
        this.encoding = encoding;
    }
}
