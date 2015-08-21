package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.InputStream;

import com.iheartradio.m3u8.data.Playlist;

public class PlaylistParser implements IPlaylistParser {
    private final IPlaylistParser mPlaylistParser;

    /**
     * Equivalent to:
     * <pre>
     *     new PlaylistParser(inputStream, format, filename, ParsingMode.STRICT);
     * </pre>
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param filename the extension of this filename will be used to determine the encoding required of the playlist
     */
    public PlaylistParser(InputStream inputStream, Format format, String filename) {
        this(inputStream, format, parseExtension(filename), ParsingMode.STRICT);
    }
    
    /**
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param filename the extension of this filename will be used to determine the encoding required of the playlist
     * @param parsingMode indicates how to handle unknown lines in the input stream
     */
    public PlaylistParser(InputStream inputStream, Format format, String filename, ParsingMode parsingMode) {
        this(inputStream, format, parseExtension(filename), parsingMode);
    }
    
    /**
     * Equivalent to:
     * <pre>
     *     new PlaylistParser(inputStream, format, extension, ParsingMode.STRICT);
     * </pre>
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param extension requires the playlist be encoded according to this extension {M3U : windows-1252, M3U8 : utf-8}
     */
    public PlaylistParser(InputStream inputStream, Format format, Extension extension) {
        this(inputStream, format, extension.encoding, ParsingMode.STRICT);
    }

    /**
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param extension requires the playlist be encoded according to this extension {M3U : windows-1252, M3U8 : utf-8}
     * @param parsingMode indicates how to handle unknown lines in the input stream
     */
    public PlaylistParser(InputStream inputStream, Format format, Extension extension, ParsingMode parsingMode) {
        this(inputStream, format, extension.encoding, parsingMode);
    }

    /**
     * Equivalent to:
     * <pre>
     *     new PlaylistParser(inputStream, format, encoding, ParsingMode.STRICT);
     * </pre>
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param encoding required encoding for the playlist
     */
    public PlaylistParser(InputStream inputStream, Format format, Encoding encoding) {
        this(inputStream, format, encoding, ParsingMode.STRICT);
    }
    
    /**
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param encoding required encoding for the playlist
     * @param parsingMode indicates how to handle unknown lines in the input stream
     */
    public PlaylistParser(InputStream inputStream, Format format, Encoding encoding, ParsingMode parsingMode) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream is null");
        }

        if (format == null) {
            throw new IllegalArgumentException("format is null");
        }

        if (encoding == null) {
            throw new IllegalArgumentException("encoding is null");
        }

        if (parsingMode == null && format != Format.M3U) {
            throw new IllegalArgumentException("parsingMode is null");
        }

        switch (format) {
            case M3U:
                mPlaylistParser = new M3uParser(inputStream, encoding);
                break;
            case EXT_M3U:
                mPlaylistParser = new ExtendedM3uParser(inputStream, encoding, parsingMode);
                break;
            default:
                throw new RuntimeException("unsupported format detected, this should be impossible: " + format);
        }
    }

    /**
     * This will not close the InputStream.
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist, this will never return null
     * @throws IOException if the InputStream throws an IOException
     * @throws java.io.EOFException if there is no data to parse
     * @throws ParseException if there is a syntactic error in the playlist
     * @throws PlaylistException if the data in the parsed playlist is invalid
     */
    @Override
    public Playlist parse() throws IOException, ParseException, PlaylistException {
        return mPlaylistParser.parse();
    }

    /**
     * @return true if there is more data to parse, false otherwise
     */
    @Override
    public boolean isAvailable() {
        return mPlaylistParser.isAvailable();
    }

    private static Extension parseExtension(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        int index = filename.lastIndexOf(".");

        if (index == -1) {
            throw new IllegalArgumentException("filename has no extension: " + filename);
        } else {
            final String extension = filename.substring(index + 1);

            if (Extension.M3U.value.equalsIgnoreCase(extension)) {
                return Extension.M3U;
            } else if (Extension.M3U8.value.equalsIgnoreCase(extension)) {
                return Extension.M3U8;
            } else {
                throw new IllegalArgumentException("filename extension should be .m3u or .m3u8: " + filename);
            }
        }
    }
}
