package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.OutputStream;

import com.iheartradio.m3u8.data.Playlist;

public class PlaylistWriter {
    
    public void write(OutputStream outputStream, Playlist playlist, Format format, Encoding encoding) throws IOException, ParseException {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream is null");
        }
        if (format == null) {
            throw new IllegalArgumentException("format is null");
        }
        if (encoding == null) {
            throw new IllegalArgumentException("encoding is null");
        }

        switch (format) {
            case M3U:
                new M3uWriter(outputStream, encoding).write(playlist);
                break;
            case EXT_M3U:
                new ExtendedM3uWriter(outputStream, encoding).write(playlist);
                break;
            default:
                throw new RuntimeException("unsupported format detected, this should be impossible: " + format);
        }

    }
    
}
