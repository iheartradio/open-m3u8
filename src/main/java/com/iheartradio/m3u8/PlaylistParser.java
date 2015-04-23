package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.InputStream;

import com.iheartradio.m3u8.data.Playlist;

/**
 * Getting started with parsing is quite easy: Get a `PlaylistParser` and specify the format 
<pre>InputStream inputStream = ...
PlaylistParser parser = new PlaylistParser();
// The inputStream is automatically closed after this operation
Playlist playlist = parser.parse(inputStream, Format.EXT_M3U, Encoding.UTF_8);</pre>
 */
public class PlaylistParser {
    /**
     * This will close the InputStream and use a strict input stream parsing.
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param filename the extension of this filename will be used to determine the encoding required of the playlist
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist
     * @throws IOException if the InputStream throws an IOException
     * @throws ParseException if the data is not formatted properly
     */
    public Playlist parse(InputStream inputStream, Format format, String filename) throws IOException, ParseException {
        return parse(inputStream, format, parseExtension(filename), ParsingMode.STRICT);
    }
    
    /**
     * This will close the InputStream.
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param filename the extension of this filename will be used to determine the encoding required of the playlist
     * @param parsingMode indicates how to handle unknown lines in the input stream
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist
     * @throws IOException if the InputStream throws an IOException
     * @throws ParseException if the data is not formatted properly
     */
    public Playlist parse(InputStream inputStream, Format format, String filename, ParsingMode parsingMode) throws IOException, ParseException {
        if (filename == null) {
            throw new IllegalArgumentException("filename is null");
        }

        return parse(inputStream, format, parseExtension(filename), parsingMode);
    }
    
    /**
     * This will close the InputStream and use a strict input stream parsing.
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param extension requires the playlist be encoded according to this extension {M3U : windows-1252, M3U8 : utf-8}
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist
     * @throws IOException if the InputStream throws an IOException
     * @throws ParseException if the data is not formatted properly
     */
    public Playlist parse(InputStream inputStream, Format format, Extension extension) throws IOException, ParseException {
        if (extension == null) {
            throw new IllegalArgumentException("extension is null");
        }

        return parse(inputStream, format, extension.encoding, ParsingMode.STRICT);
    }

    /**
     * This will close the InputStream.
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param extension requires the playlist be encoded according to this extension {M3U : windows-1252, M3U8 : utf-8}
     * @param parsingMode indicates how to handle unknown lines in the input stream
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist
     * @throws IOException if the InputStream throws an IOException
     * @throws ParseException if the data is not formatted properly
     */
    public Playlist parse(InputStream inputStream, Format format, Extension extension, ParsingMode parsingMode) throws IOException, ParseException {
        if (extension == null) {
            throw new IllegalArgumentException("extension is null");
        }

        return parse(inputStream, format, extension.encoding, parsingMode);
    }

    /**
     * This will close the InputStream and use a strict input stream parsing
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param encoding required encoding for the playlist
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist
     * @throws IOException if the InputStream throws an IOException
     * @throws ParseException if the data is not formatted properly
     */
    public Playlist parse(InputStream inputStream, Format format, Encoding encoding) throws IOException, ParseException {
        return parse(inputStream, format, encoding, ParsingMode.STRICT);
    }
    
    /**
     * This will close the InputStream.
     * @param inputStream an open input stream positioned at the beginning of the file
     * @param format requires the playlist to be this format
     * @param encoding required encoding for the playlist
     * @param parsingMode indicates how to handle unknown lines in the input stream
     * @return Playlist which is either a MasterPlaylist or a MediaPlaylist
     * @throws IOException if the InputStream throws an IOException
     * @throws ParseException if the data is not formatted properly
     */
    public Playlist parse(InputStream inputStream, Format format, Encoding encoding, ParsingMode parsingMode) throws IOException, ParseException {
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
                return new M3uParser(inputStream, encoding).parse();
            case EXT_M3U:
                return new ExtendedM3uParser(inputStream, encoding).parse(parsingMode);
            default:
                throw new RuntimeException("unsupported format detected, this should be impossible: " + format);
        }

    }

    private static Extension parseExtension(String filename) {
        if (filename != null) {
            int index = filename.lastIndexOf(".");

            if (index != -1 ) {
                String extension = filename.substring(index + 1);

                if (Extension.M3U.value.equalsIgnoreCase(extension)) {
                    return Extension.M3U;
                } else if (Extension.M3U8.value.equalsIgnoreCase(extension)) {
                    return Extension.M3U8;
                } else {
                    throw new IllegalArgumentException("filename extension should be .m3u or .m3u8: " + filename);
                }
            }
        }

        throw new IllegalArgumentException("filename has no extension: " + filename);
    }
}
