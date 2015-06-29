package com.iheartradio.m3u8;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

class M3uScanner {
    private final Scanner mScanner;
    private final boolean mSupportsByteOrderMark;
    private final StringBuilder mInput = new StringBuilder();

    private boolean mCheckedByteOrderMark;

    M3uScanner(InputStream inputStream, Encoding encoding) {
        mScanner = new Scanner(inputStream, encoding.value).useLocale(Locale.US).useDelimiter(Constants.PARSE_NEW_LINE);
        mSupportsByteOrderMark = encoding.supportsByteOrderMark;
    }

    String getInput() {
        return mInput.toString();
    }

    boolean hasNext() {
        return mScanner.hasNext();
    }

    String next() throws ParseException {
        String line = mScanner.next();

        if (mSupportsByteOrderMark && !mCheckedByteOrderMark) {
            if (!line.isEmpty() && line.charAt(0) == Constants.UNICODE_BOM) {
                line = line.substring(1);
            }

            mCheckedByteOrderMark = true;
        }

        mInput.append(line).append("\n");
        return line;
    }
}
