package com.iheartradio.m3u8;

import com.iheartradio.m3u8.data.ByteRange;
import com.iheartradio.m3u8.data.Resolution;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class ParseUtil {
    public static int parseInt(String string, String tag) throws ParseException {
        try {
            return Integer.parseInt(string);
        } catch (NumberFormatException exception) {
            throw ParseException.create(ParseExceptionType.NOT_JAVA_INTEGER, tag, string);
        }
    }

    public static <T extends Enum<T>> T parseEnum(String string, Class<T> enumType, String tag) throws ParseException {
        try {
            return Enum.valueOf(enumType, string);
        } catch (IllegalArgumentException exception) {
            throw ParseException.create(ParseExceptionType.NOT_JAVA_ENUM, tag, string);
        }
    }

    public static String parseDateTime(String string, String tag) throws ParseException {
        Matcher matcher = Constants.EXT_X_PROGRAM_DATE_TIME_PATTERN.matcher(string);

        if (!matcher.matches()) {
            throw new ParseException(ParseExceptionType.INVALID_DATE_TIME_FORMAT, tag);
        }

        return matcher.group(1);
    }

    public static float parseFloat(String string, String tag) throws ParseException {
        try {
            return Float.parseFloat(string);
        } catch (NumberFormatException exception) {
            throw ParseException.create(ParseExceptionType.NOT_JAVA_FLOAT, tag, string);
        }
    }

    public static List<Byte> parseHexadecimal(String hexString, String tag) throws ParseException {
        final List<Byte> bytes = new ArrayList<Byte>();
        final Matcher matcher = Constants.HEXADECIMAL_PATTERN.matcher(hexString.toUpperCase(Locale.US));

        if (matcher.matches()) {
            String valueString = matcher.group(1);
            if (valueString.length() % 2 != 0) {
                throw ParseException.create(ParseExceptionType.INVALID_HEXADECIMAL_STRING, tag, hexString);
            }

            for (int i = 0; i < valueString.length(); i += 2) {
                bytes.add((byte)(Short.parseShort(valueString.substring(i, i+2), 16) & 0xFF));
            }

            return bytes;
        } else {
            throw ParseException.create(ParseExceptionType.INVALID_HEXADECIMAL_STRING, tag, hexString);
        }
    }

    private static byte hexCharToByte(char hex) {
        if (hex >= 'A') {
            return (byte) ((hex & 0xF) + 9);
        } else {
            return (byte) (hex & 0xF);
        }
    }

    public static boolean parseYesNo(Attribute attribute, String tag) throws ParseException {
        if (attribute.value.equals(Constants.YES)) {
            return true;
        } else if (attribute.value.equals(Constants.NO)) {
            return false;
        } else {
            throw ParseException.create(ParseExceptionType.NOT_YES_OR_NO, tag, attribute.toString());
        }
    }

    public static Resolution parseResolution(String resolutionString, String tag) throws ParseException {
        Matcher matcher = Constants.RESOLUTION_PATTERN.matcher(resolutionString);

        if (!matcher.matches()) {
            throw new ParseException(ParseExceptionType.INVALID_RESOLUTION_FORMAT, tag);
        }

        return new Resolution(parseInt(matcher.group(1), tag), parseInt(matcher.group(2), tag));
    }

    public static String parseQuotedString(String quotedString, String tag) throws ParseException {
        final StringBuilder builder = new StringBuilder();

        boolean isEscaping = false;
        int quotesFound = 0;

        for (int i = 0; i < quotedString.length(); ++i) {
            final char c = quotedString.charAt(i);

            if (i == 0 && c != '"') {
                if (isWhitespace(c)) {
                    throw new ParseException(ParseExceptionType.ILLEGAL_WHITESPACE, tag);
                } else {
                    throw new ParseException(ParseExceptionType.INVALID_QUOTED_STRING, tag);
                }
            } else if (quotesFound == 2) {
                if (isWhitespace(c)) {
                    throw new ParseException(ParseExceptionType.ILLEGAL_WHITESPACE, tag);
                } else {
                    throw new ParseException(ParseExceptionType.INVALID_QUOTED_STRING, tag);
                }
            } else if (i == quotedString.length() - 1) {
                if (c != '"' || isEscaping) {
                    throw new ParseException(ParseExceptionType.UNCLOSED_QUOTED_STRING, tag);
                }
            } else {
                if (isEscaping) {
                    builder.append(c);
                    isEscaping = false;
                } else {
                    if (c == '\\') {
                        isEscaping = true;
                    } else if (c == '"') {
                        ++quotesFound;
                    } else {
                        builder.append(c);
                    }
                }
            }
        }

        return builder.toString();
    }

    public static ByteRange matchByteRange(Matcher matcher) {
        long subRangeLength = Long.parseLong(matcher.group(1));
        Long offset = matcher.group(2) != null ? Long.parseLong(matcher.group(2)) : null;
        return new ByteRange(subRangeLength, offset);
    }

    static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    public static String decodeUri(String encodedUri, Encoding encoding) throws ParseException {
        try {
            return URLDecoder.decode(encodedUri.replace("+", "%2B"), encoding.value);
        } catch (UnsupportedEncodingException exception) {
            throw new ParseException(ParseExceptionType.INTERNAL_ERROR);
        }
    }

    public static Matcher match(Pattern pattern, String line, String tag) throws ParseException {
        final Matcher matcher = pattern.matcher(line);

        if (!matcher.matches()) {
            throw ParseException.create(ParseExceptionType.BAD_EXT_TAG_FORMAT, tag, line);
        }

        return matcher;
    }

    public static <T> void parseAttributes(String line, T builder, ParseState state, Map<String, ? extends AttributeParser<T>> handlers, String tag) throws ParseException {
        for (Attribute attribute : parseAttributeList(line, tag)) {
            if (handlers.containsKey(attribute.name)) {
                handlers.get(attribute.name).parse(attribute, builder, state);
            } else {
                throw ParseException.create(ParseExceptionType.INVALID_ATTRIBUTE_NAME, tag, line);
            }
        }
    }

    public static List<Attribute> parseAttributeList(String line, String tag) throws ParseException {
        final List<Attribute> attributes = new ArrayList<Attribute>();
        final Set<String> attributeNames = new HashSet<String>();

        for (String string : splitAttributeList(line, tag)) {
            final int separator = string.indexOf(Constants.ATTRIBUTE_SEPARATOR);
            final int quote = string.indexOf("\"");

            if (separator == -1 || (quote != -1 && quote < separator)) {
                throw ParseException.create(ParseExceptionType.MISSING_ATTRIBUTE_SEPARATOR, tag, attributes.toString());
            } else {
                //Even Apple playlists have sometimes spaces after a ,
                final String name = string.substring(0, separator).trim();
                final String value = string.substring(separator + 1);

                if (name.isEmpty()) {
                    throw ParseException.create(ParseExceptionType.MISSING_ATTRIBUTE_NAME, tag, attributes.toString());
                }

                if (value.isEmpty()) {
                    throw ParseException.create(ParseExceptionType.MISSING_ATTRIBUTE_VALUE, tag, attributes.toString());
                }

                if (!attributeNames.add(name)) {
                    throw ParseException.create(ParseExceptionType.MULTIPLE_ATTRIBUTE_NAME_INSTANCES, tag, attributes.toString());
                }

                attributes.add(new Attribute(name, value));
            }
        }

        return attributes;
    }

    public static List<String> splitAttributeList(String line, String tag) throws ParseException {
        final List<Integer> splitIndices = new ArrayList<Integer>();
        final List<String> attributes = new ArrayList<String>();

        int startIndex = line.indexOf(Constants.EXT_TAG_END) + 1;
        boolean isQuotedString = false;
        boolean isEscaping = false;

        for (int i = startIndex; i < line.length(); i++) {
            if (isQuotedString) {
                if (isEscaping) {
                    isEscaping = false;
                } else {
                    char c = line.charAt(i);

                    if (c == '\\') {
                        isEscaping = true;
                    } else if (c == '"') {
                        isQuotedString = false;
                    }
                }
            } else {
                char c = line.charAt(i);

                if (c == Constants.COMMA_CHAR) {
                    splitIndices.add(i);
                } else if (c == '"') {
                    isQuotedString = true;
                }
            }
        }

        if (isQuotedString) {
            throw new ParseException(ParseExceptionType.UNCLOSED_QUOTED_STRING, tag);
        }

        for (Integer splitIndex : splitIndices) {
            attributes.add(line.substring(startIndex, splitIndex));
            startIndex = splitIndex + 1;
        }

        attributes.add(line.substring(startIndex));
        return attributes;
    }
}
