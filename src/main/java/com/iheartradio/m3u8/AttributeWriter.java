package com.iheartradio.m3u8;

interface AttributeWriter<T> {

    String write(T attributes) throws ParseException;

    boolean containsAttribute(T attributes);
}
