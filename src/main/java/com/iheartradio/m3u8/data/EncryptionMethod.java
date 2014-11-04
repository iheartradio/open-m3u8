package com.iheartradio.m3u8.data;

public enum EncryptionMethod {
    NONE("NONE"),
    AES("AES-128"),
    SAMPLE_AES("SAMPLE-AES");

    private final String value;

    private EncryptionMethod(String value) {
        this.value = value;
    }

    public static EncryptionMethod fromValue(String value) {
        if (NONE.value.equals(value)) {
            return NONE;
        } else if (AES.value.equals(value)) {
            return AES;
        } else if (SAMPLE_AES.value.equals(value)) {
            return SAMPLE_AES;
        } else {
            return null;
        }
    }
}
