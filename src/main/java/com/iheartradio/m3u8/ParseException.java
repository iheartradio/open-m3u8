package com.iheartradio.m3u8;

public class ParseException extends Exception {
    public final ParseExceptionType type;

    ParseException(ParseExceptionType type) {
        this.type = type;
    }
}
