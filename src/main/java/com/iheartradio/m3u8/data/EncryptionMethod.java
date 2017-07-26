package com.iheartradio.m3u8.data;

import java.util.HashMap;
import java.util.Map;

public enum EncryptionMethod {
    NONE("NONE"),
    AES("AES-128"),
    SAMPLE_AES("SAMPLE-AES");

    private static final Map<String, EncryptionMethod> sMap = new HashMap<String, EncryptionMethod>();

    private final String value;

    static {
        for (EncryptionMethod encryptionMethod : EncryptionMethod.values()) {
            sMap.put(encryptionMethod.value, encryptionMethod);
        }
    }

    private EncryptionMethod(String value) {
        this.value = value;
    }

    public static EncryptionMethod fromValue(String value) {
        return sMap.get(value);
    }
    
    public String getValue() {
        return this.value;
    }
}
