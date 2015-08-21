package com.iheartradio.m3u8;

/**
 * Represents a syntactic error in the input that prevented further parsing.
 */
public class ParseException extends Exception {
    private static final long serialVersionUID = -2217152001086567983L;

    private final String mMessageSuffix;

    public final ParseExceptionType type;

    private String mInput;

    static ParseException create(ParseExceptionType type, String tag) {
        return create(type, tag, null);
    }

    static ParseException create(ParseExceptionType type, String tag, String context) {
        final StringBuilder builder = new StringBuilder();

        if (tag != null) {
            builder.append(tag);
        }

        if (context != null) {
            if (builder.length() > 0) {
                builder.append(" - ");
            }

            builder.append(context);
        }

        if (builder.length() > 0) {
            return new ParseException(type, builder.toString());
        } else {
            return new ParseException(type);
        }
    }

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
