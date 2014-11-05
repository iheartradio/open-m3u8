package com.iheartradio.m3u8;

interface AttributeHandler<Builder> {
    void handle(Attribute attribute, Builder builder, ParseState state) throws ParseException;
}
