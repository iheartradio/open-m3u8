package com.iheartradio.m3u8;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.iheartradio.m3u8.data.Resolution;

public class WriteUtil {

    public static String writeYesNo(boolean yes) {
        if (yes) {
            return Constants.YES;
        } else {
            return Constants.NO;
        }
    }

    public static String writeHexadecimal(List<Byte> hex) {
        if (hex == null || hex.size() == 0) {
           throw new IllegalArgumentException("hex might not be null or empty!");
        }
        
        final String prefix = "0x";
        StringBuilder builder = new StringBuilder(hex.size() + prefix.length());
        builder.append(prefix);
        for(Byte b : hex) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static String writeResolution(Resolution r) {
        return r.width + "x" + r.height;
    }

    public static String writeQuotedString(String unquotedString, String tag) throws ParseException {
        return writeQuotedString(unquotedString, tag, false);
    }
    public static String writeQuotedString(String unquotedString, String tag, boolean optional) throws ParseException {
        if (unquotedString != null || !optional) {
            final StringBuilder builder = new StringBuilder(unquotedString.length() + 2);
            builder.append("\"");

            for (int i = 0; i < unquotedString.length(); ++i) {
                final char c = unquotedString.charAt(i);

                if (i == 0 && ParseUtil.isWhitespace(c)) {
                    throw new ParseException(ParseExceptionType.ILLEGAL_WHITESPACE, tag);
                } else if (c == '"') {
                    builder.append('\\').append(c);
                } else {
                    builder.append(c);
                }
            }

            builder.append("\"");
            return builder.toString();
        }

        return "\"\"";
    }

    public static String encodeUri(String decodedUri) throws ParseException {
        try {
            return URLEncoder.encode(decodedUri.replace("%2B", "+"), "utf-8");
        } catch (UnsupportedEncodingException exception) {
            throw new ParseException(ParseExceptionType.INTERNAL_ERROR);
        }
    }

    public static String join(List<? extends Object> valueList, String separator) {
        if (valueList == null || valueList.size() == 0) {
            throw new IllegalArgumentException("valueList might not be null or empty!");
        }
        if (separator == null) {
            throw new IllegalArgumentException("separator might not be null!");
        }
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < valueList.size(); i++) {
            sb.append(valueList.get(i).toString());
            if (i + 1 < valueList.size()) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

}
