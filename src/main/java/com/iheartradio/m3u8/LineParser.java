package com.iheartradio.m3u8;

interface LineParser {
    void parse(String line, ParseState state) throws ParseException;
}
