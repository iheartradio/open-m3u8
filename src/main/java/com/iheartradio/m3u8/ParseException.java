package com.iheartradio.m3u8;

public class ParseException extends Exception {
    private final String mMessageSuffix;

    public final ParseExceptionType type;

    private String mInput;

    ParseException(ParseExceptionType type) {
        this(type, null);
    }

    ParseException(ParseExceptionType type, String messageSuffix) {
        this.type = type;
        mMessageSuffix = messageSuffix;
    }

    public String getInput() {
        return mInput;
    }

    void setInput(String input) {
        mInput = input;
    }

    public String getMessage() {
        if (mMessageSuffix == null) {
            return type.message;
        } else {
            return type.message + ": " + mMessageSuffix;
        }
    }
}
