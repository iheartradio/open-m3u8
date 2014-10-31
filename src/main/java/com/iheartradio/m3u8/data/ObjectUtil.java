package com.iheartradio.m3u8.data;

final class ObjectUtil {
    public static boolean equals(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }
}
