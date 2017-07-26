package com.iheartradio.m3u8.data;

import java.util.Collections;
import java.util.List;

class DataUtil {
    static <T> List<T> emptyOrUnmodifiable(final List<T> list) {
        return list == null ? Collections.<T>emptyList() : Collections.unmodifiableList(list);
    }
}
