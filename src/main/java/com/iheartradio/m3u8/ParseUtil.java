package com.iheartradio.m3u8;

final class ParseUtil {
    public static int parseInt(String string, String tag) throws ParseException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            throw new ParseException(ParseExceptionType.INTEGER_TOO_LARGE, tag);
        }
    }
}
