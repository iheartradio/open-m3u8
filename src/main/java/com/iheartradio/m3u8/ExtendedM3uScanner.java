package com.iheartradio.m3u8;

import java.io.Closeable;
import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class ExtendedM3uScanner implements Closeable {
    private final Scanner mScanner;
    private final StringBuilder mInput = new StringBuilder();

    private boolean mClosed = false;

    ExtendedM3uScanner(InputStream inputStream, Encoding encoding) {
        mScanner = new Scanner(inputStream, encoding.getValue()).useLocale(Locale.US).useDelimiter(Constants.PARSE_NEW_LINE);
    }

    @Override
    public void close() {
        mScanner.close();
        mClosed = true;
    }

    String getInput() {
        return mInput.toString();
    }

    boolean hasNext() {
        return !mClosed && mScanner.hasNext();
    }

    String next() throws ParseException {
        String line = mScanner.next();
        mInput.append(line);
        return line;
    }
}
