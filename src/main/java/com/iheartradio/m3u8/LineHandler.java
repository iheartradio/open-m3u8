package com.iheartradio.m3u8;

interface LineHandler {
    void handle(String line, ParseState state);
}
