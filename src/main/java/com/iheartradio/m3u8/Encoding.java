package com.iheartradio.m3u8;

public enum Encoding {
    UTF_8("utf-8"),
    WINDOWS_1252("windows-1252");

    final String value;

    private Encoding(String value) {
        this.value = value;
    }
}
