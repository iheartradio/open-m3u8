package com.iheartradio.m3u8;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class ExtendedM3uScanner implements Closeable {
    private final Scanner mScanner;
    private final String mInput;

    private int mIndex = 0;
    private boolean mClosed = false;

    ExtendedM3uScanner(InputStream inputStream, Encoding encoding) {
        mScanner = new Scanner(inputStream, encoding.value).useLocale(Locale.US).useDelimiter("\\A");
        mInput = mScanner.next();
    }

    @Override
    public void close() {
        mScanner.close();
        mClosed = true;
    }

    public String getInput() {
        return mInput;
    }

    boolean hasNext() {
        return !mClosed && mIndex < mInput.length();
    }

    String next() throws ParseException {
        final StringBuilder builder = new StringBuilder();

        boolean isQuotedString = false;
        boolean isEscaping = false;
        boolean isCarriageReturn = false;

        for (; mIndex < mInput.length(); ++mIndex) {
            char next = mInput.charAt(mIndex);

            if (isQuotedString) {
                if (isEscaping) {
                    isEscaping = false;
                } else {
                    if (next == '\\') {
                        isEscaping = true;
                    } else if (next == '"') {
                        isQuotedString = false;
                    }
                }

                builder.append(next);
            } else {
                if (next == '\n') {
                    isCarriageReturn = false;
                    ++mIndex;
                    break;
                }

                if (isCarriageReturn) {
                    throw new ParseException(ParseExceptionType.ILLEGAL_CARRIAGE_RETURN);
                } else {
                    if (next == '"') {
                        isQuotedString = true;
                        builder.append(next);
                    } else if (next == '\r') {
                        isCarriageReturn = true;
                    } else {
                        builder.append(next);
                    }
                }
            }
        }

        if (isCarriageReturn) {
            throw new ParseException(ParseExceptionType.ILLEGAL_CARRIAGE_RETURN);
        }

        return builder.toString();
    }
}
