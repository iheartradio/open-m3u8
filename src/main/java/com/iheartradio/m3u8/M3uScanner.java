package com.iheartradio.m3u8;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

class M3uScanner {
    private final Scanner mScanner;
    private final StringBuilder mInput = new StringBuilder();

    M3uScanner(InputStream inputStream, Encoding encoding) {
        mScanner = new Scanner(inputStream, encoding.getValue()).useLocale(Locale.US).useDelimiter(Constants.PARSE_NEW_LINE);
    }

    String getInput() {
        return mInput.toString();
    }

    boolean hasNext() {
        return mScanner.hasNext();
    }

    String next() throws ParseException {
        String line = mScanner.next();
        mInput.append(line).append("\n");
        return line;
    }
}
