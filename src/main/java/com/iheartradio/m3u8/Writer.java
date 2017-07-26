package com.iheartradio.m3u8;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.iheartradio.m3u8.data.Playlist;

abstract class Writer {
    final TagWriter tagWriter;
    
    Writer(OutputStream outputStream, Encoding encoding) {
        try {
            tagWriter = new TagWriter(new OutputStreamWriter(outputStream, encoding.getValue()));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    void writeTagLine(String tag) throws IOException {
        writeLine(Constants.COMMENT_PREFIX + tag);
    }
    
    void writeTagLine(String tag, Object value) throws IOException {
        writeLine(Constants.COMMENT_PREFIX + tag + Constants.EXT_TAG_END + value);
    }
    
    void writeLine(String line) throws IOException {
        tagWriter.write(line);
        tagWriter.write("\n");
    }
    
    final void write(Playlist playlist) throws IOException, ParseException, PlaylistException {
        doWrite(playlist);
        
        tagWriter.flush();
    }

    abstract void doWrite(Playlist playlist) throws IOException, ParseException, PlaylistException;

}
